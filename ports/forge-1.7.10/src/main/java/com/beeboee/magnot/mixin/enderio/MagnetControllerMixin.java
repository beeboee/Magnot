package com.beeboee.magnot.mixin.enderio;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
@Mixin(targets = "crazypants.enderio.item.MagnetController", remap = false)
public abstract class MagnetControllerMixin {
    @Redirect(
            method = "doHoover",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;selectEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/AxisAlignedBB;Lnet/minecraft/command/IEntitySelector;)Ljava/util/List;",
                    ordinal = 0,
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterPulledItems(
            World world,
            Class<T> entityClass,
            AxisAlignedBB bounds,
            IEntitySelector selector,
            EntityPlayer player
    ) {
        List<T> candidates = world.selectEntitiesWithinAABB(entityClass, bounds, selector);
        if (!(world instanceof WorldServer) || !EntityItem.class.isAssignableFrom(entityClass)) {
            return candidates;
        }

        WorldServer serverWorld = (WorldServer) world;
        Vec3 source = Vec3.createVectorHelper(player.posX, player.posY + player.height * 0.5D, player.posZ);
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
