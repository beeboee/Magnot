package com.beeboee.magnot.item;

import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.MagnotForgeEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Optional;

public final class FerrousTubeItem extends Item {
    public static final int MAX_REGION_AXIS_LENGTH = 25;
    private static final int MAX_REGION_AXIS_OFFSET = MAX_REGION_AXIS_LENGTH - 1;
    private static final int PLACEMENT_DAMAGE = 2;
    private static final String HAS_FIRST = "MagnotHasFirstCorner";
    private static final String FIRST_X = "MagnotFirstX";
    private static final String FIRST_Y = "MagnotFirstY";
    private static final String FIRST_Z = "MagnotFirstZ";
    private static final String FIRST_DIMENSION = "MagnotFirstDimension";

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

        BlockPos clicked = context.getClickedPos();
        if (player.isShiftKeyDown()) {
            if (getFirstCorner(stack).isPresent()) {
                clearFirstCorner(stack);
                player.displayClientMessage(Component.translatable("message.magnot.selection_cleared"), true);
            } else if (FerrousRegionSavedData.get(serverLevel).removeRegionContaining(clicked)) {
                player.displayClientMessage(Component.translatable("message.magnot.region_removed"), true);
            }
            return InteractionResult.SUCCESS;
        }

        Optional<BlockPos> first = getFirstCorner(stack);
        String dimension = serverLevel.dimension().location().toString();
        if (first.isEmpty() || !dimension.equals(getFirstDimension(stack))) {
            setFirstCorner(stack, clicked, dimension);
            player.displayClientMessage(Component.translatable("message.magnot.first_corner"), true);
            serverLevel.playSound(null, clicked, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.PLAYERS, 0.55F, 0.85F);
            return InteractionResult.SUCCESS;
        }

        BlockPos second = clampToRegionLimit(first.get(), clicked);
        FerrousRegion region = FerrousRegionSavedData.get(serverLevel).addRegion(first.get(), second);
        clearFirstCorner(stack);
        player.displayClientMessage(Component.translatable("message.magnot.region_created"), true);
        serverLevel.playSound(null, second, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.PLAYERS, 0.65F, 1.0F);
        MagnotForgeEvents.spawnOutline(serverLevel, region);
        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(PLACEMENT_DAMAGE, player, broken -> broken.broadcastBreakEvent(context.getHand()));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean selected) {
        if (!level.isClientSide() && !selected && getFirstCorner(stack).isPresent()) {
            clearFirstCorner(stack);
            if (entity instanceof Player player) {
                player.displayClientMessage(Component.translatable("message.magnot.selection_cleared"), true);
            }
        }
    }

    public static Optional<BlockPos> getFirstCorner(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.getBoolean(HAS_FIRST)) {
            return Optional.empty();
        }
        return Optional.of(new BlockPos(tag.getInt(FIRST_X), tag.getInt(FIRST_Y), tag.getInt(FIRST_Z)));
    }

    public static BlockPos clampToRegionLimit(BlockPos first, BlockPos second) {
        return new BlockPos(
                first.getX() + Mth.clamp(second.getX() - first.getX(), -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET),
                first.getY() + Mth.clamp(second.getY() - first.getY(), -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET),
                first.getZ() + Mth.clamp(second.getZ() - first.getZ(), -MAX_REGION_AXIS_OFFSET, MAX_REGION_AXIS_OFFSET)
        );
    }

    private static String getFirstDimension(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? "" : tag.getString(FIRST_DIMENSION);
    }

    private static void setFirstCorner(ItemStack stack, BlockPos pos, String dimension) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(HAS_FIRST, true);
        tag.putInt(FIRST_X, pos.getX());
        tag.putInt(FIRST_Y, pos.getY());
        tag.putInt(FIRST_Z, pos.getZ());
        tag.putString(FIRST_DIMENSION, dimension);
    }

    private static void clearFirstCorner(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(HAS_FIRST);
        tag.remove(FIRST_X);
        tag.remove(FIRST_Y);
        tag.remove(FIRST_Z);
        tag.remove(FIRST_DIMENSION);
    }
}
