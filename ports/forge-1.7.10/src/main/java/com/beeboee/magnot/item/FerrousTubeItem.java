package com.beeboee.magnot.item;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.FerrousEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
    public boolean onItemUse(
            ItemStack stack,
            EntityPlayer player,
            World world,
            int x,
            int y,
            int z,
            int side,
            float hitX,
            float hitY,
            float hitZ
    ) {
        if (world.isRemote || !(world instanceof WorldServer)) {
            return true;
        }

        WorldServer serverWorld = (WorldServer) world;
        if (player.isSneaking()) {
            if (hasFirstCorner(stack)) {
                clearFirstCorner(stack);
                player.addChatMessage(new ChatComponentTranslation("message.magnot.selection_cleared"));
            }
            return true;
        }

        int dimension = world.provider.dimensionId;
        int[] first = getFirstCorner(stack);
        if (first == null || dimension != getDimension(stack)) {
            setFirstCorner(stack, x, y, z, dimension);
            serverWorld.playSoundEffect(
                    x + 0.5D,
                    y + 0.5D,
                    z + 0.5D,
                    "mob.slime.small",
                    0.5F,
                    0.85F
            );
            FerrousEffects.spawnCorner(serverWorld, x, y, z);
            player.addChatMessage(new ChatComponentTranslation("message.magnot.first_corner"));
            return true;
        }

        int[] second = clampToRegionLimit(first, x, y, z);
        FerrousRegion region = FerrousRegion.fromCorners(
                UUID.randomUUID(),
                first[0],
                first[1],
                first[2],
                second[0],
                second[1],
                second[2]
        );
        FerrousRegionSavedData.get(serverWorld).addRegion(region);
        MagnotNetwork.syncDimension(serverWorld);
        clearFirstCorner(stack);

        serverWorld.playSoundEffect(
                second[0] + 0.5D,
                second[1] + 0.5D,
                second[2] + 0.5D,
                "mob.slime.small",
                0.5F,
                0.95F
        );
        FerrousEffects.spawnRegion(serverWorld, region);
        player.addChatMessage(new ChatComponentTranslation("message.magnot.region_created"));
        if (!player.capabilities.isCreativeMode) {
            stack.damageItem(REGION_PLACEMENT_DAMAGE, player);
        }
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isRemote && !selected && hasFirstCorner(stack)) {
            clearFirstCorner(stack);
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addChatMessage(
                        new ChatComponentTranslation("message.magnot.selection_cleared")
                );
            }
        }
    }

    public static int[] getFirstCorner(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.getBoolean(HAS_FIRST)) {
            return null;
        }
        return new int[]{
                tag.getInteger(FIRST_X),
                tag.getInteger(FIRST_Y),
                tag.getInteger(FIRST_Z)
        };
    }

    public static int[] clampToRegionLimit(int[] first, int x, int y, int z) {
        return new int[]{
                first[0] + MathHelper.clamp_int(x - first[0], -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET),
                first[1] + MathHelper.clamp_int(y - first[1], -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET),
                first[2] + MathHelper.clamp_int(z - first[2], -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET)
        };
    }

    public static boolean exceedsRegionLimit(int[] first, int x, int y, int z) {
        return Math.abs(x - first[0]) >= MAX_REGION_AXIS_LENGTH
                || Math.abs(y - first[1]) >= MAX_REGION_AXIS_LENGTH
                || Math.abs(z - first[2]) >= MAX_REGION_AXIS_LENGTH;
    }

    private static boolean hasFirstCorner(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean(HAS_FIRST);
    }

    private static int getDimension(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag == null ? Integer.MIN_VALUE : tag.getInteger(FIRST_DIMENSION);
    }

    private static NBTTagCompound getTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    private static void setFirstCorner(ItemStack stack, int x, int y, int z, int dimension) {
        NBTTagCompound tag = getTag(stack);
        tag.setBoolean(HAS_FIRST, true);
        tag.setInteger(FIRST_X, x);
        tag.setInteger(FIRST_Y, y);
        tag.setInteger(FIRST_Z, z);
        tag.setInteger(FIRST_DIMENSION, dimension);
    }

    private static void clearFirstCorner(ItemStack stack) {
        NBTTagCompound tag = getTag(stack);
        tag.removeTag(HAS_FIRST);
        tag.removeTag(FIRST_X);
        tag.removeTag(FIRST_Y);
        tag.removeTag(FIRST_Z);
        tag.removeTag(FIRST_DIMENSION);
    }
}
