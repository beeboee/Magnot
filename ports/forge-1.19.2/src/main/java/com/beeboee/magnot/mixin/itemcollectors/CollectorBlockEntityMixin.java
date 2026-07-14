package com.beeboee.magnot.mixin.itemcollectors;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Pseudo
@Mixin(targets="com.supermartijn642.itemcollectors.CollectorBlockEntity", remap=false)
public abstract class CollectorBlockEntityMixin {
    @Redirect(method="update", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"), require=0)
    private <T extends Entity> List<T> magnot$filter(Level level, Class<T> type, AABB box, Predicate<? super T> predicate) {
        List<T> candidates=level.getEntitiesOfClass(type,box,predicate);
        if (!(level instanceof ServerLevel) || !ItemEntity.class.isAssignableFrom(type)) return candidates;
        ServerLevel serverLevel=(ServerLevel)level; Vec3 source=Vec3.atCenterOf(((BlockEntity)(Object)this).getBlockPos());
        return candidates.stream().filter(candidate -> !(candidate instanceof ItemEntity) || !FerrousMagnetRules.blocksItemPull(serverLevel,source,(ItemEntity)candidate)).toList();
    }
}
