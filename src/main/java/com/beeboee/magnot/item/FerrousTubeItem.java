package com.beeboee.magnot.item;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.FerrousParticles;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class FerrousTubeItem extends Item {
    public static final int MAX_REGION_AXIS_LENGTH = 25;
    private static final int MAX_REGION_AXIS_OFFSET = MAX_REGION_AXIS_LENGTH - 1;
    private static final int REGION_PLACEMENT_DAMAGE = 2;
    private static final String HAS_FIRST = "MagnotHasFirstCorner";
    private static final String FIRST_X = "MagnotFirstX";
    private static final String FIRST_Y = "MagnotFirstY";
    private static final String FIRST_Z = "MagnotFirstZ";

    public FerrousTubeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            if (getFirstCorner(stack).isPresent()) {
                clearFirstCorner(stack);
                player.displayClientMessage(Component.translatable("message.magnot.selection_cleared"), true);
            }
            return InteractionResult.SUCCESS;
        }

        BlockPos clicked = context.getClickedPos();
        Optional<BlockPos> firstCorner = getFirstCorner(stack);

        if (firstCorner.isEmpty()) {
            setFirstCorner(stack, clicked);
            AllSoundEvents.SLIME_ADDED.play(serverLevel, null, clicked, 0.5F, 0.85F);
            FerrousParticles.spawnRedstoneBlockBreak(serverLevel, clicked);
            return InteractionResult.SUCCESS;
        }

        BlockPos clampedSecondCorner = clampToRegionLimit(firstCorner.get(), clicked);
        FerrousRegion region = FerrousRegionSavedData.get(serverLevel).addRegion(firstCorner.get(), clampedSecondCorner);
        MagnotNetwork.syncToPlayersInDimension(serverLevel);
        clearFirstCorner(stack);
        player.displayClientMessage(Component.translatable("message.magnot.region_created"), true);
        AllSoundEvents.SLIME_ADDED.play(serverLevel, null, clampedSecondCorner, 0.5F, 0.95F);
        FerrousParticles.spawnRedstoneBlockEdges(serverLevel, region);
        damageTubeIfNeeded(player, stack, context.getHand());
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide() || isSelected || getFirstCorner(stack).isEmpty()) {
            return;
        }

        clearFirstCorner(stack);
        if (entity instanceof Player player) {
            player.displayClientMessage(Component.translatable("message.magnot.selection_cleared"), true);
        }
    }

    public static Optional<BlockPos> getFirstCorner(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null || !data.contains(HAS_FIRST)) {
            return Optional.empty();
        }

        CompoundTag tag = data.copyTag();
        return Optional.of(new BlockPos(tag.getInt(FIRST_X), tag.getInt(FIRST_Y), tag.getInt(FIRST_Z)));
    }

    public static BlockPos clampToRegionLimit(BlockPos first, BlockPos second) {
        return new BlockPos(
                first.getX() + Mth.clamp(second.getX() - first.getX(), -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET),
                first.getY() + Mth.clamp(second.getY() - first.getY(), -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET),
                first.getZ() + Mth.clamp(second.getZ() - first.getZ(), -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET)
        );
    }

    public static boolean exceedsRegionLimit(BlockPos first, BlockPos second) {
        return Math.abs(second.getX() - first.getX()) >= MAX_REGION_AXIS_LENGTH
                || Math.abs(second.getY() - first.getY()) >= MAX_REGION_AXIS_LENGTH
                || Math.abs(second.getZ() - first.getZ()) >= MAX_REGION_AXIS_LENGTH;
    }

    private static void damageTubeIfNeeded(Player player, ItemStack stack, InteractionHand hand) {
        if (player.getAbilities().instabuild) {
            return;
        }

        stack.hurtAndBreak(REGION_PLACEMENT_DAMAGE, player, LivingEntity.getSlotForHand(hand));
    }

    private static void setFirstCorner(ItemStack stack, BlockPos pos) {
        CompoundTag tag = copyCustomData(stack);
        tag.putBoolean(HAS_FIRST, true);
        tag.putInt(FIRST_X, pos.getX());
        tag.putInt(FIRST_Y, pos.getY());
        tag.putInt(FIRST_Z, pos.getZ());
        CustomData.set(DataComponents.CUSTOM_DATA, stack, tag);
    }

    private static void clearFirstCorner(ItemStack stack) {
        CompoundTag tag = copyCustomData(stack);
        tag.remove(HAS_FIRST);
        tag.remove(FIRST_X);
        tag.remove(FIRST_Y);
        tag.remove(FIRST_Z);
        CustomData.set(DataComponents.CUSTOM_DATA, stack, tag);
    }

    private static CompoundTag copyCustomData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }
}
