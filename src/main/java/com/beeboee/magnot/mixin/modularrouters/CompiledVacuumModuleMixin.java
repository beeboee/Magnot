package com.beeboee.magnot.mixin.modularrouters;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Pseudo
@Mixin(targets = "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", remap = false)
public abstract class CompiledVacuumModuleMixin {
    @Redirect(
            method = "handleItemMode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            ),
            require = 0
    )
    private <T extends Entity> List<T> magnot$filterVacuumModuleItems(Level level, Class<T> entityClass, AABB box, @Coerce Object router) {
        List<T> candidates = level.getEntitiesOfClass(entityClass, box);
        if (!(level instanceof ServerLevel serverLevel) || !ItemEntity.class.isAssignableFrom(entityClass)) {
            return candidates;
        }

        Vec3 source = box.getCenter();
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity item)
                        || !FerrousMagnetRules.blocksItemPull(serverLevel, source, item))
                .toList();
    }
}
