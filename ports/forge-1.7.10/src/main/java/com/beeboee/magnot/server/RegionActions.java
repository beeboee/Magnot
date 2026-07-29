package com.beeboee.magnot.server;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;

import java.util.UUID;

public final class RegionActions {
    private static final double REMOVAL_RANGE = 6.0D;

    private RegionActions() {
    }

    public static boolean remove(EntityPlayerMP player, UUID selectedRegionId) {
        ItemStack held = player.getHeldItem();
        if (held == null || held.getItem() != Magnot.FERROUS_TUBE) {
            return false;
        }

        WorldServer world = (WorldServer) player.worldObj;
        Vec3 source = Vec3.createVectorHelper(
                player.posX,
                player.posY + player.getEyeHeight(),
                player.posZ
        );
        Vec3 look = player.getLook(1.0F);
        Vec3 target = source.addVector(
                look.xCoord * REMOVAL_RANGE,
                look.yCoord * REMOVAL_RANGE,
                look.zCoord * REMOVAL_RANGE
        );
        FerrousRegion removed =
                FerrousRegionSavedData.get(world).removeIntersectingById(selectedRegionId, source, target);
        if (removed == null) {
            return false;
        }

        world.playSoundEffect(
                removed.getMinX() + 0.5D,
                removed.getMinY() + 0.5D,
                removed.getMinZ() + 0.5D,
                "mob.slime.small",
                0.5F,
                0.75F
        );
        FerrousEffects.spawnRegion(world, removed);
        MagnotNetwork.syncDimension(world);
        player.addChatMessage(new ChatComponentTranslation("message.magnot.region_removed"));
        return true;
    }
}
