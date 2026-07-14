package com.beeboee.magnot.item;

import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.MagnotForgeEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public final class FerrousTubeItem extends Item {
    private static final int LIMIT = 24;
    private static final String HAS_FIRST="MagnotHasFirstCorner", FIRST_X="MagnotFirstX", FIRST_Y="MagnotFirstY", FIRST_Z="MagnotFirstZ", FIRST_DIM="MagnotFirstDimension";
    public FerrousTubeItem(Properties properties) { super(properties); }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world=context.getLevel(); PlayerEntity player=context.getPlayer(); ItemStack stack=context.getItemInHand();
        if (player==null) return ActionResultType.PASS;
        if (world.isClientSide) return ActionResultType.SUCCESS;
        if (!(world instanceof ServerWorld)) return ActionResultType.PASS;
        ServerWorld level=(ServerWorld)world; BlockPos clicked=context.getClickedPos();
        if (player.isShiftKeyDown()) {
            if (first(stack).isPresent()) { clear(stack); player.displayClientMessage(new TranslationTextComponent("message.magnot.selection_cleared"), true); }
            else if (FerrousRegionSavedData.get(level).removeContaining(clicked)) player.displayClientMessage(new TranslationTextComponent("message.magnot.region_removed"), true);
            return ActionResultType.SUCCESS;
        }
        Optional<BlockPos> first=first(stack); String dimension=level.dimension().location().toString();
        if (!first.isPresent() || !dimension.equals(dimension(stack))) {
            setFirst(stack,clicked,dimension); player.displayClientMessage(new TranslationTextComponent("message.magnot.first_corner"),true);
            level.playSound(null,clicked, SoundEvents.SLIME_BLOCK_PLACE, SoundCategory.PLAYERS,0.55F,0.85F); return ActionResultType.SUCCESS;
        }
        BlockPos a=first.get(); BlockPos second=new BlockPos(a.getX()+ MathHelper.clamp(clicked.getX()-a.getX(),-LIMIT,LIMIT),a.getY()+MathHelper.clamp(clicked.getY()-a.getY(),-LIMIT,LIMIT),a.getZ()+MathHelper.clamp(clicked.getZ()-a.getZ(),-LIMIT,LIMIT));
        FerrousRegion region=FerrousRegionSavedData.get(level).addRegion(a,second); clear(stack);
        player.displayClientMessage(new TranslationTextComponent("message.magnot.region_created"),true); MagnotForgeEvents.spawnOutline(level,region);
        if (!player.abilities.instabuild) stack.hurtAndBreak(2,player,broken -> broken.broadcastBreakEvent(context.getHand()));
        return ActionResultType.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) { if (!world.isClientSide && !selected && first(stack).isPresent()) clear(stack); }
    private static Optional<BlockPos> first(ItemStack stack) { CompoundNBT tag=stack.getTag(); if (tag==null || !tag.getBoolean(HAS_FIRST)) return Optional.empty(); return Optional.of(new BlockPos(tag.getInt(FIRST_X),tag.getInt(FIRST_Y),tag.getInt(FIRST_Z))); }
    private static String dimension(ItemStack stack) { return stack.getTag()==null?"":stack.getTag().getString(FIRST_DIM); }
    private static void setFirst(ItemStack stack,BlockPos pos,String dimension) { CompoundNBT tag=stack.getOrCreateTag(); tag.putBoolean(HAS_FIRST,true); tag.putInt(FIRST_X,pos.getX()); tag.putInt(FIRST_Y,pos.getY()); tag.putInt(FIRST_Z,pos.getZ()); tag.putString(FIRST_DIM,dimension); }
    private static void clear(ItemStack stack) { CompoundNBT tag=stack.getOrCreateTag(); tag.remove(HAS_FIRST); tag.remove(FIRST_X); tag.remove(FIRST_Y); tag.remove(FIRST_Z); tag.remove(FIRST_DIM); }
}
