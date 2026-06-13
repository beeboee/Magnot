package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.registry.MagnotItems;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = Magnot.MOD_ID, value = Dist.CLIENT)
public final class MagnotClientEvents {
    private static final Object SELECTION_OUTLINE_SLOT = new Object();
    private static final int FERROUS_RED = 0xC63A32;

    private MagnotClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return;
        }

        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return;
        }

        var firstCorner = FerrousTubeItem.getFirstCorner(held);
        if (firstCorner.isEmpty()) {
            return;
        }

        HitResult hitResult = minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }

        AABB selectionBox = boxBetween(firstCorner.get(), blockHitResult.getBlockPos());
        Outliner.getInstance()
                .showAABB(SELECTION_OUTLINE_SLOT, selectionBox)
                .colored(FERROUS_RED)
                .disableLineNormals()
                .lineWidth(1.0F / 16.0F);
    }

    private static AABB boxBetween(BlockPos first, BlockPos second) {
        int minX = Math.min(first.getX(), second.getX());
        int minY = Math.min(first.getY(), second.getY());
        int minZ = Math.min(first.getZ(), second.getZ());
        int maxX = Math.max(first.getX(), second.getX()) + 1;
        int maxY = Math.max(first.getY(), second.getY()) + 1;
        int maxZ = Math.max(first.getZ(), second.getZ()) + 1;
        return new AABB(new Vec3(minX, minY, minZ), new Vec3(maxX, maxY, maxZ));
    }
}
