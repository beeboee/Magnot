package com.beeboee.magnot.mixin.draconicevolution;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "com.brandon3055.draconicevolution.items.tools.Magnet", remap = false)
public abstract class MagnetMixin {
    @Redirect(
            method = "updateMagnet",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterItems(
            World world,
            Class<T> entityClass,
            AxisAlignedBB box,
            ItemStack stack,
            Entity source
    ) {
        List<T> candidates = world.getEntitiesWithinAABB(entityClass, box);
        if (!(world instanceof WorldServer)
                || !(source instanceof EntityPlayer)
                || !EntityItem.class.isAssignableFrom(entityClass)) {
            return candidates;
        }
        final WorldServer serverWorld = (WorldServer) world;
        final EntityPlayer player = (EntityPlayer) source;
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof EntityItem)
                        || !FerrousMagnetRules.blocksPlayerItemPull(serverWorld, player, (EntityItem) candidate))
                .collect(Collectors.toList());
    }
}
