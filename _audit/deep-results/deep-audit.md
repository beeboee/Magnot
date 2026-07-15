# Deep registered-mixin audit

This report checks only mixins registered by each branch and compares their target classes and method names with the newest resolved jar for that exact Minecraft/loader pair.

## Official Create Maven metadata

| Minecraft | Latest Maven artifact | Recent versions |
|---|---|---|
| 1.21.1 | `6.0.11-295` | `6.0.11-282`<br>`6.0.11-283`<br>`6.0.11-284`<br>`6.0.11-285`<br>`6.0.11-286`<br>`6.0.11-287`<br>`6.0.11-288`<br>`6.0.11-289`<br>`6.0.11-290`<br>`6.0.11-291`<br>`6.0.11-292`<br>`6.0.11-295` |
| 1.20.1 | `6.0.8-291` | `6.0.7-280`<br>`6.0.7-281`<br>`6.0.7-282`<br>`6.0.7-283`<br>`6.0.7-284`<br>`6.0.7-285`<br>`6.0.7-286`<br>`6.0.7-287`<br>`6.0.8-288`<br>`6.0.8-289`<br>`6.0.8-290`<br>`6.0.8-291` |
| 1.19.2 | `unresolved` | <HTTPError 404: 'Not Found'> |
| 1.18.2 | `unresolved` | <HTTPError 404: 'Not Found'> |
| 1.16.5 | `unresolved` | <HTTPError 404: 'Not Found'> |

## main-1.21.1-neoforge

| Integration | Resolved version | Target | Class | Required method strings |
|---|---|---|---|---|
| ae2wtlib | `19.5.0` | `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler` | exact | handleMagnet: yes, playerTouch: yes |
| artifacts | `13.2.1` | `artifacts.effect.MagnetismMobEffect` | exact | applyEffectTick: yes, getEntitiesOfClass: yes |
| draconic-evolution | `3.1.4.632` | `com.brandon3055.draconicevolution.items.tools.Magnet` | exact | updateMagnet: yes, getEntitiesOfClass: yes |
| enderio | `v8.2.11-beta` | `com.enderio.enderio.content.tools.ElectromagnetItem` | exact | onTickWhenActive: yes, getEntities: yes |
| enderio | `v8.2.11-beta` | `com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity` | exact | getEntities: yes, getEntitiesOfClass: yes |
| industrial-foregoing | `1.21-3.6.39` | `com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack` | exact | inventoryTick: yes, getEntitiesOfClass: yes |
| item-collectors | `1.1.10-neoforge-mc1.21` | `com.supermartijn642.itemcollectors.CollectorBlockEntity` | exact | tick: NO, getEntitiesOfClass: yes |
| mekanism | `10.7.19.85` | `mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit` | exact | pullItem: yes |
| mob-grinding-utils | `unresolved` | `mob_grinding_utils.tile.TileEntityAbsorptionHopper` | unresolved | — |
| modular-routers | `13.2.6` | `me.desht.modularrouters.logic.compiled.CompiledVacuumModule` | exact | handleItemMode: yes, getEntitiesOfClass: yes |
| projecte | `1.21.1-1.0.6` | `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand` | candidate: `cool.furry.mc.neoforge.projectexpansion.events.RenderingEvent`<br>`cool.furry.mc.neoforge.projectexpansion.rendering.package-info`<br>`cool.furry.mc.neoforge.projectexpansion.rendering.ChestRenderer`<br>`cool.furry.mc.neoforge.projectexpansion.item.ItemKnowledgeSharingBook` | inventoryTick: NO, gravitateEntityTowards: NO |
| reliquary | `1.21.1-2.0.77.1537` | `reliquary.item.FortuneCoinItem` | exact | scanForEntitiesInRange: yes, teleportEntityToPlayer: yes, pickupItems: yes |
| simple-magnets | `1.1.12c-neoforge-mc1.21` | `com.supermartijn642.simplemagnets.MagnetItem` | exact | inventoryTick: NO, inventoryUpdate: yes, getEntities: yes |
| sophisticated-core | `1.21.1-1.4.73.2151` | `net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper` | exact | pickupItems: yes, tryToInsertItem: yes |

### main-1.21.1-neoforge: item-collectors

#### `com.supermartijn642.itemcollectors.CollectorBlockEntity`

```text
public class com.supermartijn642.itemcollectors.CollectorBlockEntity extends com.supermartijn642.core.block.BaseBlockEntity implements com.supermartijn642.core.block.TickableBlockEntity {
      38: ldc           #120                // class net/minecraft/world/entity/item/ItemEntity
      47: invokevirtual #126                // Method net/minecraft/world/level/Level.getEntitiesOfClass:(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
      76: checkcast     #120                // class net/minecraft/world/entity/item/ItemEntity
      83: invokevirtual #143                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
     144: invokevirtual #161                // Method net/minecraft/world/entity/item/ItemEntity.setItem:(Lnet/minecraft/world/item/ItemStack;)V
     152: invokevirtual #171                // Method net/minecraft/world/entity/item/ItemEntity.remove:(Lnet/minecraft/world/entity/Entity$RemovalReason;)V
     168: invokevirtual #161                // Method net/minecraft/world/entity/item/ItemEntity.setItem:(Lnet/minecraft/world/item/ItemStack;)V
  private boolean lambda$update$0(net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;)Z
       1: invokevirtual #306                // Method net/minecraft/world/entity/item/ItemEntity.isAlive:()Z
       8: invokevirtual #309                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
      21: invokevirtual #309                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
      36: invokevirtual #143                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
```


### main-1.21.1-neoforge: mob-grinding-utils


### main-1.21.1-neoforge: projecte

Candidate classes: `cool.furry.mc.neoforge.projectexpansion.events.RenderingEvent`, `cool.furry.mc.neoforge.projectexpansion.rendering.package-info`, `cool.furry.mc.neoforge.projectexpansion.rendering.ChestRenderer`, `cool.furry.mc.neoforge.projectexpansion.item.ItemKnowledgeSharingBook`, `cool.furry.mc.neoforge.projectexpansion.util.BasicDataComponentTypes$StringValue`

#### `cool.furry.mc.neoforge.projectexpansion.events.RenderingEvent`

```text
(no relevant signatures)
```

#### `cool.furry.mc.neoforge.projectexpansion.rendering.package-info`

```text
(no relevant signatures)
```

#### `cool.furry.mc.neoforge.projectexpansion.rendering.ChestRenderer`

```text
(no relevant signatures)
```

#### `cool.furry.mc.neoforge.projectexpansion.item.ItemKnowledgeSharingBook`

```text
(no relevant signatures)
```

#### `cool.furry.mc.neoforge.projectexpansion.util.BasicDataComponentTypes$StringValue`

```text
(no relevant signatures)
```


### main-1.21.1-neoforge: simple-magnets

#### `com.supermartijn642.simplemagnets.MagnetItem`

```text
  public void inventoryUpdate(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);
     108: invokevirtual #136                // Method net/minecraft/world/level/Level.getEntities:(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
     245: invokevirtual #167                // Method net/minecraft/world/level/Level.getEntitiesOfClass:(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;
  private static void playerTouch(net.minecraft.world.entity.item.ItemEntity, net.minecraft.world.entity.player.Player);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)V
       1: invokevirtual #174                // Method net/minecraft/world/entity/item/ItemEntity.level:()Lnet/minecraft/world/level/Level;
      11: invokevirtual #177                // Method net/minecraft/world/entity/item/ItemEntity.hasPickUpDelay:()Z
      19: invokevirtual #180                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
      36: invokestatic  #187                // Method net/neoforged/neoforge/event/EventHooks.fireItemPickupPre:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)Lnet/neoforged/neoforge/event/entity/player/ItemEntityPickupEvent$Pre;
      39: invokevirtual #193                // Method net/neoforged/neoforge/event/entity/player/ItemEntityPickupEvent$Pre.canPickup:()Lnet/neoforged/neoforge/common/util/TriState;
      68: invokevirtual #177                // Method net/minecraft/world/entity/item/ItemEntity.hasPickUpDelay:()Z
      75: invokevirtual #210                // Method net/minecraft/world/entity/item/ItemEntity.getTarget:()Ljava/util/UUID;
      82: getfield      #214                // Field net/minecraft/world/entity/item/ItemEntity.lifespan:I
      86: invokevirtual #218                // Method net/minecraft/world/entity/item/ItemEntity.getAge:()I
      97: invokevirtual #210                // Method net/minecraft/world/entity/item/ItemEntity.getTarget:()Ljava/util/UUID;
     140: invokestatic  #242                // Method net/neoforged/neoforge/event/EventHooks.fireItemPickupPost:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V
     158: invokevirtual #253                // Method net/minecraft/world/entity/item/ItemEntity.discard:()V
     182: invokevirtual #272                // Method net/minecraft/world/entity/player/Player.onItemPickup:(Lnet/minecraft/world/entity/item/ItemEntity;)V
      17: instanceof    #175                // class net/minecraft/world/entity/item/ItemEntity
      27: checkcast     #175                // class net/minecraft/world/entity/item/ItemEntity
      30: getfield      #294                // Field net/minecraft/world/entity/item/ItemEntity.thrower:Ljava/util/UUID;
      51: checkcast     #175                // class net/minecraft/world/entity/item/ItemEntity
      54: invokespecial #300                // Method com/supermartijn642/simplemagnets/packets/magnet/PacketItemInfo."<init>":(Lnet/minecraft/world/entity/item/ItemEntity;)V
  private static void lambda$inventoryUpdate$3(net.minecraft.world.entity.player.Player, net.minecraft.world.entity.ExperienceOrb);
      12: invokevirtual #308                // Method net/minecraft/world/entity/ExperienceOrb.playerTouch:(Lnet/minecraft/world/entity/player/Player;)V
  private static void lambda$inventoryUpdate$2(net.minecraft.world.entity.Entity, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/item/ItemEntity;)V
       5: invokestatic  #312                // Method playerTouch:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)V
  private static void lambda$inventoryUpdate$1(net.minecraft.world.entity.Entity, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/item/ItemEntity;)V
      13: invokevirtual #325                // Method net/minecraft/world/entity/item/ItemEntity.setPos:(DDD)V
  private boolean lambda$inventoryUpdate$0(net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, net.minecraft.world.item.ItemStack, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/item/ItemEntity;)Z
       2: invokevirtual #329                // Method net/minecraft/world/entity/item/ItemEntity.isAlive:()Z
      17: getfield      #332                // Field net/minecraft/world/entity/item/ItemEntity.tickCount:I
      26: getfield      #294                // Field net/minecraft/world/entity/item/ItemEntity.thrower:Ljava/util/UUID;
      34: getfield      #294                // Field net/minecraft/world/entity/item/ItemEntity.thrower:Ljava/util/UUID;
      49: invokevirtual #177                // Method net/minecraft/world/entity/item/ItemEntity.hasPickUpDelay:()Z
      57: invokevirtual #180                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
      68: invokevirtual #336                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
      84: invokevirtual #180                // Method net/minecraft/world/entity/item/ItemEntity.getItem:()Lnet/minecraft/world/item/ItemStack;
```


## forge-1.20.1

| Integration | Resolved version | Target | Class | Required method strings |
|---|---|---|---|---|
| simple-magnets | `1.1.12-forge-mc1.20.1` | `com.supermartijn642.simplemagnets.MagnetItem` | exact | inventoryTick: NO, inventoryUpdate: yes, getEntities: NO |
| item-collectors | `1.1.12-forge-mc1.20.2` | `com.supermartijn642.itemcollectors.CollectorBlockEntity` | exact | tick: NO, getEntitiesOfClass: NO |
| sophisticated-core | `1.20.1-1.3.67.2148` | `net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper` | exact | pickupItems: yes, tryToInsertItem: yes |
| ae2wtlib | `15.3.3-forge` | `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler` | exact | handleMagnet: yes, playerTouch: NO |
| artifacts | `9.5.19` | `artifacts.effect.MagnetismMobEffect` | candidate: `artifacts.item.wearable.MobEffectItem` | applyEffectTick: NO, getEntitiesOfClass: NO |
| mekanism | `10.4.16.80` | `mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit` | exact | pullItem: yes |
| draconic-evolution | `3.1.2.621` | `com.brandon3055.draconicevolution.items.tools.Magnet` | exact | updateMagnet: yes, getEntitiesOfClass: NO |
| reliquary | `1.20.1-2.0.62.1532` | `reliquary.item.FortuneCoinItem` | exact | scanForEntitiesInRange: yes, teleportEntityToPlayer: yes, pickupItems: yes |
| mob-grinding-utils | `unresolved` | `mob_grinding_utils.tile.TileEntityAbsorptionHopper` | unresolved | — |
| modular-routers | `12.1.1` | `me.desht.modularrouters.logic.compiled.CompiledVacuumModule` | exact | handleItemMode: yes, getEntitiesOfClass: NO |
| enderio | `6.2.18-beta` | `com.enderio.enderio.content.tools.ElectromagnetItem` | candidate: `com.enderio.base.common.item.tool.ElectromagnetItem`<br>`com.enderio.base.common.init.EIOItems`<br>`com.enderio.base.common.tag.EIOTags$Items`<br>`com.enderio.base.common.item.tool.SoulVialItem` | onTickWhenActive: yes, getEntities: NO |
| enderio | `6.2.18-beta` | `com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity` | candidate: `com.enderio.machines.common.blockentity.base.VacuumMachineBlockEntity`<br>`com.enderio.machines.common.blockentity.VacuumChestBlockEntity`<br>`com.enderio.machines.common.blockentity.XPVacuumBlockEntity`<br>`com.enderio.machines.common.blockentity.XPVacuumBlockEntity$1` | getEntities: yes, getEntitiesOfClass: NO |

### forge-1.20.1: simple-magnets

#### `com.supermartijn642.simplemagnets.MagnetItem`

```text
  public void inventoryUpdate(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);
  private static void playerTouch(net.minecraft.world.entity.item.ItemEntity, net.minecraft.world.entity.player.Player);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)V
       1: invokevirtual #264                // Method net/minecraft/world/entity/item/ItemEntity.m_9236_:()Lnet/minecraft/world/level/Level;
      11: invokevirtual #267                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      19: invokevirtual #271                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      36: invokestatic  #281                // Method net/minecraftforge/event/ForgeEventFactory.onItemPickup:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)I
      54: invokevirtual #267                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      61: invokevirtual #290                // Method net/minecraft/world/entity/item/ItemEntity.m_19749_:()Lnet/minecraft/world/entity/Entity;
      68: getfield      #293                // Field net/minecraft/world/entity/item/ItemEntity.lifespan:I
      72: invokevirtual #296                // Method net/minecraft/world/entity/item/ItemEntity.m_32059_:()I
      83: invokevirtual #290                // Method net/minecraft/world/entity/item/ItemEntity.m_19749_:()Lnet/minecraft/world/entity/Entity;
     137: invokestatic  #321                // Method net/minecraftforge/event/ForgeEventFactory.firePlayerItemPickupEvent:(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;)V
     155: invokevirtual #331                // Method net/minecraft/world/entity/item/ItemEntity.m_146870_:()V
     179: invokevirtual #350                // Method net/minecraft/world/entity/player/Player.m_21053_:(Lnet/minecraft/world/entity/item/ItemEntity;)V
      17: instanceof    #263                // class net/minecraft/world/entity/item/ItemEntity
      27: checkcast     #263                // class net/minecraft/world/entity/item/ItemEntity
      30: getfield      #392                // Field net/minecraft/world/entity/item/ItemEntity.f_31988_:Ljava/util/UUID;
      51: checkcast     #263                // class net/minecraft/world/entity/item/ItemEntity
      54: invokespecial #396                // Method com/supermartijn642/simplemagnets/packets/magnet/PacketItemInfo."<init>":(Lnet/minecraft/world/entity/item/ItemEntity;)V
  private static void lambda$inventoryUpdate$3(net.minecraft.world.entity.player.Player, net.minecraft.world.entity.ExperienceOrb);
  private static void lambda$inventoryUpdate$2(net.minecraft.world.entity.Entity, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/item/ItemEntity;)V
       5: invokestatic  #411                // Method playerTouch:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)V
  private static void lambda$inventoryUpdate$1(net.minecraft.world.entity.Entity, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/item/ItemEntity;)V
      13: invokevirtual #425                // Method net/minecraft/world/entity/item/ItemEntity.m_6034_:(DDD)V
  private boolean lambda$inventoryUpdate$0(net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, net.minecraft.nbt.CompoundTag, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/entity/item/ItemEntity;)Z
       2: invokevirtual #428                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
      17: getfield      #431                // Field net/minecraft/world/entity/item/ItemEntity.f_19797_:I
      26: getfield      #392                // Field net/minecraft/world/entity/item/ItemEntity.f_31988_:Ljava/util/UUID;
      34: getfield      #392                // Field net/minecraft/world/entity/item/ItemEntity.f_31988_:Ljava/util/UUID;
      49: invokevirtual #267                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      57: invokevirtual #271                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      68: invokevirtual #438                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
      84: invokevirtual #271                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
```


### forge-1.20.1: item-collectors

#### `com.supermartijn642.itemcollectors.CollectorBlockEntity`

```text
public class com.supermartijn642.itemcollectors.CollectorBlockEntity extends com.supermartijn642.core.block.BaseBlockEntity implements com.supermartijn642.core.block.TickableBlockEntity {
      19: ldc_w         #357                // class net/minecraft/world/entity/item/ItemEntity
     107: checkcast     #357                // class net/minecraft/world/entity/item/ItemEntity
     114: invokevirtual #373                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
     122: invokevirtual #376                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
     136: invokevirtual #376                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
     153: invokevirtual #384                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     334: invokevirtual #411                // Method net/minecraft/world/entity/item/ItemEntity.m_32045_:(Lnet/minecraft/world/item/ItemStack;)V
     342: invokevirtual #419                // Method net/minecraft/world/entity/item/ItemEntity.m_142687_:(Lnet/minecraft/world/entity/Entity$RemovalReason;)V
     358: invokevirtual #411                // Method net/minecraft/world/entity/item/ItemEntity.m_32045_:(Lnet/minecraft/world/item/ItemStack;)V
```


### forge-1.20.1: ae2wtlib

#### `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler`

```text
      38: invokestatic  #45                 // Method handleMagnet:(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V
  private static void handleMagnet(net.minecraft.world.entity.player.Player, net.minecraft.world.item.ItemStack);
      20: invokestatic  #195                // Method handleMagnet:(Lnet/minecraft/world/entity/player/Player;)V
  private static void handleMagnet(net.minecraft.world.entity.player.Player);
      33: ldc           #208                // class net/minecraft/world/entity/item/ItemEntity
      80: checkcast     #208                // class net/minecraft/world/entity/item/ItemEntity
      87: invokevirtual #258                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     123: invokestatic  #278                // Method de/mari_023/ae2wtlib/Platform.preventRemoteMovement:(Lnet/minecraft/world/entity/item/ItemEntity;)Z
     132: invokevirtual #281                // Method net/minecraft/world/entity/item/ItemEntity.m_6123_:(Lnet/minecraft/world/entity/player/Player;)V
```


### forge-1.20.1: artifacts

Candidate classes: `artifacts.item.wearable.MobEffectItem`

#### `artifacts.item.wearable.MobEffectItem`

```text
(no relevant signatures)
```


### forge-1.20.1: draconic-evolution

#### `com.brandon3055.draconicevolution.items.tools.Magnet`

```text
       3: invokevirtual #45                 // Method updateMagnet:(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)V
  private void updateMagnet(net.minecraft.world.item.ItemStack, net.minecraft.world.entity.Entity);
      37: ldc           #69                 // class net/minecraft/world/entity/item/ItemEntity
      97: ldc           #69                 // class net/minecraft/world/entity/item/ItemEntity
     176: checkcast     #69                 // class net/minecraft/world/entity/item/ItemEntity
     183: invokevirtual #114                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
     194: invokevirtual #118                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
     219: invokevirtual #130                // Method net/minecraft/world/entity/item/ItemEntity.m_19749_:()Lnet/minecraft/world/entity/Entity;
     227: invokevirtual #130                // Method net/minecraft/world/entity/item/ItemEntity.m_19749_:()Lnet/minecraft/world/entity/Entity;
     242: getfield      #141                // Field net/minecraft/world/entity/item/ItemEntity.f_31986_:I
     278: invokevirtual #151                // Method net/minecraft/world/entity/item/ItemEntity.m_20183_:()Lnet/minecraft/core/BlockPos;
     397: getfield      #141                // Field net/minecraft/world/entity/item/ItemEntity.f_31986_:I
     406: putfield      #141                // Field net/minecraft/world/entity/item/ItemEntity.f_31986_:I
     414: invokevirtual #196                // Method net/minecraft/world/entity/item/ItemEntity.m_20334_:(DDD)V
     420: putfield      #200                // Field net/minecraft/world/entity/item/ItemEntity.f_19789_:F
     477: invokevirtual #218                // Method net/minecraft/world/entity/item/ItemEntity.m_6034_:(DDD)V
```


### forge-1.20.1: mob-grinding-utils


### forge-1.20.1: modular-routers

#### `me.desht.modularrouters.logic.compiled.CompiledVacuumModule`

```text
      15: invokevirtual #171                // Method handleItemMode:(Lme/desht/modularrouters/block/tile/ModularRouterBlockEntity;)Z
  private boolean handleItemMode(me.desht.modularrouters.block.tile.ModularRouterBlockEntity);
      41: ldc_w         #283                // class net/minecraft/world/entity/item/ItemEntity
     118: checkcast     #283                // class net/minecraft/world/entity/item/ItemEntity
     125: invokevirtual #317                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
     140: invokevirtual #320                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
     151: invokevirtual #324                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     303: invokevirtual #377                // Method net/minecraft/world/entity/item/ItemEntity.m_142687_:(Lnet/minecraft/world/entity/Entity$RemovalReason;)V
     361: invokevirtual #415                // Method net/minecraft/world/entity/item/ItemEntity.m_20185_:()D
     366: invokevirtual #418                // Method net/minecraft/world/entity/item/ItemEntity.m_20186_:()D
     375: invokevirtual #423                // Method net/minecraft/world/entity/item/ItemEntity.m_20189_:()D
```


### forge-1.20.1: enderio

Candidate classes: `com.enderio.base.common.item.tool.ElectromagnetItem`, `com.enderio.base.common.init.EIOItems`, `com.enderio.base.common.tag.EIOTags$Items`, `com.enderio.base.common.item.tool.SoulVialItem`, `com.enderio.base.data.recipe.ItemRecipeProvider`, `com.enderio.base.common.config.common.ItemsConfig`, `com.enderio.base.common.item.tool.PoweredToggledItem`, `com.enderio.base.common.item.tool.LevitationStaffItem`

#### `com.enderio.base.common.item.tool.ElectromagnetItem`

```text
  private boolean isBlacklisted(net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;)Z
       1: invokevirtual #92                 // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
       1: instanceof    #88                 // class net/minecraft/world/entity/item/ItemEntity
       8: checkcast     #88                 // class net/minecraft/world/entity/item/ItemEntity
      14: invokevirtual #108                // Method isBlacklisted:(Lnet/minecraft/world/entity/item/ItemEntity;)Z
  protected void onTickWhenActive(net.minecraft.world.entity.player.Player, net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);
```

#### `com.enderio.base.common.init.EIOItems`

```text
(no relevant signatures)
```

#### `com.enderio.base.common.tag.EIOTags$Items`

```text
(no relevant signatures)
```

#### `com.enderio.base.common.item.tool.SoulVialItem`

```text
      91: invokevirtual #203                // Method net/minecraft/world/entity/player/Player.m_36176_:(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;
```

#### `com.enderio.base.data.recipe.ItemRecipeProvider`

```text
(no relevant signatures)
```

#### `com.enderio.base.common.config.common.ItemsConfig`

```text
      20: ldc           #48                 // String The chance of enderios teleporting the player
      41: ldc           #69                 // String The range of an enderio teleport
```


### forge-1.20.1: enderio

Candidate classes: `com.enderio.machines.common.blockentity.base.VacuumMachineBlockEntity`, `com.enderio.machines.common.blockentity.VacuumChestBlockEntity`, `com.enderio.machines.common.blockentity.XPVacuumBlockEntity`, `com.enderio.machines.common.blockentity.XPVacuumBlockEntity$1`, `com.enderio.base.common.blockentity.IMachineInstall`, `com.enderio.machines.common.blockentity.MachineState`, `com.enderio.machines.common.blockentity.package-info`, `com.enderio.machines.common.blockentity.AlloySmelterMode`

#### `com.enderio.machines.common.blockentity.base.VacuumMachineBlockEntity`

```text
  protected static final java.util.function.Predicate<net.minecraft.world.entity.item.ItemEntity> ITEM_ENTITY_FILTER_TRUE;
      26: invokevirtual #190                // Method getEntities:(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILjava/util/function/Predicate;)V
  private void getEntities(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int, java.util.function.Predicate<T>);
      29: invokevirtual #190                // Method getEntities:(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ILjava/util/function/Predicate;)V
  private static boolean lambda$static$0(net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;)Z
```

#### `com.enderio.machines.common.blockentity.VacuumChestBlockEntity`

```text
public class com.enderio.machines.common.blockentity.VacuumChestBlockEntity extends com.enderio.machines.common.blockentity.base.VacuumMachineBlockEntity<net.minecraft.world.entity.item.ItemEntity> {
       4: ldc           #31                 // class net/minecraft/world/entity/item/ItemEntity
  public void handleEntity(net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;)V
      19: invokevirtual #160                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      38: invokevirtual #174                // Method net/minecraft/world/entity/item/ItemEntity.m_146870_:()V
      43: invokevirtual #160                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
  public java.util.function.Predicate<net.minecraft.world.entity.item.ItemEntity> getFilter();
       2: checkcast     #31                 // class net/minecraft/world/entity/item/ItemEntity
       5: invokevirtual #249                // Method handleEntity:(Lnet/minecraft/world/entity/item/ItemEntity;)V
  private static boolean lambda$getFilter$2(com.enderio.api.filter.ItemStackFilter, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lcom/enderio/api/filter/ItemStackFilter;Lnet/minecraft/world/entity/item/ItemEntity;)Z
       2: invokevirtual #160                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
```

#### `com.enderio.machines.common.blockentity.XPVacuumBlockEntity`

```text
(no relevant signatures)
```

#### `com.enderio.machines.common.blockentity.XPVacuumBlockEntity$1`

```text
(no relevant signatures)
```

#### `com.enderio.base.common.blockentity.IMachineInstall`

```text
(no relevant signatures)
```

#### `com.enderio.machines.common.blockentity.MachineState`

```text
(no relevant signatures)
```


## fabric-1.20.1

| Integration | Resolved version | Target | Class | Required method strings |
|---|---|---|---|---|
| simple-magnets | `1.1.12-fabric-mc1.20.1` | `com.supermartijn642.simplemagnets.MagnetItem` | exact | inventoryTick: NO, inventoryUpdate: yes, getEntities: NO |
| item-collectors | `1.1.12-fabric-mc1.20.2` | `com.supermartijn642.itemcollectors.CollectorBlockEntity` | exact | tick: NO, getEntitiesOfClass: NO |
| ae2wtlib | `15.2.1-fabric` | `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler` | exact | handleMagnet: yes, playerTouch: NO |
| artifacts | `9.5.17` | `artifacts.effect.MagnetismMobEffect` | candidate: `artifacts.item.wearable.MobEffectItem` | applyEffectTick: NO, getEntitiesOfClass: NO |

### fabric-1.20.1: simple-magnets

#### `com.supermartijn642.simplemagnets.MagnetItem`

```text
  public void inventoryUpdate(net.minecraft.class_1799, net.minecraft.class_1937, net.minecraft.class_1297, int, boolean);
  private static void playerTouch(net.minecraft.class_1542, net.minecraft.class_1657);
  private static void lambda$inventoryUpdate$3(net.minecraft.class_1657, net.minecraft.class_1303);
  private static void lambda$inventoryUpdate$2(net.minecraft.class_1297, net.minecraft.class_1542);
       5: invokestatic  #364                // Method playerTouch:(Lnet/minecraft/class_1542;Lnet/minecraft/class_1657;)V
  private static void lambda$inventoryUpdate$1(net.minecraft.class_1297, net.minecraft.class_1542);
  private boolean lambda$inventoryUpdate$0(net.minecraft.class_1937, net.minecraft.class_1297, net.minecraft.class_2487, net.minecraft.class_1542);
      68: checkcast     #390                // class com/supermartijn642/simplemagnets/extensions/SimpleMagnetsItemEntity
      71: invokeinterface #393,  1          // InterfaceMethod com/supermartijn642/simplemagnets/extensions/SimpleMagnetsItemEntity.simplemagnetsCanBePickedUp:()Z
```


### fabric-1.20.1: item-collectors

#### `com.supermartijn642.itemcollectors.CollectorBlockEntity`

```text
public class com.supermartijn642.itemcollectors.CollectorBlockEntity extends com.supermartijn642.core.block.BaseBlockEntity implements com.supermartijn642.core.block.TickableBlockEntity {
```


### fabric-1.20.1: ae2wtlib

#### `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler`

```text
      38: invokestatic  #47                 // Method handleMagnet:(Lnet/minecraft/class_1657;Lnet/minecraft/class_1799;)V
  private static void handleMagnet(net.minecraft.class_1657, net.minecraft.class_1799);
      20: invokestatic  #195                // Method handleMagnet:(Lnet/minecraft/class_1657;)V
  private static void handleMagnet(net.minecraft.class_1657);
```


### fabric-1.20.1: artifacts

Candidate classes: `artifacts.item.wearable.MobEffectItem`

#### `artifacts.item.wearable.MobEffectItem`

```text
(no relevant signatures)
```


## forge-1.19.2

| Integration | Resolved version | Target | Class | Required method strings |
|---|---|---|---|---|
| simple-magnets | `1.1.12-forge-mc1.19.2` | `com.supermartijn642.simplemagnets.MagnetItem` | exact | inventoryTick: NO, inventoryUpdate: yes, getEntities: NO |
| item-collectors | `1.1.12-forge-mc1.19.2` | `com.supermartijn642.itemcollectors.CollectorBlockEntity` | exact | tick: NO, getEntitiesOfClass: NO |
| ae2wtlib | `12.9.7-forge` | `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler` | exact | handleMagnet: yes, playerTouch: NO |
| artifacts | `5.0.6+forge` | `artifacts.effect.MagnetismMobEffect` | candidate: `artifacts.data.MobEffectTags` | applyEffectTick: NO, getEntitiesOfClass: NO |
| mekanism | `10.3.9.13` | `mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit` | exact | pullItem: yes |
| reliquary | `1.19.2-2.0.40.1198` | `reliquary.item.FortuneCoinItem` | candidate: `reliquary.items.FortuneCoinItem`<br>`reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`<br>`reliquary.items.FortuneCoinToggler`<br>`reliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings` | scanForEntitiesInRange: yes, teleportEntityToPlayer: yes, pickupItems: yes |
| mob-grinding-utils | `unresolved` | `mob_grinding_utils.tile.TileEntityAbsorptionHopper` | unresolved | — |
| modular-routers | `1.19.2-10.2.1` | `me.desht.modularrouters.logic.compiled.CompiledVacuumModule` | exact | handleItemMode: yes, getEntitiesOfClass: NO |
| sophisticated-core | `1.19.2-0.6.4.730` | `net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper` | exact | pickupItems: yes, tryToInsertItem: yes |

### forge-1.19.2: simple-magnets

#### `com.supermartijn642.simplemagnets.MagnetItem`

```text
  public void inventoryUpdate(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);
  private static void playerTouch(net.minecraft.world.entity.item.ItemEntity, net.minecraft.world.entity.player.Player);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)V
       1: getfield      #263                // Field net/minecraft/world/entity/item/ItemEntity.f_19853_:Lnet/minecraft/world/level/Level;
      11: invokevirtual #266                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      19: invokevirtual #270                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      36: invokestatic  #280                // Method net/minecraftforge/event/ForgeEventFactory.onItemPickup:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)I
      54: invokevirtual #266                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      61: invokevirtual #289                // Method net/minecraft/world/entity/item/ItemEntity.m_32056_:()Ljava/util/UUID;
      68: getfield      #292                // Field net/minecraft/world/entity/item/ItemEntity.lifespan:I
      72: invokevirtual #295                // Method net/minecraft/world/entity/item/ItemEntity.m_32059_:()I
      83: invokevirtual #289                // Method net/minecraft/world/entity/item/ItemEntity.m_32056_:()Ljava/util/UUID;
     137: invokestatic  #321                // Method net/minecraftforge/event/ForgeEventFactory.firePlayerItemPickupEvent:(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;)V
     155: invokevirtual #331                // Method net/minecraft/world/entity/item/ItemEntity.m_146870_:()V
     179: invokevirtual #350                // Method net/minecraft/world/entity/player/Player.m_21053_:(Lnet/minecraft/world/entity/item/ItemEntity;)V
      17: instanceof    #262                // class net/minecraft/world/entity/item/ItemEntity
      27: checkcast     #262                // class net/minecraft/world/entity/item/ItemEntity
      30: invokevirtual #392                // Method net/minecraft/world/entity/item/ItemEntity.m_32057_:()Ljava/util/UUID;
      51: checkcast     #262                // class net/minecraft/world/entity/item/ItemEntity
      54: invokespecial #396                // Method com/supermartijn642/simplemagnets/packets/magnet/PacketItemInfo."<init>":(Lnet/minecraft/world/entity/item/ItemEntity;)V
  private static void lambda$inventoryUpdate$3(net.minecraft.world.entity.player.Player, net.minecraft.world.entity.ExperienceOrb);
  private static void lambda$inventoryUpdate$2(net.minecraft.world.entity.Entity, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/item/ItemEntity;)V
       5: invokestatic  #411                // Method playerTouch:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)V
  private static void lambda$inventoryUpdate$1(net.minecraft.world.entity.Entity, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/item/ItemEntity;)V
      13: invokevirtual #425                // Method net/minecraft/world/entity/item/ItemEntity.m_6034_:(DDD)V
  private boolean lambda$inventoryUpdate$0(net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, net.minecraft.nbt.CompoundTag, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/entity/item/ItemEntity;)Z
       2: invokevirtual #428                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
      17: getfield      #431                // Field net/minecraft/world/entity/item/ItemEntity.f_19797_:I
      26: invokevirtual #392                // Method net/minecraft/world/entity/item/ItemEntity.m_32057_:()Ljava/util/UUID;
      34: invokevirtual #392                // Method net/minecraft/world/entity/item/ItemEntity.m_32057_:()Ljava/util/UUID;
      49: invokevirtual #266                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      57: invokevirtual #270                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      68: invokevirtual #435                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
      84: invokevirtual #270                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
```


### forge-1.19.2: item-collectors

#### `com.supermartijn642.itemcollectors.CollectorBlockEntity`

```text
public class com.supermartijn642.itemcollectors.CollectorBlockEntity extends com.supermartijn642.core.block.BaseBlockEntity implements com.supermartijn642.core.block.TickableBlockEntity {
      19: ldc_w         #357                // class net/minecraft/world/entity/item/ItemEntity
     107: checkcast     #357                // class net/minecraft/world/entity/item/ItemEntity
     114: invokevirtual #373                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
     122: invokevirtual #376                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
     136: invokevirtual #376                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
     153: invokevirtual #384                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     334: invokevirtual #411                // Method net/minecraft/world/entity/item/ItemEntity.m_32045_:(Lnet/minecraft/world/item/ItemStack;)V
     342: invokevirtual #419                // Method net/minecraft/world/entity/item/ItemEntity.m_142687_:(Lnet/minecraft/world/entity/Entity$RemovalReason;)V
     358: invokevirtual #411                // Method net/minecraft/world/entity/item/ItemEntity.m_32045_:(Lnet/minecraft/world/item/ItemStack;)V
```


### forge-1.19.2: ae2wtlib

#### `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler`

```text
      38: invokestatic  #45                 // Method handleMagnet:(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V
  private static void handleMagnet(net.minecraft.world.entity.player.Player, net.minecraft.world.item.ItemStack);
      20: invokestatic  #201                // Method handleMagnet:(Lnet/minecraft/world/entity/player/Player;)V
  private static void handleMagnet(net.minecraft.world.entity.player.Player);
      33: ldc           #214                // class net/minecraft/world/entity/item/ItemEntity
      80: checkcast     #214                // class net/minecraft/world/entity/item/ItemEntity
      87: invokevirtual #264                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     123: invokestatic  #284                // Method de/mari_023/ae2wtlib/Platform.preventRemoteMovement:(Lnet/minecraft/world/entity/item/ItemEntity;)Z
     132: invokevirtual #287                // Method net/minecraft/world/entity/item/ItemEntity.m_6123_:(Lnet/minecraft/world/entity/player/Player;)V
```


### forge-1.19.2: artifacts

Candidate classes: `artifacts.data.MobEffectTags`

#### `artifacts.data.MobEffectTags`

```text
(no relevant signatures)
```


### forge-1.19.2: reliquary

Candidate classes: `reliquary.items.FortuneCoinItem`, `reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`, `reliquary.items.FortuneCoinToggler`, `reliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings`, `reliquary.compat.curios.CuriosFortuneCoinToggler`, `reliquary.network.PacketFortuneCoinTogglePressed`, `reliquary.network.PacketFortuneCoinTogglePressed$1`, `reliquary.network.PacketFortuneCoinTogglePressed$InventoryType`

#### `reliquary.items.FortuneCoinItem`

```text
      59: invokevirtual #188                // Method scanForEntitiesInRange:(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;D)V
  private void scanForEntitiesInRange(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, double);
      12: ldc           #204                // class net/minecraft/world/entity/item/ItemEntity
      53: checkcast     #204                // class net/minecraft/world/entity/item/ItemEntity
      64: invokevirtual #237                // Method canPickupItem:(Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;Z)Z
      73: invokevirtual #241                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      86: invokevirtual #249                // Method net/minecraft/world/entity/item/ItemEntity.m_32010_:(I)V
     107: invokevirtual #259                // Method teleportEntityToPlayer:(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/player/Player;)V
     194: invokevirtual #259                // Method teleportEntityToPlayer:(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/player/Player;)V
  private boolean canPickupItem(net.minecraft.world.entity.item.ItemEntity, java.util.List<net.minecraft.core.BlockPos>, boolean);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;Z)Z
       1: invokevirtual #282                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
      35: invokevirtual #292                // Method isInDisabledRange:(Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;)Z
      78: invokeinterface #297,  2          // InterfaceMethod reliquary/items/FortuneCoinItem$IFortuneCoinPickupChecker.canPickup:(Lnet/minecraft/world/entity/item/ItemEntity;)Z
  private boolean isInDisabledRange(net.minecraft.world.entity.item.ItemEntity, java.util.List<net.minecraft.core.BlockPos>);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;)Z
      28: invokevirtual #305                // Method net/minecraft/world/entity/item/ItemEntity.m_20183_:()Lnet/minecraft/core/BlockPos;
      48: invokevirtual #305                // Method net/minecraft/world/entity/item/ItemEntity.m_20183_:()Lnet/minecraft/core/BlockPos;
      68: invokevirtual #305                // Method net/minecraft/world/entity/item/ItemEntity.m_20183_:()Lnet/minecraft/core/BlockPos;
  private void teleportEntityToPlayer(net.minecraft.world.entity.Entity, net.minecraft.world.entity.player.Player);
      29: invokevirtual #188                // Method scanForEntitiesInRange:(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;D)V
      28: invokevirtual #566                // Method pickupItems:(Lreliquary/api/IPedestal;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
  private void pickupItems(reliquary.api.IPedestal, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);
       9: ldc           #204                // class net/minecraft/world/entity/item/ItemEntity
      57: checkcast     #204                // class net/minecraft/world/entity/item/ItemEntity
      68: invokevirtual #237                // Method canPickupItem:(Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;Z)Z
      81: invokevirtual #241                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     101: invokevirtual #241                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     106: invokevirtual #241                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     120: invokevirtual #241                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     131: invokevirtual #587                // Method net/minecraft/world/entity/item/ItemEntity.m_146870_:()V
```

#### `reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`

```text
  public abstract boolean canPickup(net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;)Z
```

#### `reliquary.items.FortuneCoinToggler`

```text
(no relevant signatures)
```

#### `reliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings`

```text
(no relevant signatures)
```

#### `reliquary.compat.curios.CuriosFortuneCoinToggler`

```text
(no relevant signatures)
```

#### `reliquary.network.PacketFortuneCoinTogglePressed`

```text
(no relevant signatures)
```


### forge-1.19.2: mob-grinding-utils


### forge-1.19.2: modular-routers

#### `me.desht.modularrouters.logic.compiled.CompiledVacuumModule`

```text
      15: invokevirtual #171                // Method handleItemMode:(Lme/desht/modularrouters/block/tile/ModularRouterBlockEntity;)Z
  private boolean handleItemMode(me.desht.modularrouters.block.tile.ModularRouterBlockEntity);
      41: ldc_w         #283                // class net/minecraft/world/entity/item/ItemEntity
     118: checkcast     #283                // class net/minecraft/world/entity/item/ItemEntity
     125: invokevirtual #317                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
     140: invokevirtual #320                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
     151: invokevirtual #324                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     303: invokevirtual #377                // Method net/minecraft/world/entity/item/ItemEntity.m_142687_:(Lnet/minecraft/world/entity/Entity$RemovalReason;)V
     361: invokevirtual #415                // Method net/minecraft/world/entity/item/ItemEntity.m_20185_:()D
     366: invokevirtual #418                // Method net/minecraft/world/entity/item/ItemEntity.m_20186_:()D
     375: invokevirtual #423                // Method net/minecraft/world/entity/item/ItemEntity.m_20189_:()D
```


## forge-1.18.2

| Integration | Resolved version | Target | Class | Required method strings |
|---|---|---|---|---|
| simple-magnets | `1.1.12-forge-mc1.18` | `com.supermartijn642.simplemagnets.MagnetItem` | exact | inventoryTick: NO, inventoryUpdate: yes, getEntities: NO |
| item-collectors | `1.1.12-forge-mc1.18` | `com.supermartijn642.itemcollectors.CollectorBlockEntity` | exact | tick: NO, getEntitiesOfClass: NO |
| ae2wtlib | `11.6.3-forge` | `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler` | exact | handleMagnet: yes, playerTouch: NO |
| artifacts | `1.0.0` | `artifacts.effect.MagnetismMobEffect` | no-candidate | applyEffectTick: NO, getEntitiesOfClass: NO |
| mekanism | `10.2.5.465` | `mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit` | exact | pullItem: yes |
| draconic-evolution | `3.0.31.531` | `com.brandon3055.draconicevolution.items.tools.Magnet` | exact | updateMagnet: yes, getEntitiesOfClass: NO |
| reliquary | `1.18.2-2.0.19.1161` | `reliquary.item.FortuneCoinItem` | candidate: `reliquary.items.FortuneCoinItem`<br>`reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`<br>`reliquary.items.FortuneCoinToggler`<br>`reliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings` | scanForEntitiesInRange: yes, teleportEntityToPlayer: yes, pickupItems: yes |
| mob-grinding-utils | `unresolved` | `mob_grinding_utils.tile.TileEntityAbsorptionHopper` | unresolved | — |
| modular-routers | `9.1.2` | `me.desht.modularrouters.logic.compiled.CompiledVacuumModule` | exact | handleItemMode: yes, getEntitiesOfClass: NO |
| sophisticated-core | `1.18.2-0.6.4.604` | `net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper` | exact | pickupItems: yes, tryToInsertItem: yes |

### forge-1.18.2: simple-magnets

#### `com.supermartijn642.simplemagnets.MagnetItem`

```text
  public void inventoryUpdate(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);
  private static void playerTouch(net.minecraft.world.entity.item.ItemEntity, net.minecraft.world.entity.player.Player);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)V
       1: getfield      #263                // Field net/minecraft/world/entity/item/ItemEntity.f_19853_:Lnet/minecraft/world/level/Level;
      11: invokevirtual #266                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      19: invokevirtual #270                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      36: invokestatic  #280                // Method net/minecraftforge/event/ForgeEventFactory.onItemPickup:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)I
      54: invokevirtual #266                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      61: invokevirtual #289                // Method net/minecraft/world/entity/item/ItemEntity.m_32056_:()Ljava/util/UUID;
      68: getfield      #292                // Field net/minecraft/world/entity/item/ItemEntity.lifespan:I
      72: invokevirtual #295                // Method net/minecraft/world/entity/item/ItemEntity.m_32059_:()I
      83: invokevirtual #289                // Method net/minecraft/world/entity/item/ItemEntity.m_32056_:()Ljava/util/UUID;
     137: invokestatic  #321                // Method net/minecraftforge/event/ForgeEventFactory.firePlayerItemPickupEvent:(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;)V
     155: invokevirtual #331                // Method net/minecraft/world/entity/item/ItemEntity.m_146870_:()V
     179: invokevirtual #350                // Method net/minecraft/world/entity/player/Player.m_21053_:(Lnet/minecraft/world/entity/item/ItemEntity;)V
      17: instanceof    #262                // class net/minecraft/world/entity/item/ItemEntity
      27: checkcast     #262                // class net/minecraft/world/entity/item/ItemEntity
      30: invokevirtual #392                // Method net/minecraft/world/entity/item/ItemEntity.m_32057_:()Ljava/util/UUID;
      51: checkcast     #262                // class net/minecraft/world/entity/item/ItemEntity
      54: invokespecial #396                // Method com/supermartijn642/simplemagnets/packets/magnet/PacketItemInfo."<init>":(Lnet/minecraft/world/entity/item/ItemEntity;)V
  private static void lambda$inventoryUpdate$3(net.minecraft.world.entity.player.Player, net.minecraft.world.entity.ExperienceOrb);
  private static void lambda$inventoryUpdate$2(net.minecraft.world.entity.Entity, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/item/ItemEntity;)V
       5: invokestatic  #411                // Method playerTouch:(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/entity/player/Player;)V
  private static void lambda$inventoryUpdate$1(net.minecraft.world.entity.Entity, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/item/ItemEntity;)V
      13: invokevirtual #425                // Method net/minecraft/world/entity/item/ItemEntity.m_6034_:(DDD)V
  private boolean lambda$inventoryUpdate$0(net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, net.minecraft.nbt.CompoundTag, net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/entity/item/ItemEntity;)Z
       2: invokevirtual #428                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
      17: getfield      #431                // Field net/minecraft/world/entity/item/ItemEntity.f_19797_:I
      26: invokevirtual #392                // Method net/minecraft/world/entity/item/ItemEntity.m_32057_:()Ljava/util/UUID;
      34: invokevirtual #392                // Method net/minecraft/world/entity/item/ItemEntity.m_32057_:()Ljava/util/UUID;
      49: invokevirtual #266                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
      57: invokevirtual #270                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      68: invokevirtual #435                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
      84: invokevirtual #270                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
```


### forge-1.18.2: item-collectors

#### `com.supermartijn642.itemcollectors.CollectorBlockEntity`

```text
public class com.supermartijn642.itemcollectors.CollectorBlockEntity extends com.supermartijn642.core.block.BaseBlockEntity implements com.supermartijn642.core.block.TickableBlockEntity {
      19: ldc_w         #357                // class net/minecraft/world/entity/item/ItemEntity
     107: checkcast     #357                // class net/minecraft/world/entity/item/ItemEntity
     114: invokevirtual #373                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
     122: invokevirtual #376                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
     136: invokevirtual #376                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
     153: invokevirtual #384                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     334: invokevirtual #411                // Method net/minecraft/world/entity/item/ItemEntity.m_32045_:(Lnet/minecraft/world/item/ItemStack;)V
     342: invokevirtual #419                // Method net/minecraft/world/entity/item/ItemEntity.m_142687_:(Lnet/minecraft/world/entity/Entity$RemovalReason;)V
     358: invokevirtual #411                // Method net/minecraft/world/entity/item/ItemEntity.m_32045_:(Lnet/minecraft/world/item/ItemStack;)V
```


### forge-1.18.2: ae2wtlib

#### `de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler`

```text
       1: invokestatic  #54                 // Method handleMagnet:(Lnet/minecraft/world/entity/player/Player;)V
  private static void handleMagnet(net.minecraft.world.entity.player.Player);
      33: ldc           #204                // class net/minecraft/world/entity/item/ItemEntity
      88: checkcast     #204                // class net/minecraft/world/entity/item/ItemEntity
      95: invokevirtual #242                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     131: invokestatic  #262                // Method de/mari_023/ae2wtlib/Platform.preventRemoteMovement:(Lnet/minecraft/world/entity/item/ItemEntity;)Z
     140: invokevirtual #265                // Method net/minecraft/world/entity/item/ItemEntity.m_6123_:(Lnet/minecraft/world/entity/player/Player;)V
```


### forge-1.18.2: artifacts


### forge-1.18.2: draconic-evolution

#### `com.brandon3055.draconicevolution.items.tools.Magnet`

```text
       3: invokevirtual #45                 // Method updateMagnet:(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)V
  private void updateMagnet(net.minecraft.world.item.ItemStack, net.minecraft.world.entity.Entity);
      37: ldc           #69                 // class net/minecraft/world/entity/item/ItemEntity
      97: ldc           #69                 // class net/minecraft/world/entity/item/ItemEntity
     176: checkcast     #69                 // class net/minecraft/world/entity/item/ItemEntity
     183: invokevirtual #114                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
     194: invokevirtual #118                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
     219: invokevirtual #130                // Method net/minecraft/world/entity/item/ItemEntity.m_32057_:()Ljava/util/UUID;
     227: invokevirtual #130                // Method net/minecraft/world/entity/item/ItemEntity.m_32057_:()Ljava/util/UUID;
     242: getfield      #142                // Field net/minecraft/world/entity/item/ItemEntity.f_31986_:I
     278: invokevirtual #152                // Method net/minecraft/world/entity/item/ItemEntity.m_142538_:()Lnet/minecraft/core/BlockPos;
     394: getfield      #142                // Field net/minecraft/world/entity/item/ItemEntity.f_31986_:I
     403: putfield      #142                // Field net/minecraft/world/entity/item/ItemEntity.f_31986_:I
     411: invokevirtual #192                // Method net/minecraft/world/entity/item/ItemEntity.m_20334_:(DDD)V
     417: putfield      #196                // Field net/minecraft/world/entity/item/ItemEntity.f_19789_:F
     470: invokevirtual #214                // Method net/minecraft/world/entity/item/ItemEntity.m_6034_:(DDD)V
```


### forge-1.18.2: reliquary

Candidate classes: `reliquary.items.FortuneCoinItem`, `reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`, `reliquary.items.FortuneCoinToggler`, `reliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings`, `reliquary.compat.curios.CuriosFortuneCoinToggler`, `reliquary.network.PacketFortuneCoinTogglePressed`, `reliquary.network.PacketFortuneCoinTogglePressed$1`, `reliquary.network.PacketFortuneCoinTogglePressed$InventoryType`

#### `reliquary.items.FortuneCoinItem`

```text
      59: invokevirtual #207                // Method scanForEntitiesInRange:(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;D)V
  private void scanForEntitiesInRange(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, double);
      12: ldc           #223                // class net/minecraft/world/entity/item/ItemEntity
      53: checkcast     #223                // class net/minecraft/world/entity/item/ItemEntity
      64: invokevirtual #256                // Method canPickupItem:(Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;Z)Z
      73: invokevirtual #260                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
      86: invokevirtual #268                // Method net/minecraft/world/entity/item/ItemEntity.m_32010_:(I)V
     107: invokevirtual #278                // Method teleportEntityToPlayer:(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/player/Player;)V
     194: invokevirtual #278                // Method teleportEntityToPlayer:(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/player/Player;)V
  private boolean canPickupItem(net.minecraft.world.entity.item.ItemEntity, java.util.List<net.minecraft.core.BlockPos>, boolean);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;Z)Z
       1: invokevirtual #300                // Method net/minecraft/world/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundTag;
      35: invokevirtual #310                // Method isInDisabledRange:(Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;)Z
      78: invokeinterface #315,  2          // InterfaceMethod reliquary/items/FortuneCoinItem$IFortuneCoinPickupChecker.canPickup:(Lnet/minecraft/world/entity/item/ItemEntity;)Z
  private boolean isInDisabledRange(net.minecraft.world.entity.item.ItemEntity, java.util.List<net.minecraft.core.BlockPos>);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;)Z
      28: invokevirtual #323                // Method net/minecraft/world/entity/item/ItemEntity.m_142538_:()Lnet/minecraft/core/BlockPos;
      48: invokevirtual #323                // Method net/minecraft/world/entity/item/ItemEntity.m_142538_:()Lnet/minecraft/core/BlockPos;
      68: invokevirtual #323                // Method net/minecraft/world/entity/item/ItemEntity.m_142538_:()Lnet/minecraft/core/BlockPos;
  private void teleportEntityToPlayer(net.minecraft.world.entity.Entity, net.minecraft.world.entity.player.Player);
      29: invokevirtual #207                // Method scanForEntitiesInRange:(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;D)V
      28: invokevirtual #586                // Method pickupItems:(Lreliquary/api/IPedestal;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
  private void pickupItems(reliquary.api.IPedestal, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);
       9: ldc           #223                // class net/minecraft/world/entity/item/ItemEntity
      57: checkcast     #223                // class net/minecraft/world/entity/item/ItemEntity
      68: invokevirtual #256                // Method canPickupItem:(Lnet/minecraft/world/entity/item/ItemEntity;Ljava/util/List;Z)Z
      81: invokevirtual #260                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     101: invokevirtual #260                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     106: invokevirtual #260                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     120: invokevirtual #260                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     131: invokevirtual #607                // Method net/minecraft/world/entity/item/ItemEntity.m_146870_:()V
```

#### `reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`

```text
  public abstract boolean canPickup(net.minecraft.world.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/entity/item/ItemEntity;)Z
```

#### `reliquary.items.FortuneCoinToggler`

```text
(no relevant signatures)
```

#### `reliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings`

```text
(no relevant signatures)
```

#### `reliquary.compat.curios.CuriosFortuneCoinToggler`

```text
(no relevant signatures)
```

#### `reliquary.network.PacketFortuneCoinTogglePressed`

```text
(no relevant signatures)
```


### forge-1.18.2: mob-grinding-utils


### forge-1.18.2: modular-routers

#### `me.desht.modularrouters.logic.compiled.CompiledVacuumModule`

```text
      15: invokevirtual #171                // Method handleItemMode:(Lme/desht/modularrouters/block/tile/ModularRouterBlockEntity;)Z
  private boolean handleItemMode(me.desht.modularrouters.block.tile.ModularRouterBlockEntity);
      41: ldc_w         #283                // class net/minecraft/world/entity/item/ItemEntity
     118: checkcast     #283                // class net/minecraft/world/entity/item/ItemEntity
     125: invokevirtual #317                // Method net/minecraft/world/entity/item/ItemEntity.m_6084_:()Z
     140: invokevirtual #320                // Method net/minecraft/world/entity/item/ItemEntity.m_32063_:()Z
     151: invokevirtual #324                // Method net/minecraft/world/entity/item/ItemEntity.m_32055_:()Lnet/minecraft/world/item/ItemStack;
     303: invokevirtual #377                // Method net/minecraft/world/entity/item/ItemEntity.m_142687_:(Lnet/minecraft/world/entity/Entity$RemovalReason;)V
     361: invokevirtual #415                // Method net/minecraft/world/entity/item/ItemEntity.m_20185_:()D
     366: invokevirtual #418                // Method net/minecraft/world/entity/item/ItemEntity.m_20186_:()D
     375: invokevirtual #423                // Method net/minecraft/world/entity/item/ItemEntity.m_20189_:()D
```


## forge-1.16.5

| Integration | Resolved version | Target | Class | Required method strings |
|---|---|---|---|---|
| simple-magnets | `1.1.12-forge-mc1.16` | `com.supermartijn642.simplemagnets.MagnetItem` | exact | inventoryTick: NO, inventoryUpdate: yes, getEntities: NO |
| item-collectors | `1.1.12-forge-mc1.16` | `com.supermartijn642.itemcollectors.CollectorBlockEntity` | exact | tick: NO, getEntitiesOfClass: NO |
| mob-grinding-utils | `unresolved` | `mob_grinding_utils.tile.TileEntityAbsorptionHopper` | unresolved | — |
| modular-routers | `1.16.5-7.5.4` | `me.desht.modularrouters.logic.compiled.CompiledVacuumModule` | exact | handleItemMode: yes, getEntitiesOfClass: NO |
| mekanism | `10.1.2.457` | `mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit` | exact | pullItem: yes |
| draconic-evolution | `3.0.29.518` | `com.brandon3055.draconicevolution.items.tools.Magnet` | exact | updateMagnet: yes, getEntitiesOfClass: NO |
| reliquary | `1.16.5-1.3.5.1124` | `reliquary.item.FortuneCoinItem` | candidate: `xreliquary.items.FortuneCoinItem`<br>`xreliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`<br>`xreliquary.items.FortuneCoinToggler`<br>`xreliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings` | scanForEntitiesInRange: yes, teleportEntityToPlayer: yes, pickupItems: yes |
| sophisticated-core | `unresolved` | `net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper` | unresolved | — |
| industrial-foregoing | `3.2.14.7` | `com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack` | exact | inventoryTick: NO, getEntitiesOfClass: NO |

### forge-1.16.5: simple-magnets

#### `com.supermartijn642.simplemagnets.MagnetItem`

```text
  public void inventoryUpdate(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.Entity, int, boolean);
  private static void playerTouch(net.minecraft.entity.item.ItemEntity, net.minecraft.entity.player.PlayerEntity);
    descriptor: (Lnet/minecraft/entity/item/ItemEntity;Lnet/minecraft/entity/player/PlayerEntity;)V
       1: getfield      #263                // Field net/minecraft/entity/item/ItemEntity.field_70170_p:Lnet/minecraft/world/World;
      11: invokevirtual #266                // Method net/minecraft/entity/item/ItemEntity.func_174874_s:()Z
      19: invokevirtual #270                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
      36: invokestatic  #280                // Method net/minecraftforge/event/ForgeEventFactory.onItemPickup:(Lnet/minecraft/entity/item/ItemEntity;Lnet/minecraft/entity/player/PlayerEntity;)I
      54: invokevirtual #266                // Method net/minecraft/entity/item/ItemEntity.func_174874_s:()Z
      61: invokevirtual #289                // Method net/minecraft/entity/item/ItemEntity.func_200215_l:()Ljava/util/UUID;
      68: getfield      #292                // Field net/minecraft/entity/item/ItemEntity.lifespan:I
      72: invokevirtual #295                // Method net/minecraft/entity/item/ItemEntity.func_174872_o:()I
      83: invokevirtual #289                // Method net/minecraft/entity/item/ItemEntity.func_200215_l:()Ljava/util/UUID;
     137: invokestatic  #323                // Method net/minecraftforge/fml/hooks/BasicEventHooks.firePlayerItemPickupEvent:(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/item/ItemEntity;Lnet/minecraft/item/ItemStack;)V
     155: invokevirtual #333                // Method net/minecraft/entity/item/ItemEntity.func_70106_y:()V
     179: invokevirtual #352                // Method net/minecraft/entity/player/PlayerEntity.func_233630_a_:(Lnet/minecraft/entity/item/ItemEntity;)V
      17: instanceof    #262                // class net/minecraft/entity/item/ItemEntity
      27: checkcast     #262                // class net/minecraft/entity/item/ItemEntity
      30: invokevirtual #394                // Method net/minecraft/entity/item/ItemEntity.func_200214_m:()Ljava/util/UUID;
      51: checkcast     #262                // class net/minecraft/entity/item/ItemEntity
      54: invokespecial #398                // Method com/supermartijn642/simplemagnets/packets/magnet/PacketItemInfo."<init>":(Lnet/minecraft/entity/item/ItemEntity;)V
  private static void lambda$inventoryUpdate$3(net.minecraft.entity.player.PlayerEntity, net.minecraft.entity.item.ExperienceOrbEntity);
  private static void lambda$inventoryUpdate$2(net.minecraft.entity.Entity, net.minecraft.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/item/ItemEntity;)V
       5: invokestatic  #413                // Method playerTouch:(Lnet/minecraft/entity/item/ItemEntity;Lnet/minecraft/entity/player/PlayerEntity;)V
  private static void lambda$inventoryUpdate$1(net.minecraft.entity.Entity, net.minecraft.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/item/ItemEntity;)V
      13: invokevirtual #427                // Method net/minecraft/entity/item/ItemEntity.func_70107_b:(DDD)V
  private boolean lambda$inventoryUpdate$0(net.minecraft.world.World, net.minecraft.entity.Entity, net.minecraft.nbt.CompoundNBT, net.minecraft.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/nbt/CompoundNBT;Lnet/minecraft/entity/item/ItemEntity;)Z
       2: invokevirtual #430                // Method net/minecraft/entity/item/ItemEntity.func_70089_S:()Z
      17: getfield      #433                // Field net/minecraft/entity/item/ItemEntity.field_70173_aa:I
      26: invokevirtual #394                // Method net/minecraft/entity/item/ItemEntity.func_200214_m:()Ljava/util/UUID;
      34: invokevirtual #394                // Method net/minecraft/entity/item/ItemEntity.func_200214_m:()Ljava/util/UUID;
      49: invokevirtual #266                // Method net/minecraft/entity/item/ItemEntity.func_174874_s:()Z
      57: invokevirtual #270                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
      68: invokevirtual #437                // Method net/minecraft/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundNBT;
      84: invokevirtual #270                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
```


### forge-1.16.5: item-collectors

#### `com.supermartijn642.itemcollectors.CollectorBlockEntity`

```text
public class com.supermartijn642.itemcollectors.CollectorBlockEntity extends com.supermartijn642.core.block.BaseBlockEntity implements com.supermartijn642.core.block.TickableBlockEntity {
      19: ldc_w         #355                // class net/minecraft/entity/item/ItemEntity
     107: checkcast     #355                // class net/minecraft/entity/item/ItemEntity
     114: invokevirtual #371                // Method net/minecraft/entity/item/ItemEntity.func_70089_S:()Z
     122: invokevirtual #374                // Method net/minecraft/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundNBT;
     136: invokevirtual #374                // Method net/minecraft/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundNBT;
     153: invokevirtual #382                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
     334: invokevirtual #409                // Method net/minecraft/entity/item/ItemEntity.func_92058_a:(Lnet/minecraft/item/ItemStack;)V
     339: invokevirtual #412                // Method net/minecraft/entity/item/ItemEntity.func_70106_y:()V
     355: invokevirtual #409                // Method net/minecraft/entity/item/ItemEntity.func_92058_a:(Lnet/minecraft/item/ItemStack;)V
```


### forge-1.16.5: mob-grinding-utils


### forge-1.16.5: modular-routers

#### `me.desht.modularrouters.logic.compiled.CompiledVacuumModule`

```text
      15: invokespecial #164                // Method handleItemMode:(Lme/desht/modularrouters/block/tile/TileEntityItemRouter;)Z
  private boolean handleItemMode(me.desht.modularrouters.block.tile.TileEntityItemRouter);
      41: ldc_w         #275                // class net/minecraft/entity/item/ItemEntity
     118: checkcast     #275                // class net/minecraft/entity/item/ItemEntity
     125: invokevirtual #310                // Method net/minecraft/entity/item/ItemEntity.func_70089_S:()Z
     140: invokevirtual #313                // Method net/minecraft/entity/item/ItemEntity.func_174874_s:()Z
     151: invokevirtual #317                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
     300: invokevirtual #366                // Method net/minecraft/entity/item/ItemEntity.func_70106_y:()V
     343: invokevirtual #387                // Method net/minecraft/entity/item/ItemEntity.func_226277_ct_:()D
     348: invokevirtual #390                // Method net/minecraft/entity/item/ItemEntity.func_226278_cu_:()D
     357: invokevirtual #395                // Method net/minecraft/entity/item/ItemEntity.func_226281_cx_:()D
```


### forge-1.16.5: draconic-evolution

#### `com.brandon3055.draconicevolution.items.tools.Magnet`

```text
       3: invokespecial #45                 // Method updateMagnet:(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;)V
  private void updateMagnet(net.minecraft.item.ItemStack, net.minecraft.entity.Entity);
      37: ldc           #69                 // class net/minecraft/entity/item/ItemEntity
      97: ldc           #69                 // class net/minecraft/entity/item/ItemEntity
     176: checkcast     #69                 // class net/minecraft/entity/item/ItemEntity
     183: invokevirtual #114                // Method net/minecraft/entity/item/ItemEntity.func_70089_S:()Z
     194: invokevirtual #118                // Method net/minecraft/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundNBT;
     219: invokevirtual #130                // Method net/minecraft/entity/item/ItemEntity.func_200214_m:()Ljava/util/UUID;
     227: invokevirtual #130                // Method net/minecraft/entity/item/ItemEntity.func_200214_m:()Ljava/util/UUID;
     242: getfield      #142                // Field net/minecraft/entity/item/ItemEntity.field_145804_b:I
     278: invokevirtual #152                // Method net/minecraft/entity/item/ItemEntity.func_233580_cy_:()Lnet/minecraft/util/math/BlockPos;
     394: getfield      #142                // Field net/minecraft/entity/item/ItemEntity.field_145804_b:I
     403: putfield      #142                // Field net/minecraft/entity/item/ItemEntity.field_145804_b:I
     411: invokevirtual #192                // Method net/minecraft/entity/item/ItemEntity.func_213293_j:(DDD)V
     417: putfield      #196                // Field net/minecraft/entity/item/ItemEntity.field_70143_R:F
     470: invokevirtual #214                // Method net/minecraft/entity/item/ItemEntity.func_70107_b:(DDD)V
```


### forge-1.16.5: reliquary

Candidate classes: `xreliquary.items.FortuneCoinItem`, `xreliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`, `xreliquary.items.FortuneCoinToggler`, `xreliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings`, `xreliquary.compat.curios.CuriosFortuneCoinToggler`, `xreliquary.network.PacketFortuneCoinTogglePressed`, `xreliquary.network.PacketFortuneCoinTogglePressed$1`, `xreliquary.network.PacketFortuneCoinTogglePressed$InventoryType`

#### `xreliquary.items.FortuneCoinItem`

```text
      67: invokespecial #202                // Method scanForEntitiesInRange:(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;D)V
  private void scanForEntitiesInRange(net.minecraft.world.World, net.minecraft.entity.player.PlayerEntity, double);
      12: ldc           #218                // class net/minecraft/entity/item/ItemEntity
      53: checkcast     #218                // class net/minecraft/entity/item/ItemEntity
      64: invokespecial #251                // Method canPickupItem:(Lnet/minecraft/entity/item/ItemEntity;Ljava/util/List;Z)Z
      73: invokevirtual #255                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
      86: invokevirtual #263                // Method net/minecraft/entity/item/ItemEntity.func_174867_a:(I)V
     107: invokespecial #273                // Method teleportEntityToPlayer:(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/player/PlayerEntity;)V
     194: invokespecial #273                // Method teleportEntityToPlayer:(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/player/PlayerEntity;)V
  private boolean canPickupItem(net.minecraft.entity.item.ItemEntity, java.util.List<net.minecraft.util.math.BlockPos>, boolean);
    descriptor: (Lnet/minecraft/entity/item/ItemEntity;Ljava/util/List;Z)Z
       1: invokevirtual #295                // Method net/minecraft/entity/item/ItemEntity.getPersistentData:()Lnet/minecraft/nbt/CompoundNBT;
      35: invokespecial #305                // Method isInDisabledRange:(Lnet/minecraft/entity/item/ItemEntity;Ljava/util/List;)Z
      78: invokeinterface #310,  2          // InterfaceMethod xreliquary/items/FortuneCoinItem$IFortuneCoinPickupChecker.canPickup:(Lnet/minecraft/entity/item/ItemEntity;)Z
  private boolean isInDisabledRange(net.minecraft.entity.item.ItemEntity, java.util.List<net.minecraft.util.math.BlockPos>);
    descriptor: (Lnet/minecraft/entity/item/ItemEntity;Ljava/util/List;)Z
      28: invokevirtual #318                // Method net/minecraft/entity/item/ItemEntity.func_233580_cy_:()Lnet/minecraft/util/math/BlockPos;
      48: invokevirtual #318                // Method net/minecraft/entity/item/ItemEntity.func_233580_cy_:()Lnet/minecraft/util/math/BlockPos;
      68: invokevirtual #318                // Method net/minecraft/entity/item/ItemEntity.func_233580_cy_:()Lnet/minecraft/util/math/BlockPos;
  private void teleportEntityToPlayer(net.minecraft.entity.Entity, net.minecraft.entity.player.PlayerEntity);
      26: invokespecial #202                // Method scanForEntitiesInRange:(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;D)V
      35: invokespecial #585                // Method pickupItems:(Lxreliquary/api/IPedestal;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V
  private void pickupItems(xreliquary.api.IPedestal, net.minecraft.world.World, net.minecraft.util.math.BlockPos);
       9: ldc           #218                // class net/minecraft/entity/item/ItemEntity
      57: checkcast     #218                // class net/minecraft/entity/item/ItemEntity
      68: invokespecial #251                // Method canPickupItem:(Lnet/minecraft/entity/item/ItemEntity;Ljava/util/List;Z)Z
      80: invokevirtual #255                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
     100: invokevirtual #255                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
     105: invokevirtual #255                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
     119: invokevirtual #255                // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
     130: invokevirtual #604                // Method net/minecraft/entity/item/ItemEntity.func_70106_y:()V
```

#### `xreliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker`

```text
  public abstract boolean canPickup(net.minecraft.entity.item.ItemEntity);
    descriptor: (Lnet/minecraft/entity/item/ItemEntity;)Z
```

#### `xreliquary.items.FortuneCoinToggler`

```text
(no relevant signatures)
```

#### `xreliquary.reference.Settings$Common$ItemSettings$FortuneCoinSettings`

```text
(no relevant signatures)
```

#### `xreliquary.compat.curios.CuriosFortuneCoinToggler`

```text
(no relevant signatures)
```

#### `xreliquary.network.PacketFortuneCoinTogglePressed`

```text
(no relevant signatures)
```


### forge-1.16.5: sophisticated-core


### forge-1.16.5: industrial-foregoing

#### `com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack`

```text
      11: ldc_w         #574                // class net/minecraft/entity/item/ItemEntity
      78: checkcast     #574                // class net/minecraft/entity/item/ItemEntity
      95: invokevirtual #608                // Method net/minecraft/entity/item/ItemEntity.func_233570_aj_:()Z
     115: getfield      #619                // Field net/minecraft/entity/item/ItemEntity.field_70170_p:Lnet/minecraft/world/World;
     123: invokevirtual #626                // Method net/minecraft/entity/item/ItemEntity.func_226277_ct_:()D
     128: invokevirtual #627                // Method net/minecraft/entity/item/ItemEntity.func_226278_cu_:()D
     137: invokevirtual #630                // Method net/minecraft/entity/item/ItemEntity.func_226281_cx_:()D
     153: invokevirtual #639                // Method net/minecraft/entity/item/ItemEntity.func_174874_s:()Z
     181: invokevirtual #646                // Method net/minecraft/entity/item/ItemEntity.func_70634_a:(DDD)V
      92: invokevirtual #1445               // Method net/minecraftforge/event/entity/player/EntityItemPickupEvent.getItem:()Lnet/minecraft/entity/item/ItemEntity;
      95: getfield      #619                // Field net/minecraft/entity/item/ItemEntity.field_70170_p:Lnet/minecraft/world/World;
     144: invokevirtual #1445               // Method net/minecraftforge/event/entity/player/EntityItemPickupEvent.getItem:()Lnet/minecraft/entity/item/ItemEntity;
     147: invokevirtual #1448               // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
       1: invokevirtual #1445               // Method net/minecraftforge/event/entity/player/EntityItemPickupEvent.getItem:()Lnet/minecraft/entity/item/ItemEntity;
       4: invokevirtual #1448               // Method net/minecraft/entity/item/ItemEntity.func_92059_d:()Lnet/minecraft/item/ItemStack;
```

