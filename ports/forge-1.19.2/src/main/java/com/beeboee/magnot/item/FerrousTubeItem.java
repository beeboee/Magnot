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
    private static final int LIMIT = 24;
    private static final String HAS_FIRST = "MagnotHasFirstCorner";
    private static final String FIRST_X = "MagnotFirstX";
    private static final String FIRST_Y = "MagnotFirstY";
    private static final String FIRST_Z = "MagnotFirstZ";
    private static final String FIRST_DIMENSION = "MagnotFirstDimension";

    public FerrousTubeItem(Properties properties) { super(properties); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (player == null) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level instanceof ServerLevel)) return InteractionResult.PASS;
        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos clicked = context.getClickedPos();

        if (player.isShiftKeyDown()) {
            if (getFirstCorner(stack).isPresent()) {
                clear(stack);
                player.displayClientMessage(Component.translatable("message.magnot.selection_cleared"), true);
            } else if (FerrousRegionSavedData.get(serverLevel).removeRegionContaining(clicked)) {
                player.displayClientMessage(Component.translatable("message.magnot.region_removed"), true);
            }
            return InteractionResult.SUCCESS;
        }

        Optional<BlockPos> first = getFirstCorner(stack);
        String dimension = serverLevel.dimension().location().toString();
        if (first.isEmpty() || !dimension.equals(getDimension(stack))) {
            setFirst(stack, clicked, dimension);
            player.displayClientMessage(Component.translatable("message.magnot.first_corner"), true);
            serverLevel.playSound(null, clicked, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.PLAYERS, 0.55F, 0.85F);
            return InteractionResult.SUCCESS;
        }

        BlockPos second = new BlockPos(
                first.get().getX() + Mth.clamp(clicked.getX() - first.get().getX(), -LIMIT, LIMIT),
                first.get().getY() + Mth.clamp(clicked.getY() - first.get().getY(), -LIMIT, LIMIT),
                first.get().getZ() + Mth.clamp(clicked.getZ() - first.get().getZ(), -LIMIT, LIMIT)
        );
        FerrousRegion region = FerrousRegionSavedData.get(serverLevel).addRegion(first.get(), second);
        clear(stack);
        player.displayClientMessage(Component.translatable("message.magnot.region_created"), true);
        MagnotForgeEvents.spawnOutline(serverLevel, region);
        if (!player.getAbilities().instabuild) stack.hurtAndBreak(2, player, broken -> broken.broadcastBreakEvent(context.getHand()));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide && !selected && getFirstCorner(stack).isPresent()) clear(stack);
    }

    public static Optional<BlockPos> getFirstCorner(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.getBoolean(HAS_FIRST)) return Optional.empty();
        return Optional.of(new BlockPos(tag.getInt(FIRST_X), tag.getInt(FIRST_Y), tag.getInt(FIRST_Z)));
    }

    private static String getDimension(ItemStack stack) { return stack.getTag() == null ? "" : stack.getTag().getString(FIRST_DIMENSION); }
    private static void setFirst(ItemStack stack, BlockPos pos, String dimension) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(HAS_FIRST, true); tag.putInt(FIRST_X, pos.getX()); tag.putInt(FIRST_Y, pos.getY()); tag.putInt(FIRST_Z, pos.getZ()); tag.putString(FIRST_DIMENSION, dimension);
    }
    private static void clear(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(HAS_FIRST); tag.remove(FIRST_X); tag.remove(FIRST_Y); tag.remove(FIRST_Z); tag.remove(FIRST_DIMENSION);
    }
}
