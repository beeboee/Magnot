package com.beeboee.magnot.mixin.openblocks;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "openblocks.common.tileentity.TileEntityVacuumHopper", remap = false)
public abstract class VacuumHopperMixin {
    @Inject(
            method = {"isEntityApplicable", "func_82704_a"},
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    private void magnot$rejectBlockedItems(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof EntityItem) || !(entity.worldObj instanceof WorldServer)) {
            return;
        }

        TileEntity hopper = (TileEntity) (Object) this;
        Vec3 source = Vec3.createVectorHelper(
                hopper.xCoord + 0.5D,
                hopper.yCoord + 0.5D,
                hopper.zCoord + 0.5D
        );
        EntityItem item = (EntityItem) entity;
        Vec3 target = Vec3.createVectorHelper(item.posX, item.posY + item.height * 0.5D, item.posZ);
        if (FerrousMagnetRules.blocksMagnet((WorldServer) entity.worldObj, source, target)) {
            cir.setReturnValue(Boolean.FALSE);
        }
    }
}
