package com.beeboee.magnot.item;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.FerrousEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Optional;
import java.util.UUID;

public final class FerrousTubeItem extends Item {
    public static final int MAX_REGION_AXIS_LENGTH = 25;
    private static final int MAX_REGION_AXIS_OFFSET = MAX_REGION_AXIS_LENGTH - 1;
    private static final int REGION_PLACEMENT_DAMAGE = 2;

    private static final String HAS_FIRST = "MagnotHasFirstCorner";
    private static final String FIRST_X = "MagnotFirstX";
    private static final String FIRST_Y = "MagnotFirstY";
    private static final String FIRST_Z = "MagnotFirstZ";
    private static final String FIRST_DIMENSION = "MagnotFirstDimension";

    public FerrousTubeItem() {
        setMaxStackSize(1);
        setMaxDamage(99);
    }

    @Override
    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing facing,
            float hitX,
            float hitY,
            float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (!(world instanceof WorldServer)) {
            return EnumActionResult.PASS;
        }

        WorldServer serverWorld = (WorldServer) world;
        if (player.isSneaking()) {
            if (getFirstCorner(stack).isPresent()) {
                clear(stack);
                player.sendStatusMessage(
                        new TextComponentTranslation("message.magnot.selection_cleared"),
                        true
                );
            }
            return EnumActionResult.SUCCESS;
        }

        Optional<BlockPos> first = getFirstCorner(stack);
        int dimension = world.provider.getDimension();
        if (!first.isPresent() || dimension != getDimension(stack)) {
            setFirst(stack, pos, dimension);
            serverWorld.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_SLIME_PLACE,
                    SoundCategory.BLOCKS,
                    0.5F,
                    0.85F
            );
            FerrousEffects.spawnCornerParticles(serverWorld, pos);
            player.sendStatusMessage(new TextComponentTranslation("message.magnot.first_corner"), true);
            return EnumActionResult.SUCCESS;
        }

        BlockPos second = clampToRegionLimit(first.get(), pos);
        FerrousRegion region = FerrousRegion.fromCorners(UUID.randomUUID(), first.get(), second);
        FerrousRegionSavedData.get(serverWorld).addRegion(region);
        MagnotNetwork.syncDimension(serverWorld);
        clear(stack);

        serverWorld.playSound(
                null,
                second,
                SoundEvents.BLOCK_SLIME_PLACE,
                SoundCategory.BLOCKS,
                0.5F,
                0.95F
        );
        FerrousEffects.spawnRegionParticles(serverWorld, region);
        player.sendStatusMessage(new TextComponentTranslation("message.magnot.region_created"), true);
        if (!player.capabilities.isCreativeMode) {
            stack.damageItem(REGION_PLACEMENT_DAMAGE, player);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isRemote && !selected && getFirstCorner(stack).isPresent()) {
            clear(stack);
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).sendStatusMessage(
                        new TextComponentTranslation("message.magnot.selection_cleared"),
                        true
                );
            }
        }
    }

    public static Optional<BlockPos> getFirstCorner(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.getBoolean(HAS_FIRST)) {
            return Optional.empty();
        }
        return Optional.of(new BlockPos(
                tag.getInteger(FIRST_X),
                tag.getInteger(FIRST_Y),
                tag.getInteger(FIRST_Z)
        ));
    }

    public static BlockPos clampToRegionLimit(BlockPos first, BlockPos second) {
        return new BlockPos(
                first.getX() + MathHelper.clamp(
                        second.getX() - first.getX(),
                        -MAX_REGION_AXIS_OFFSET,
                        MAX_REGION_AXIS_OFFSET
                ),
                first.getY() + MathHelper.clamp(
                        second.getY() - first.getY(),
                        -MAX_REGION_AXIS_OFFSET,
                        MAX_REGION_AXIS_OFFSET
                ),
                first.getZ() + MathHelper.clamp(
                        second.getZ() - first.getZ(),
                        -MAX_REGION_AXIS_OFFSET,
                        MAX_REGION_AXIS_OFFSET
                )
        );
    }

    public static boolean exceedsRegionLimit(BlockPos first, BlockPos second) {
        return Math.abs(second.getX() - first.getX()) >= MAX_REGION_AXIS_LENGTH
                || Math.abs(second.getY() - first.getY()) >= MAX_REGION_AXIS_LENGTH
                || Math.abs(second.getZ() - first.getZ()) >= MAX_REGION_AXIS_LENGTH;
    }

    private static int getDimension(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag == null ? Integer.MIN_VALUE : tag.getInteger(FIRST_DIMENSION);
    }

    private static NBTTagCompound tag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    private static void setFirst(ItemStack stack, BlockPos pos, int dimension) {
        NBTTagCompound tag = tag(stack);
        tag.setBoolean(HAS_FIRST, true);
        tag.setInteger(FIRST_X, pos.getX());
        tag.setInteger(FIRST_Y, pos.getY());
        tag.setInteger(FIRST_Z, pos.getZ());
        tag.setInteger(FIRST_DIMENSION, dimension);
    }

    private static void clear(ItemStack stack) {
        NBTTagCompound tag = tag(stack);
        tag.removeTag(HAS_FIRST);
        tag.removeTag(FIRST_X);
        tag.removeTag(FIRST_Y);
        tag.removeTag(FIRST_Z);
        tag.removeTag(FIRST_DIMENSION);
    }
}
