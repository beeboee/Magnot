package com.beeboee.magnot.server;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;

import java.util.Optional;
import java.util.UUID;

public final class RegionActions {
    private static final double REMOVAL_RANGE = 6.0D;

    private RegionActions() {
    }

    public static boolean remove(EntityPlayerMP player, UUID selectedRegionId) {
        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() != MagnotItems.FERROUS_TUBE) {
            return false;
        }

        WorldServer world = player.getServerWorld();
        Vec3d source = player.getPositionEyes(1.0F);
        Vec3d target = source.add(player.getLookVec().scale(REMOVAL_RANGE));
        Optional<FerrousRegion> removed =
                FerrousRegionSavedData.get(world).removeIntersectingById(selectedRegionId, source, target);
        if (!removed.isPresent()) {
            return false;
        }

        world.playSound(
                null,
                removed.get().min(),
                SoundEvents.BLOCK_SLIME_BREAK,
                SoundCategory.BLOCKS,
                0.5F,
                0.75F
        );
        FerrousEffects.spawnRegionParticles(world, removed.get());
        MagnotNetwork.syncDimension(world);
        player.sendStatusMessage(new TextComponentTranslation("message.magnot.region_removed"), true);
        return true;
    }
}
