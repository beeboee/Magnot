package com.beeboee.magnot.mixin.simplemagnets;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Pseudo
@Mixin(targets = "com.supermartijn642.simplemagnets.MagnetItem", remap = false)
public abstract class MagnetItemMixin {
    @Redirect(method = "inventoryUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;)Ljava/util/List;"), require = 0)
    private <T extends Entity> List<T> magnot$filter(World world, Class<T> type, AxisAlignedBB box,
                                                     Predicate<? super T> predicate, ItemStack stack,
                                                     World original, Entity source, int slot,
                                                     boolean selected) {
        List<T> candidates = world.getEntitiesOfClass(type, box, predicate);
        if (!(world instanceof ServerWorld) || !(source instanceof PlayerEntity)) return candidates;
        ServerWorld level = (ServerWorld) world;
        PlayerEntity player = (PlayerEntity) source;
        return candidates.stream()
                .filter(candidate -> !(candidate instanceof ItemEntity)
                        || !FerrousMagnetRules.blocksPlayerItemPull(level, player, (ItemEntity) candidate))
                .collect(Collectors.toList());
    }
}
