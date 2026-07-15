# Unresolved magnet implementation scan

## main-1.21.1-neoforge — 13.2.1

| Score | Class | Signals |
|---:|---|---|
| 65 | `artifacts.effect.MagnetismMobEffect` | ItemEntity, getEntitiesOfClass, setDeltaMovement, Magnetism, artifacts$ |
| 35 | `artifacts.extensions.mobeffect.magnetism.ItemEntityExtensions` | ItemEntity, artifacts$ |
| 35 | `artifacts.mixin.mobeffect.magnetism.ItemEntityMixin` | ItemEntity, artifacts$ |
| 35 | `artifacts.mixin.mobeffect.magnetism.PlayerMixin` | ItemEntity, artifacts$ |
| 10 | `artifacts.client.item.ArtifactLayers` | UniversalAttractor |
| 10 | `artifacts.client.item.ArtifactRenderers` | UniversalAttractor |
| 10 | `artifacts.client.item.model.ArmsModel$1` | artifacts$ |
| 10 | `artifacts.client.item.model.BeltModel` | UniversalAttractor |
| 10 | `artifacts.component.ability.DoubleJump` | setDeltaMovement |
| 10 | `artifacts.config.ItemConfigs` | Magnetism |
| 10 | `artifacts.event.ArtifactHooks` | artifacts$ |
| 10 | `artifacts.extensions.ability.LivingEntityExtensions` | artifacts$ |
| 10 | `artifacts.extensions.pocketpiston.LivingEntityExtensions` | artifacts$ |
| 10 | `artifacts.mixin.ability.LivingEntityMixin` | artifacts$ |
| 10 | `artifacts.mixin.ability.fluidcollision.EntityMixin` | artifacts$ |
| 10 | `artifacts.mixin.ability.phantomrepellent.PhantomSweepAttackGoalMixin` | artifacts$ |
| 10 | `artifacts.mixin.attribute.invincibilityticks.LivingEntityMixin` | artifacts$ |
| 10 | `artifacts.mixin.compat.trinkets.client.PlayerRendererMixin` | artifacts$ |
| 10 | `artifacts.mixin.item.pocketpiston.client.LivingEntityMixin` | artifacts$ |
| 10 | `artifacts.neoforge.data.Language` | Magnetism |
| 10 | `artifacts.neoforge.data.LootModifiers$Builder` | ItemEntity |
| 10 | `artifacts.neoforge.event.ArtifactHooksNeoForge` | ItemEntity |
| 10 | `artifacts.registry.ModItems` | Magnetism |
| 10 | `artifacts.registry.ModMobEffects` | Magnetism |

## forge-1.20.1 — 9.5.19

| Score | Class | Signals |
|---:|---|---|
| 40 | `artifacts.item.wearable.belt.UniversalAttractorItem` | ItemEntity, UniversalAttractor |
| 10 | `artifacts.client.item.ArtifactLayers` | UniversalAttractor |
| 10 | `artifacts.client.item.ArtifactRenderers` | UniversalAttractor |
| 10 | `artifacts.client.item.model.ArmsModel$1` | artifacts$ |
| 10 | `artifacts.client.item.model.BeltModel` | UniversalAttractor |
| 10 | `artifacts.extensions.pocketpiston.LivingEntityExtensions` | artifacts$ |
| 10 | `artifacts.mixin.item.wearable.pocketpiston.client.ItemInHandLayerMixin` | artifacts$ |
| 10 | `artifacts.mixin.item.wearable.pocketpiston.client.LivingEntityMixin` | artifacts$ |
| 10 | `artifacts.registry.ModItems` | UniversalAttractor |

## fabric-1.20.1 — 9.5.17

| Score | Class | Signals |
|---:|---|---|
| 30 | `artifacts.item.wearable.belt.UniversalAttractorItem` | UniversalAttractor |
| 10 | `artifacts.client.item.ArtifactLayers` | UniversalAttractor |
| 10 | `artifacts.client.item.ArtifactRenderers` | UniversalAttractor |
| 10 | `artifacts.client.item.model.ArmsModel$1` | artifacts$ |
| 10 | `artifacts.client.item.model.BeltModel` | UniversalAttractor |
| 10 | `artifacts.extensions.pocketpiston.LivingEntityExtensions` | artifacts$ |
| 10 | `artifacts.mixin.item.wearable.pocketpiston.client.ItemInHandLayerMixin` | artifacts$ |
| 10 | `artifacts.mixin.item.wearable.pocketpiston.client.LivingEntityMixin` | artifacts$ |
| 10 | `artifacts.registry.ModItems` | UniversalAttractor |

## forge-1.19.2 — 5.0.6+forge

| Score | Class | Signals |
|---:|---|---|
| 40 | `artifacts.common.item.curio.belt.UniversalAttractorItem` | ItemEntity, UniversalAttractor |
| 30 | `artifacts.common.config.item.curio.belt.UniversalAttractorConfig` | UniversalAttractor |
| 10 | `artifacts.client.render.curio.CurioLayers` | UniversalAttractor |
| 10 | `artifacts.client.render.curio.CurioRenderers` | UniversalAttractor |
| 10 | `artifacts.client.render.curio.model.BeltModel` | UniversalAttractor |
| 10 | `artifacts.common.config.ServerConfig` | UniversalAttractor |
| 10 | `artifacts.common.init.ModItems` | UniversalAttractor |
| 10 | `artifacts.data.LootModifiers$Builder` | ItemEntity |

## forge-1.18.2 — 1.0.0

| Score | Class | Signals |
|---:|---|---|

## Main pinned ProjectE

- BlackHoleBand class found: **True**

```text
  public void inventoryTick(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);
       8: invokespecial #209                // Method moze_intel/projecte/gameObjs/items/rings/PEToggleItem.inventoryTick:(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;IZ)V
      45: ldc           #230                // class net/minecraft/world/entity/item/ItemEntity
      58: invokevirtual #246                // Method net/minecraft/world/level/Level.getEntitiesOfClass:(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;
      85: checkcast     #230                // class net/minecraft/world/entity/item/ItemEntity
     100: invokevirtual #274                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
     108: invokevirtual #274                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
     124: invokestatic  #292                // Method moze_intel/projecte/utils/WorldHelper.gravitateEntityTowards:(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V
      19: ldc           #230                // class net/minecraft/world/entity/item/ItemEntity
      36: invokevirtual #339                // Method net/minecraft/world/level/Level.getEntitiesOfClass:(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
      63: checkcast     #230                // class net/minecraft/world/entity/item/ItemEntity
      72: invokestatic  #292                // Method moze_intel/projecte/utils/WorldHelper.gravitateEntityTowards:(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V
      86: invokevirtual #347                // Method net/minecraft/world/entity/item/ItemEntity.distanceToSqr:(Lnet/minecraft/world/phys/Vec3;)D
     178: invokevirtual #274                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
     197: invokevirtual #388                // Method net/minecraft/world/entity/item/ItemEntity.discard:()V
     207: invokevirtual #392                // Method net/minecraft/world/entity/item/ItemEntity.setItem:(Lnet/minecraft/world/item/ItemStack;)V
      63: ldc           #230                // class net/minecraft/world/entity/item/ItemEntity
      72: invokevirtual #339                // Method net/minecraft/world/level/Level.getEntitiesOfClass:(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
      99: checkcast     #230                // class net/minecraft/world/entity/item/ItemEntity
     108: invokestatic  #292                // Method moze_intel/projecte/utils/WorldHelper.gravitateEntityTowards:(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V
     122: invokevirtual #347                // Method net/minecraft/world/entity/item/ItemEntity.distanceToSqr:(Lnet/minecraft/world/phys/Vec3;)D
     136: invokevirtual #274                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
     157: invokevirtual #392                // Method net/minecraft/world/entity/item/ItemEntity.setItem:(Lnet/minecraft/world/item/ItemStack;)V
     165: invokevirtual #388                // Method net/minecraft/world/entity/item/ItemEntity.discard:()V
      24: ldc           #230                // class net/minecraft/world/entity/item/ItemEntity
      36: invokevirtual #246                // Method net/minecraft/world/level/Level.getEntitiesOfClass:(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;
      63: checkcast     #230                // class net/minecraft/world/entity/item/ItemEntity
      74: invokestatic  #292                // Method moze_intel/projecte/utils/WorldHelper.gravitateEntityTowards:(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V
  private static boolean lambda$updateInAlchChest$1(net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;)Z
       1: invokevirtual #466                // Method net/minecraft/world/entity/item/ItemEntity.isSpectator:()Z
       8: invokevirtual #469                // Method net/minecraft/world/entity/item/ItemEntity.isAlive:()Z
  private static boolean lambda$updateInPedestal$0(net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;)Z
       1: invokevirtual #466                // Method net/minecraft/world/entity/item/ItemEntity.isSpectator:()Z
       8: invokevirtual #469                // Method net/minecraft/world/entity/item/ItemEntity.isAlive:()Z
```

