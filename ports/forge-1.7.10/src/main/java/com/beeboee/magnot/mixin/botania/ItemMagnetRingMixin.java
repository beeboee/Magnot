package com.beeboee.magnot.mixin.botania;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

@Pseudo
@Mixin(targets = "vazkii.botania.common.item.equipment.bauble.ItemMagnetRing", remap = false)
public abstract class ItemMagnetRingMixin {
    @Redirect(
            method = "onWornTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterPulledItems(
            World world,
            Class<T> entityClass,
            AxisAlignedBB bounds,
            ItemStack ring,
            EntityLivingBase wearer
    ) {
        List<T> candidates = world.getEntitiesWithinAABB(entityClass, bounds);
        if (!(world instanceof WorldServer)
                || !(wearer instanceof EntityPlayer)
                || !EntityItem.class.isAssignableFrom(entityClass)) {
            return candidates;
        }

        WorldServer serverWorld = (WorldServer) world;
        Vec3 source = Vec3.createVectorHelper(wearer.posX, wearer.posY + wearer.height * 0.5D, wearer.posZ);
        Iterator<T> iterator = candidates.iterator();
        while (iterator.hasNext()) {
            T candidate = iterator.next();
            if (candidate instanceof EntityItem) {
                EntityItem item = (EntityItem) candidate;
                Vec3 target = Vec3.createVectorHelper(item.posX, item.posY + item.height * 0.5D, item.posZ);
                if (FerrousMagnetRules.blocksMagnet(serverWorld, source, target)) {
                    iterator.remove();
                }
            }
        }
        return candidates;
    }
}
