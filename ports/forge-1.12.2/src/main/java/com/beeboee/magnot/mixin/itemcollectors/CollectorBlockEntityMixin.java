package com.beeboee.magnot.mixin.itemcollectors;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "com.supermartijn642.itemcollectors.CollectorBlockEntity", remap = false)
public abstract class CollectorBlockEntityMixin {
    @Redirect(
            method = {"update", "lambda$update$0", "lambda$update$1"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;",
                    remap = true
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterItems(World world, Class<T> entityClass, AxisAlignedBB box) {
        List<T> candidates = world.getEntitiesWithinAABB(entityClass, box);
        if (!(world instanceof WorldServer) || !EntityItem.class.isAssignableFrom(entityClass)) {
            return candidates;
        }
        BlockPos pos = ((TileEntity) (Object) this).getPos();
        Vec3d source = new Vec3d(pos).addVector(0.5D, 0.5D, 0.5D);
        final WorldServer serverWorld = (WorldServer) world;
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof EntityItem)
                        || !FerrousMagnetRules.blocksItemPull(serverWorld, source, (EntityItem) candidate))
                .collect(Collectors.toList());
    }
}
