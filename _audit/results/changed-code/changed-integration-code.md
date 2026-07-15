# Changed integration code inspection

## main-1.21.1-neoforge — projecte

Version: `1.21.1-1.0.6`

Missing targets:
- `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand`

Highest-signal candidate classes:
- `cool/furry/mc/neoforge/projectexpansion/events/RenderingEvent.class`
- `cool/furry/mc/neoforge/projectexpansion/item/ItemKnowledgeSharingBook.class`
- `cool/furry/mc/neoforge/projectexpansion/rendering/ChestRenderer.class`
- `cool/furry/mc/neoforge/projectexpansion/rendering/package-info.class`
- `cool/furry/mc/neoforge/projectexpansion/util/BasicDataComponentTypes$StringValue.class`
- `cool/furry/mc/neoforge/projectexpansion/Main.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockAdvancedAlchemicalChest$ContainerProvider.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockAdvancedAlchemicalChest.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockCollector.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockCompactSun.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockCondenserMK3.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockEMCLink.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockMatter.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockPowerFlower.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockRelay.class`
- `cool/furry/mc/neoforge/projectexpansion/block/BlockTransmutationInterface.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityAdvancedAlchemicalChest.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityBase$CompactableStackHandler.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityCollector.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityCondenserMK3$SidedHandler.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityCondenserMK3.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityEMC.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityEMCLink.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityNBTFilterable.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityOwnable$ActivationType.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityOwnable.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityPowerFlower.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityRelay.class`
- `cool/furry/mc/neoforge/projectexpansion/block/entity/BlockEntityTransmutationInterface.class`
- `cool/furry/mc/neoforge/projectexpansion/capability/CapabilityAlchemicalBookLocations$AlchemicalBookLocationData.class`

`cool.furry.mc.neoforge.projectexpansion.events.RenderingEvent` methods:
- `public static void registerRenderers(net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterRenderers);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerChest$0(net.minecraft.world.level.block.entity.BlockEntityType, net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`

`cool.furry.mc.neoforge.projectexpansion.item.ItemKnowledgeSharingBook` methods:
- `public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> use(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand);`
- `public boolean isFoil(net.minecraft.world.item.ItemStack);`
- `public void appendHoverText(net.minecraft.world.item.ItemStack, net.minecraft.world.item.Item$TooltipContext, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`

`cool.furry.mc.neoforge.projectexpansion.rendering.ChestRenderer` methods:
- `public void render(net.minecraft.world.level.block.entity.BlockEntity, float, com.mojang.blaze3d.vertex.PoseStack, net.minecraft.client.renderer.MultiBufferSource, int, int);`
- `private net.minecraft.resources.ResourceLocation getTexture(net.minecraft.world.level.block.Block);`

`cool.furry.mc.neoforge.projectexpansion.util.BasicDataComponentTypes$StringValue` methods:
- `public final java.lang.String toString();`
- `public final int hashCode();`
- `public final boolean equals(java.lang.Object);`
- `public java.lang.String value();`
- `private static com.mojang.datafixers.kinds.App lambda$static$0(com.mojang.serialization.codecs.RecordCodecBuilder$Instance);`

`cool.furry.mc.neoforge.projectexpansion.Main` methods:
- `public static cool.furry.mc.neoforge.projectexpansion.net.PacketHandler packetHandler();`
- `private void serverTick(net.neoforged.neoforge.event.tick.ServerTickEvent$Post);`
- `public static net.minecraft.resources.ResourceLocation rl(java.lang.String);`

`cool.furry.mc.neoforge.projectexpansion.block.BlockAdvancedAlchemicalChest$ContainerProvider` methods:
- `public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int, net.minecraft.world.entity.player.Inventory, net.minecraft.world.entity.player.Player);`
- `public net.minecraft.network.chat.Component getDisplayName();`
- `public final java.lang.String toString();`
- `public final int hashCode();`
- `public final boolean equals(java.lang.Object);`
- `public cool.furry.mc.neoforge.projectexpansion.block.entity.BlockEntityAdvancedAlchemicalChest blockEntity();`
- `public net.minecraft.world.InteractionHand hand();`

`cool.furry.mc.neoforge.projectexpansion.block.BlockAdvancedAlchemicalChest` methods:
- `public static net.minecraft.world.level.block.state.BlockBehaviour$Properties getProperties();`
- `public net.minecraft.world.item.DyeColor getColor();`
- `public void appendHoverText(net.minecraft.world.item.ItemStack, net.minecraft.world.item.Item$TooltipContext, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.block.RenderShape getRenderShape(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.phys.shapes.VoxelShape getShape(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.phys.shapes.CollisionContext);`
- `public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `protected net.minecraft.world.InteractionResult useWithoutItem(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.phys.BlockHitResult);`
- `protected net.minecraft.world.ItemInteractionResult useItemOn(net.minecraft.world.item.ItemStack, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public void setPlacedBy(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public void tick(net.minecraft.world.level.block.state.BlockState, net.minecraft.server.level.ServerLevel, net.minecraft.core.BlockPos, net.minecraft.util.RandomSource);`
- `protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `protected boolean isPathfindable(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.pathfinder.PathComputationType);`
- `public boolean hasAnalogOutputSignal(net.minecraft.world.level.block.state.BlockState);`
- `public int getAnalogOutputSignal(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public net.minecraft.world.level.block.state.BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext);`
- `public net.minecraft.world.level.material.FluidState getFluidState(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.block.state.BlockState updateShape(net.minecraft.world.level.block.state.BlockState, net.minecraft.core.Direction, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.LevelAccessor, net.minecraft.core.BlockPos, net.minecraft.core.BlockPos);`
- `public boolean triggerEvent(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int, int);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `private static void lambda$useWithoutItem$1(net.minecraft.world.InteractionHand, net.minecraft.world.entity.player.Player, net.minecraft.core.BlockPos, net.minecraft.network.RegistryFriendlyByteBuf);`
- `private static int lambda$getProperties$0(net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.neoforge.projectexpansion.block.BlockCollector` methods:
- `public static net.minecraft.world.level.block.state.BlockBehaviour$Properties getProperties(cool.furry.mc.neoforge.projectexpansion.util.Matter);`
- `private static float getDestroyTime(cool.furry.mc.neoforge.projectexpansion.util.Matter);`
- `private static float getExplosionResistance(cool.furry.mc.neoforge.projectexpansion.util.Matter);`
- `public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public cool.furry.mc.neoforge.projectexpansion.util.Matter getMatter();`
- `public moze_intel.projecte.gameObjs.IMatterType getMatterType();`
- `public void appendHoverText(net.minecraft.world.item.ItemStack, net.minecraft.world.item.Item$TooltipContext, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction getPistonPushReaction(net.minecraft.world.level.block.state.BlockState);`
- `protected net.minecraft.world.InteractionResult useWithoutItem(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.phys.BlockHitResult);`
- `public boolean hasAnalogOutputSignal(net.minecraft.world.level.block.state.BlockState);`
- `public int getAnalogOutputSignal(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public void onRemove(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, boolean);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `private static int lambda$getProperties$0(cool.furry.mc.neoforge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.neoforge.projectexpansion.block.BlockCompactSun` methods:
- `public static net.minecraft.world.level.block.state.BlockBehaviour$Properties getProperties();`
- `public void appendHoverText(net.minecraft.world.item.ItemStack, net.minecraft.world.item.Item$TooltipContext, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.material.PushReaction getPistonPushReaction(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `public void stepOn(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.Entity);`
- `public moze_intel.projecte.gameObjs.IMatterType getMatterType();`
- `public static boolean adjacent(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos);`
- `public static boolean adjacent(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.core.Direction);`
- `private static int lambda$getProperties$0(net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.neoforge.projectexpansion.block.BlockCondenserMK3` methods:
- `public static net.minecraft.world.level.block.state.BlockBehaviour$Properties getProperties();`
- `protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `public void appendHoverText(net.minecraft.world.item.ItemStack, net.minecraft.world.item.Item$TooltipContext, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.block.state.BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext);`
- `protected boolean isPathfindable(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.pathfinder.PathComputationType);`
- `protected net.minecraft.world.phys.shapes.VoxelShape getShape(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.phys.shapes.CollisionContext);`
- `public net.minecraft.world.level.block.RenderShape getRenderShape(net.minecraft.world.level.block.state.BlockState);`
- `protected net.minecraft.world.InteractionResult useWithoutItem(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.phys.BlockHitResult);`
- `public boolean triggerEvent(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int, int);`
- `public boolean triggerBlockEntityEvent(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int, int);`
- `public void tick(net.minecraft.world.level.block.state.BlockState, net.minecraft.server.level.ServerLevel, net.minecraft.core.BlockPos, net.minecraft.util.RandomSource);`
- `public boolean hasAnalogOutputSignal(net.minecraft.world.level.block.state.BlockState);`
- `public int getAnalogOutputSignal(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public net.minecraft.world.level.material.FluidState getFluidState(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.block.state.BlockState updateShape(net.minecraft.world.level.block.state.BlockState, net.minecraft.core.Direction, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.LevelAccessor, net.minecraft.core.BlockPos, net.minecraft.core.BlockPos);`
- `public void onRemove(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, boolean);`
- `public void attack(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player);`
- `public net.minecraft.world.level.block.state.BlockState rotate(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.Rotation);`
- `public net.minecraft.world.level.block.state.BlockState mirror(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.Mirror);`
- `public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`

`cool.furry.mc.neoforge.projectexpansion.block.BlockEMCLink` methods:
- `public static net.minecraft.world.level.block.state.BlockBehaviour$Properties getProperties(cool.furry.mc.neoforge.projectexpansion.util.Matter);`
- `private static float getDestroyTime(cool.furry.mc.neoforge.projectexpansion.util.Matter);`
- `private static float getExplosionResistance(cool.furry.mc.neoforge.projectexpansion.util.Matter);`
- `public cool.furry.mc.neoforge.projectexpansion.util.Matter getMatter();`
- `public moze_intel.projecte.gameObjs.IMatterType getMatterType();`
- `public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public void appendHoverText(net.minecraft.world.item.ItemStack, net.minecraft.world.item.Item$TooltipContext, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `protected net.minecraft.world.InteractionResult useWithoutItem(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.phys.BlockHitResult);`
- `public void setPlacedBy(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction getPistonPushReaction(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `private static int lambda$getProperties$0(cool.furry.mc.neoforge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

## forge-1.20.1 — artifacts

Version: `9.5.19`

Missing targets:
- `artifacts.effect.MagnetismMobEffect`

Highest-signal candidate classes:
- `artifacts/item/wearable/belt/UniversalAttractorItem.class`

`artifacts.item.wearable.belt.UniversalAttractorItem` methods:
- `public boolean hasNonCosmeticEffects();`
- `public void wornTick(net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public boolean makesPiglinsNeutral();`

## forge-1.20.1 — enderio

Version: `6.2.18-beta`

Missing targets:
- `com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity`
- `com.enderio.enderio.content.tools.ElectromagnetItem`

Highest-signal candidate classes:
- `com/enderio/machines/common/blockentity/base/VacuumMachineBlockEntity.class`
- `com/enderio/base/common/item/tool/ElectromagnetItem.class`
- `com/enderio/machines/common/blockentity/VacuumChestBlockEntity.class`
- `com/enderio/machines/client/gui/screen/VacuumChestScreen.class`
- `com/enderio/machines/client/gui/screen/XPVacuumScreen.class`
- `com/enderio/machines/common/blockentity/XPVacuumBlockEntity$1.class`
- `com/enderio/machines/common/blockentity/XPVacuumBlockEntity.class`
- `com/enderio/machines/common/menu/VacuumChestMenu.class`
- `com/enderio/machines/common/menu/XPVacuumMenu.class`
- `com/enderio/base/common/advancement/UseGliderAdvancementBenefit.class`
- `com/enderio/base/common/config/common/ItemsConfig.class`
- `com/enderio/base/common/enchantment/SoulBoundHandler.class`
- `com/enderio/base/common/entity/PaintedSandEntity.class`
- `com/enderio/base/common/handler/FireCraftingHandler.class`
- `com/enderio/base/common/init/EIOItems.class`
- `com/enderio/base/common/item/tool/SoulVialItem.class`
- `com/enderio/base/common/menu/CoordinateMenu.class`
- `com/enderio/base/common/tag/EIOTags$Items.class`
- `com/enderio/base/data/recipe/ItemRecipeProvider.class`
- `com/enderio/conduits/common/conduit/block/ConduitBlockEntity.class`
- `com/enderio/machines/common/config/client/MachinesClientBlocksConfig.class`
- `com/enderio/machines/common/entity/FallingMachineEntity.class`
- `com/enderio/machines/common/init/MachineBlockEntities.class`
- `com/enderio/machines/common/init/MachineBlocks.class`
- `com/enderio/machines/common/init/MachineMenus.class`
- `com/enderio/machines/common/io/fluid/IFluidItemInteractive.class`
- `com/enderio/machines/data/recipes/MachineRecipeProvider.class`

`com.enderio.machines.common.blockentity.base.VacuumMachineBlockEntity` methods:
- `public void serverTick();`
- `public void clientTick();`
- `protected com.enderio.api.io.IIOConfig createIOConfig();`
- `public java.util.function.Predicate<T> getFilter();`
- `private void attractEntities(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int);`
- `public abstract void handleEntity(T);`
- `private void getEntities(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int, java.util.function.Predicate<T>);`
- `public int getMaxRange();`
- `public void onLoad();`
- `private static boolean lambda$getFilter$3(net.minecraft.world.entity.Entity);`
- `private void lambda$new$2(java.lang.Boolean);`
- `private void lambda$new$1(java.lang.Integer);`
- `private static boolean lambda$static$0(net.minecraft.world.entity.item.ItemEntity);`

`com.enderio.base.common.item.tool.ElectromagnetItem` methods:
- `protected int getEnergyUse();`
- `protected int getMaxEnergy();`
- `private int getRange();`
- `private int getMaxItems();`
- `private boolean isBlacklisted(net.minecraft.world.entity.item.ItemEntity);`
- `private boolean isMagnetable(net.minecraft.world.entity.Entity);`
- `protected void onTickWhenActive(net.minecraft.world.entity.player.Player, net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);`

`com.enderio.machines.common.blockentity.VacuumChestBlockEntity` methods:
- `public net.minecraft.world.inventory.AbstractContainerMenu m_7208_(int, net.minecraft.world.entity.player.Inventory, net.minecraft.world.entity.player.Player);`
- `public com.enderio.machines.common.io.item.MachineInventoryLayout createInventoryLayout();`
- `private boolean acceptFilter(int, net.minecraft.world.item.ItemStack);`
- `public void handleEntity(net.minecraft.world.entity.item.ItemEntity);`
- `public java.lang.String getColor();`
- `public java.util.function.Predicate<net.minecraft.world.entity.item.ItemEntity> getFilter();`
- `public com.enderio.machines.common.io.item.MachineInventoryLayout$Builder extractableGUISlot(com.enderio.machines.common.io.item.MachineInventoryLayout$Builder, int);`
- `public void handleEntity(net.minecraft.world.entity.Entity);`
- `private static com.enderio.machines.common.io.item.MachineInventoryLayout$Builder$SlotBuilder lambda$extractableGUISlot$4(com.enderio.machines.common.io.item.MachineInventoryLayout$Builder$SlotBuilder);`
- `private static java.util.function.Predicate lambda$getFilter$3(com.enderio.api.filter.ResourceFilter);`
- `private static boolean lambda$getFilter$2(com.enderio.api.filter.ItemStackFilter, net.minecraft.world.entity.item.ItemEntity);`
- `private static java.lang.Boolean lambda$acceptFilter$1(com.enderio.api.filter.ResourceFilter);`
- `private com.enderio.machines.common.io.item.MachineInventoryLayout$Builder$SlotBuilder lambda$createInventoryLayout$0(com.enderio.machines.common.io.item.MachineInventoryLayout$Builder$SlotBuilder);`

`com.enderio.machines.client.gui.screen.VacuumChestScreen` methods:
- `protected void m_7856_();`
- `public net.minecraft.resources.ResourceLocation getBackgroundImage();`
- `protected com.enderio.api.misc.Vector2i getBackgroundImageSize();`
- `protected void m_280003_(net.minecraft.client.gui.GuiGraphics, int, int);`
- `public void m_88315_(net.minecraft.client.gui.GuiGraphics, int, int, float);`
- `private void lambda$init$8(java.lang.Integer);`
- `private java.lang.Integer lambda$init$7();`
- `private void lambda$init$6(java.lang.Integer);`
- `private java.lang.Integer lambda$init$5();`
- `private net.minecraft.network.chat.Component lambda$init$4();`
- `private void lambda$init$3(java.lang.Boolean);`
- `private java.lang.Boolean lambda$init$2();`
- `private void lambda$init$1(com.enderio.api.misc.RedstoneControl);`
- `private com.enderio.api.misc.RedstoneControl lambda$init$0();`

`com.enderio.machines.client.gui.screen.XPVacuumScreen` methods:
- `protected void m_7856_();`
- `public net.minecraft.resources.ResourceLocation getBackgroundImage();`
- `protected com.enderio.api.misc.Vector2i getBackgroundImageSize();`
- `protected void m_280003_(net.minecraft.client.gui.GuiGraphics, int, int);`
- `public void m_88315_(net.minecraft.client.gui.GuiGraphics, int, int, float);`
- `private void lambda$init$8(java.lang.Integer);`
- `private java.lang.Integer lambda$init$7();`
- `private void lambda$init$6(java.lang.Integer);`
- `private java.lang.Integer lambda$init$5();`
- `private net.minecraft.network.chat.Component lambda$init$4();`
- `private void lambda$init$3(java.lang.Boolean);`
- `private java.lang.Boolean lambda$init$2();`
- `private void lambda$init$1(com.enderio.api.misc.RedstoneControl);`
- `private com.enderio.api.misc.RedstoneControl lambda$init$0();`

`com.enderio.machines.common.blockentity.XPVacuumBlockEntity$1` methods:
- `protected void onContentsChanged(int);`

`com.enderio.machines.common.blockentity.XPVacuumBlockEntity` methods:
- `public java.lang.String getColor();`
- `public net.minecraft.world.inventory.AbstractContainerMenu m_7208_(int, net.minecraft.world.entity.player.Inventory, net.minecraft.world.entity.player.Player);`
- `public void handleEntity(net.minecraft.world.entity.ExperienceOrb);`
- `public com.enderio.machines.common.io.fluid.MachineTankLayout getTankLayout();`
- `protected com.enderio.machines.common.io.fluid.MachineFluidHandler createFluidHandler(com.enderio.machines.common.io.fluid.MachineTankLayout);`
- `public com.enderio.machines.common.io.fluid.MachineFluidTank getFluidTank();`
- `public void handleEntity(net.minecraft.world.entity.Entity);`
- `private void lambda$new$1(java.lang.Integer);`
- `private java.lang.Integer lambda$new$0();`

`com.enderio.machines.common.menu.VacuumChestMenu` methods:
- `public static com.enderio.machines.common.menu.VacuumChestMenu factory(net.minecraft.world.inventory.MenuType<com.enderio.machines.common.menu.VacuumChestMenu>, int, net.minecraft.world.entity.player.Inventory, net.minecraft.network.FriendlyByteBuf);`

`com.enderio.machines.common.menu.XPVacuumMenu` methods:
- `public static com.enderio.machines.common.menu.XPVacuumMenu factory(net.minecraft.world.inventory.MenuType<com.enderio.machines.common.menu.XPVacuumMenu>, int, net.minecraft.world.entity.player.Inventory, net.minecraft.network.FriendlyByteBuf);`

`com.enderio.base.common.advancement.UseGliderAdvancementBenefit` methods:
- `public static void onEarnAdvancement(net.minecraftforge.event.entity.player.AdvancementEvent$AdvancementEarnEvent);`

`com.enderio.base.common.enchantment.SoulBoundHandler` methods:
- `public static void deathHandler(net.minecraftforge.event.entity.living.LivingDropsEvent);`
- `public static void reviveHandler(net.minecraftforge.event.entity.player.PlayerEvent$Clone);`
- `public static boolean isSoulBound(net.minecraft.world.item.ItemStack);`
- `private static void lambda$reviveHandler$2(net.minecraftforge.event.entity.player.PlayerEvent$Clone, net.minecraft.world.item.ItemStack);`
- `private static void lambda$reviveHandler$1(net.minecraftforge.event.entity.player.PlayerEvent$Clone, net.minecraft.world.item.ItemStack);`
- `private static void lambda$reviveHandler$0(net.minecraftforge.event.entity.player.PlayerEvent$Clone, net.minecraft.world.item.ItemStack);`

## forge-1.20.1 — projecte

Version: `1.20.1-1.1.3`

Missing targets:
- `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand`

Highest-signal candidate classes:
- `cool/furry/mc/forge/projectexpansion/events/RenderingEvent.class`
- `cool/furry/mc/forge/projectexpansion/item/ItemKnowledgeSharingBook.class`
- `cool/furry/mc/forge/projectexpansion/rendering/ChestRenderer.class`
- `cool/furry/mc/forge/projectexpansion/rendering/package-info.class`
- `cool/furry/mc/forge/projectexpansion/Main.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockAdvancedAlchemicalChest$ContainerProvider.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockAdvancedAlchemicalChest.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockCollector.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockCompactSun.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockEMCLink.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockMatter.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockPowerFlower.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockRelay.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockTransmutationInterface.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityAdvancedAlchemicalChest.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityCollector$CollectorItemHandlerProvider.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityCollector.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMC$CompactableStackHandler.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMC.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMCLink.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityNBTFilterable.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityOwnable$ActivationType.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityOwnable.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityPowerFlower.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityRelay.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityTransmutationInterface.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$DimensionNotFoundError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$DuplicateNameError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$NameNotFoundError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$NotEnoughEMCError.class`

`cool.furry.mc.forge.projectexpansion.events.RenderingEvent` methods:
- `public static void registerRenderers(net.minecraftforge.client.event.EntityRenderersEvent$RegisterRenderers);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerRenderers$1(net.minecraft.world.item.DyeColor, net.minecraftforge.registries.RegistryObject, net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static net.minecraftforge.registries.RegistryObject lambda$registerRenderers$0(net.minecraftforge.registries.RegistryObject);`

`cool.furry.mc.forge.projectexpansion.item.ItemKnowledgeSharingBook` methods:
- `public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> m_7203_(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand);`
- `public boolean m_5812_(net.minecraft.world.item.ItemStack);`
- `public void m_7373_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`

`cool.furry.mc.forge.projectexpansion.rendering.ChestRenderer` methods:
- `public void m_6922_(BE, float, com.mojang.blaze3d.vertex.PoseStack, net.minecraft.client.renderer.MultiBufferSource, int, int);`
- `private static boolean lambda$new$0(java.util.function.Supplier, net.minecraft.world.level.block.Block);`

`cool.furry.mc.forge.projectexpansion.Main` methods:
- `private void serverTick(net.minecraftforge.event.TickEvent$ServerTickEvent);`
- `public static net.minecraft.resources.ResourceLocation rl(java.lang.String);`

`cool.furry.mc.forge.projectexpansion.block.BlockAdvancedAlchemicalChest$ContainerProvider` methods:
- `public net.minecraft.world.inventory.AbstractContainerMenu m_7208_(int, net.minecraft.world.entity.player.Inventory, net.minecraft.world.entity.player.Player);`
- `public net.minecraft.network.chat.Component m_5446_();`
- `public final java.lang.String toString();`
- `public final int hashCode();`
- `public final boolean equals(java.lang.Object);`
- `public cool.furry.mc.forge.projectexpansion.block.entity.BlockEntityAdvancedAlchemicalChest blockEntity();`
- `public net.minecraft.world.InteractionHand hand();`

`cool.furry.mc.forge.projectexpansion.block.BlockAdvancedAlchemicalChest` methods:
- `public net.minecraft.world.item.DyeColor getColor();`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.block.RenderShape m_7514_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public void m_6402_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.phys.shapes.VoxelShape m_5940_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.phys.shapes.CollisionContext);`
- `public void m_213897_(net.minecraft.world.level.block.state.BlockState, net.minecraft.server.level.ServerLevel, net.minecraft.core.BlockPos, net.minecraft.util.RandomSource);`
- `protected void m_7926_(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `public boolean m_7357_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.pathfinder.PathComputationType);`
- `public boolean m_7278_(net.minecraft.world.level.block.state.BlockState);`
- `public int m_6782_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public net.minecraft.world.level.block.state.BlockState m_5573_(net.minecraft.world.item.context.BlockPlaceContext);`
- `public net.minecraft.world.level.material.FluidState m_5888_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.block.state.BlockState m_7417_(net.minecraft.world.level.block.state.BlockState, net.minecraft.core.Direction, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.LevelAccessor, net.minecraft.core.BlockPos, net.minecraft.core.BlockPos);`
- `public boolean m_8133_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int, int);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `private static void lambda$use$1(net.minecraft.world.InteractionHand, net.minecraft.world.entity.player.Player, net.minecraft.core.BlockPos, net.minecraft.network.FriendlyByteBuf);`
- `private static int lambda$new$0(net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockCollector` methods:
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction getPistonPushReaction(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public boolean m_7278_(net.minecraft.world.level.block.state.BlockState);`
- `public int m_6782_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public void m_6810_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, boolean);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockCompactSun` methods:
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.material.PushReaction getPistonPushReaction(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `public void m_141947_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.Entity);`
- `public static boolean adjacent(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos);`
- `public static boolean adjacent(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.core.Direction);`
- `private static int lambda$new$0(net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockEMCLink` methods:
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public void m_6402_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `protected void m_7926_(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction getPistonPushReaction(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockMatter` methods:
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockPowerFlower` methods:
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.phys.shapes.VoxelShape m_5940_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.phys.shapes.CollisionContext);`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public void m_6402_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction getPistonPushReaction(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MapColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MapColor);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

## fabric-1.20.1 — artifacts

Version: `9.5.17`

Missing targets:
- `artifacts.effect.MagnetismMobEffect`

Highest-signal candidate classes:
- none

## fabric-1.20.1 — enderio

Version: `1.20.1-1.1`

Missing targets:
- `com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity`
- `com.enderio.enderio.content.tools.ElectromagnetItem`

Highest-signal candidate classes:
- none

## forge-1.19.2 — artifacts

Version: `5.0.6+forge`

Missing targets:
- `artifacts.effect.MagnetismMobEffect`

Highest-signal candidate classes:
- `artifacts/common/item/curio/belt/UniversalAttractorItem.class`
- `artifacts/data/LootModifiers$Builder.class`

`artifacts.common.item.curio.belt.UniversalAttractorItem` methods:
- `private void onItemPickup(net.minecraftforge.event.entity.player.PlayerEvent$ItemPickupEvent, net.minecraft.world.entity.LivingEntity);`
- `public static int getCooldown(net.minecraft.world.item.ItemStack);`
- `public static void setCooldown(net.minecraft.world.item.ItemStack, int);`
- `private void onItemToss(net.minecraftforge.event.entity.item.ItemTossEvent);`
- `public void curioTick(top.theillusivec4.curios.api.SlotContext, net.minecraft.world.item.ItemStack);`
- `private static void lambda$onItemToss$0(int, org.apache.commons.lang3.tuple.ImmutableTriple);`

`artifacts.data.LootModifiers$Builder` methods:
- `private artifacts.common.loot.RollLootTableModifier build();`
- `protected net.minecraft.world.level.storage.loot.LootTable$Builder createLootTable();`
- `public net.minecraft.world.level.storage.loot.parameters.LootContextParamSet getParameterSet();`
- `protected java.lang.String getName();`
- `private artifacts.data.LootModifiers$Builder parameterSet(net.minecraft.world.level.storage.loot.parameters.LootContextParamSet);`
- `private artifacts.data.LootModifiers$Builder lootPoolCondition(net.minecraft.world.level.storage.loot.predicates.LootItemCondition$Builder);`
- `private artifacts.data.LootModifiers$Builder lootModifierCondition(net.minecraft.world.level.storage.loot.predicates.LootItemCondition$Builder);`
- `private artifacts.data.LootModifiers$Builder item(net.minecraft.world.item.Item, int);`
- `private artifacts.data.LootModifiers$Builder item(net.minecraft.world.item.Item);`
- `private artifacts.data.LootModifiers$Builder artifact(int);`
- `private artifacts.data.LootModifiers$Builder drinkingHat(int);`
- `private artifacts.data.LootModifiers$Builder everlastingBeef();`

## forge-1.19.2 — projecte

Version: `1.19.2-1.1.1`

Missing targets:
- `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand`

Highest-signal candidate classes:
- `cool/furry/mc/forge/projectexpansion/events/RenderingEvent.class`
- `cool/furry/mc/forge/projectexpansion/item/ItemKnowledgeSharingBook.class`
- `cool/furry/mc/forge/projectexpansion/rendering/ChestRenderer.class`
- `cool/furry/mc/forge/projectexpansion/rendering/package-info.class`
- `cool/furry/mc/forge/projectexpansion/Main$1.class`
- `cool/furry/mc/forge/projectexpansion/Main.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockAdvancedAlchemicalChest$ContainerProvider.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockAdvancedAlchemicalChest.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockCollector.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockCompactSun.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockEMCLink.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockMatter.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockPowerFlower.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockRelay.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockTransmutationInterface.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityAdvancedAlchemicalChest.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityCollector$CollectorItemHandlerProvider.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityCollector.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMC$CompactableStackHandler.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMC.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMCLink.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityNBTFilterable.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityOwnable$ActivationType.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityOwnable.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityPowerFlower.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityRelay.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityTransmutationInterface.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$DimensionNotFoundError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$DuplicateNameError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$NameNotFoundError.class`

`cool.furry.mc.forge.projectexpansion.events.RenderingEvent` methods:
- `public static void registerRenderers(net.minecraftforge.client.event.EntityRenderersEvent$RegisterRenderers);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerRenderers$1(net.minecraft.world.item.DyeColor, net.minecraftforge.registries.RegistryObject, net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static net.minecraftforge.registries.RegistryObject lambda$registerRenderers$0(net.minecraftforge.registries.RegistryObject);`

`cool.furry.mc.forge.projectexpansion.item.ItemKnowledgeSharingBook` methods:
- `public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> m_7203_(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand);`
- `public boolean m_5812_(net.minecraft.world.item.ItemStack);`
- `public void m_7373_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`

`cool.furry.mc.forge.projectexpansion.rendering.ChestRenderer` methods:
- `public void m_6922_(BE, float, com.mojang.blaze3d.vertex.PoseStack, net.minecraft.client.renderer.MultiBufferSource, int, int);`
- `private static boolean lambda$new$0(java.util.function.Supplier, net.minecraft.world.level.block.Block);`

`cool.furry.mc.forge.projectexpansion.Main$1` methods:
- `public net.minecraft.world.item.ItemStack m_6976_();`

`cool.furry.mc.forge.projectexpansion.Main` methods:
- `private void serverTick(net.minecraftforge.event.TickEvent$ServerTickEvent);`
- `public static net.minecraft.resources.ResourceLocation rl(java.lang.String);`

`cool.furry.mc.forge.projectexpansion.block.BlockAdvancedAlchemicalChest$ContainerProvider` methods:
- `public net.minecraft.world.inventory.AbstractContainerMenu m_7208_(int, net.minecraft.world.entity.player.Inventory, net.minecraft.world.entity.player.Player);`
- `public net.minecraft.network.chat.Component m_5446_();`
- `public final java.lang.String toString();`
- `public final int hashCode();`
- `public final boolean equals(java.lang.Object);`
- `public cool.furry.mc.forge.projectexpansion.block.entity.BlockEntityAdvancedAlchemicalChest blockEntity();`
- `public net.minecraft.world.InteractionHand hand();`

`cool.furry.mc.forge.projectexpansion.block.BlockAdvancedAlchemicalChest` methods:
- `public net.minecraft.world.item.DyeColor getColor();`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.block.RenderShape m_7514_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public void m_6402_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.phys.shapes.VoxelShape m_5940_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.phys.shapes.CollisionContext);`
- `public void m_213897_(net.minecraft.world.level.block.state.BlockState, net.minecraft.server.level.ServerLevel, net.minecraft.core.BlockPos, net.minecraft.util.RandomSource);`
- `protected void m_7926_(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `public boolean m_7357_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.pathfinder.PathComputationType);`
- `public boolean m_7278_(net.minecraft.world.level.block.state.BlockState);`
- `public int m_6782_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public net.minecraft.world.level.block.state.BlockState m_5573_(net.minecraft.world.item.context.BlockPlaceContext);`
- `public net.minecraft.world.level.material.FluidState m_5888_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.block.state.BlockState m_7417_(net.minecraft.world.level.block.state.BlockState, net.minecraft.core.Direction, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.LevelAccessor, net.minecraft.core.BlockPos, net.minecraft.core.BlockPos);`
- `public boolean m_8133_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int, int);`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `private static void lambda$use$1(net.minecraft.world.InteractionHand, net.minecraft.world.entity.player.Player, net.minecraft.core.BlockPos, net.minecraft.network.FriendlyByteBuf);`
- `private static int lambda$new$0(net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockCollector` methods:
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction m_5537_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public boolean m_7278_(net.minecraft.world.level.block.state.BlockState);`
- `public int m_6782_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public void m_6810_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, boolean);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockCompactSun` methods:
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.material.PushReaction m_5537_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `public void m_141947_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.Entity);`
- `public static boolean adjacent(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos);`
- `public static boolean adjacent(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.core.Direction);`
- `private static int lambda$new$0(net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockEMCLink` methods:
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public void m_6402_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `protected void m_7926_(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction m_5537_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockMatter` methods:
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

## forge-1.19.2 — reliquary

Version: `1.19.2-2.0.40.1198`

Missing targets:
- `reliquary.item.FortuneCoinItem`

Highest-signal candidate classes:
- `reliquary/items/FortuneCoinItem.class`
- `reliquary/compat/curios/CuriosFortuneCoinToggler.class`
- `reliquary/items/FortuneCoinToggler.class`
- `reliquary/network/PacketFortuneCoinTogglePressed.class`
- `reliquary/items/FortuneCoinItem$IFortuneCoinPickupChecker.class`
- `reliquary/network/PacketFortuneCoinTogglePressed$1.class`
- `reliquary/network/PacketFortuneCoinTogglePressed$InventoryType.class`
- `reliquary/reference/Settings$Common$ItemSettings$FortuneCoinSettings.class`
- `reliquary/handler/ClientEventHandler.class`
- `reliquary/init/ModItems.class`
- `reliquary/compat/curios/CuriosCompat.class`
- `reliquary/compat/jei/ItemDescriptionBuilder.class`
- `reliquary/data/ModRecipeProvider.class`
- `reliquary/network/PacketHandler.class`
- `reliquary/reference/Settings$Common$ItemSettings.class`
- `reliquary/reference/Settings.class`

`reliquary.items.FortuneCoinItem` methods:
- `public static void addFortuneCoinPickupChecker(reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker);`
- `public void onEquipped(java.lang.String, net.minecraft.world.entity.LivingEntity);`
- `public reliquary.items.util.ICuriosItem$Type getCuriosType();`
- `public void onWornTick(net.minecraft.world.item.ItemStack, net.minecraft.world.entity.LivingEntity);`
- `protected void addMoreInformation(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, reliquary.util.TooltipBuilder);`
- `protected boolean hasMoreInformation(net.minecraft.world.item.ItemStack);`
- `public net.minecraft.world.item.Rarity m_41460_(net.minecraft.world.item.ItemStack);`
- `public boolean m_5812_(net.minecraft.world.item.ItemStack);`
- `public static boolean isEnabled(net.minecraft.world.item.ItemStack);`
- `public void m_6883_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);`
- `private void scanForEntitiesInRange(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, double);`
- `private boolean canPickupItem(net.minecraft.world.entity.item.ItemEntity, java.util.List<net.minecraft.core.BlockPos>, boolean);`
- `private boolean isInDisabledRange(net.minecraft.world.entity.item.ItemEntity, java.util.List<net.minecraft.core.BlockPos>);`
- `private java.util.List<net.minecraft.core.BlockPos> getDisablePositions(net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `private void teleportEntityToPlayer(net.minecraft.world.entity.Entity, net.minecraft.world.entity.player.Player);`
- `private boolean checkForRoom(net.minecraft.world.item.ItemStack, net.minecraft.world.entity.player.Player);`
- `public void onUsingTick(net.minecraft.world.item.ItemStack, net.minecraft.world.entity.LivingEntity, int);`
- `private double getLongRangePullDistance();`
- `private double getStandardPullDistance();`
- `public int m_8105_(net.minecraft.world.item.ItemStack);`
- `public net.minecraft.world.item.UseAnim m_6164_(net.minecraft.world.item.ItemStack);`
- `public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> m_7203_(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand);`
- `public void update(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, reliquary.api.IPedestal);`
- `private void pickupItems(reliquary.api.IPedestal, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `private void pickupXp(reliquary.api.IPedestal, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public void onRemoved(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, reliquary.api.IPedestal);`
- `public void stop(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, reliquary.api.IPedestal);`
- `public void toggle(net.minecraft.world.item.ItemStack);`

`reliquary.compat.curios.CuriosFortuneCoinToggler` methods:
- `public boolean findAndToggle();`
- `public void registerSelf();`
- `private static java.lang.Boolean lambda$findAndToggle$1(top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static void lambda$findAndToggle$0(java.util.concurrent.atomic.AtomicBoolean, java.lang.String, top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`

`reliquary.items.FortuneCoinToggler` methods:
- `public static void setCoinToggler(reliquary.items.FortuneCoinToggler);`
- `public static void handleKeyInputEvent(net.minecraftforge.event.TickEvent$ClientTickEvent);`
- `public boolean findAndToggle();`

`reliquary.network.PacketFortuneCoinTogglePressed` methods:
- `static void encode(reliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.network.FriendlyByteBuf);`
- `static reliquary.network.PacketFortuneCoinTogglePressed decode(net.minecraft.network.FriendlyByteBuf);`
- `static void onMessage(reliquary.network.PacketFortuneCoinTogglePressed, java.util.function.Supplier<net.minecraftforge.network.NetworkEvent$Context>);`
- `private static void handleMessage(reliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.server.level.ServerPlayer);`
- `private static void showMessage(net.minecraft.server.level.ServerPlayer, net.minecraft.world.item.ItemStack);`
- `private static void run(java.util.function.Supplier<java.lang.Runnable>);`
- `private static java.lang.Runnable lambda$handleMessage$3(net.minecraft.server.level.ServerPlayer, reliquary.network.PacketFortuneCoinTogglePressed);`
- `private static void lambda$handleMessage$2(net.minecraft.server.level.ServerPlayer, reliquary.network.PacketFortuneCoinTogglePressed);`
- `private static void lambda$handleMessage$1(net.minecraft.server.level.ServerPlayer, reliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.world.item.ItemStack);`
- `private static void lambda$onMessage$0(reliquary.network.PacketFortuneCoinTogglePressed, net.minecraftforge.network.NetworkEvent$Context);`

`reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker` methods:
- `public abstract boolean canPickup(net.minecraft.world.entity.item.ItemEntity);`

`reliquary.network.PacketFortuneCoinTogglePressed$InventoryType` methods:
- `public static reliquary.network.PacketFortuneCoinTogglePressed$InventoryType[] values();`
- `public static reliquary.network.PacketFortuneCoinTogglePressed$InventoryType valueOf(java.lang.String);`
- `private static reliquary.network.PacketFortuneCoinTogglePressed$InventoryType[] $values();`

`reliquary.handler.ClientEventHandler` methods:
- `public static void registerHandlers();`
- `private static void onRenderLiving(net.minecraftforge.client.event.RenderLivingEvent$Pre<net.minecraft.world.entity.player.Player, net.minecraft.client.model.PlayerModel<net.minecraft.world.entity.player.Player>>);`
- `private static void registerLayer(net.minecraftforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions);`
- `private static void setHandgunArmPoses(net.minecraftforge.client.event.RenderLivingEvent$Pre<net.minecraft.world.entity.player.Player, net.minecraft.client.model.PlayerModel<net.minecraft.world.entity.player.Player>>, net.minecraft.world.entity.player.Player, boolean, boolean);`
- `private static net.minecraft.world.InteractionHand getActiveHandgunHand(net.minecraft.world.entity.player.Player, boolean, boolean);`
- `private static boolean isHandgunActive(net.minecraft.world.entity.player.Player, boolean, boolean);`
- `private static boolean isValidTimeFrame(net.minecraft.world.level.Level, net.minecraft.world.item.ItemStack);`
- `private static void registerOverlay(net.minecraftforge.client.event.RegisterGuiOverlaysEvent);`
- `private static void onMouseScrolled(net.minecraftforge.client.event.InputEvent$MouseScrollingEvent);`
- `private static void renderHUDComponents(com.mojang.blaze3d.vertex.PoseStack);`
- `private static void initHUDComponents();`
- `private static void registerEntityRenderers(net.minecraftforge.client.event.EntityRenderersEvent$RegisterRenderers);`
- `private static void clientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent);`
- `private static void registerBulletAndMagazineItemProperties();`
- `private static void registerVoidTearItemProperties();`
- `private static void registerInfernalTearItemProperties();`
- `private static void registerLyssaRodItemProperties();`
- `private static void registerKeyMappings(net.minecraftforge.client.event.RegisterKeyMappingsEvent);`
- `private static void registerPropertyToItems(net.minecraft.resources.ResourceLocation, net.minecraft.client.renderer.item.ItemPropertyFunction, net.minecraft.world.item.Item...);`
- `private static boolean isPotionAttached(net.minecraft.world.item.ItemStack);`
- `private static void loadComplete(net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent);`
- `private static void lambda$loadComplete$32();`
- `private static float lambda$registerLyssaRodItemProperties$31(net.minecraft.world.item.ItemStack, net.minecraft.client.multiplayer.ClientLevel, net.minecraft.world.entity.LivingEntity, int);`
- `private static float lambda$registerInfernalTearItemProperties$30(net.minecraft.world.item.ItemStack, net.minecraft.client.multiplayer.ClientLevel, net.minecraft.world.entity.LivingEntity, int);`
- `private static float lambda$registerVoidTearItemProperties$29(net.minecraft.world.item.ItemStack, net.minecraft.client.multiplayer.ClientLevel, net.minecraft.world.entity.LivingEntity, int);`
- `private static float lambda$registerBulletAndMagazineItemProperties$28(net.minecraft.world.item.ItemStack, net.minecraft.client.multiplayer.ClientLevel, net.minecraft.world.entity.LivingEntity, int);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerEntityRenderers$27(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerEntityRenderers$26(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerEntityRenderers$25(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static java.lang.String lambda$initHUDComponents$24(net.minecraft.world.item.ItemStack);`

`reliquary.init.ModItems` methods:
- `public static void registerContainers(net.minecraftforge.registries.RegisterEvent);`
- `public static void registerDispenseBehaviors();`
- `public static void registerRecipeSerializers(net.minecraftforge.registries.RegisterEvent);`
- `public static void registerHandgunMagazines();`
- `public static void registerListeners(net.minecraftforge.eventbus.api.IEventBus);`
- `private static void onResourceReload(net.minecraftforge.event.AddReloadListenerEvent);`
- `private static java.lang.Runnable lambda$registerContainers$30();`
- `private static void lambda$registerContainers$29();`
- `private static net.minecraft.world.item.crafting.RecipeSerializer lambda$static$28();`
- `private static net.minecraft.world.inventory.MenuType lambda$static$27();`
- `private static net.minecraft.world.inventory.MenuType lambda$static$26();`
- `private static reliquary.common.gui.AlkahestTomeMenu lambda$static$25(int, net.minecraft.world.entity.player.Inventory, net.minecraft.network.FriendlyByteBuf);`
- `private static reliquary.items.ItemBase lambda$static$24();`
- `private static reliquary.items.BulletItem lambda$static$23();`
- `private static reliquary.items.BulletItem lambda$static$22();`
- `private static reliquary.items.BulletItem lambda$static$21();`
- `private static reliquary.items.BulletItem lambda$static$20();`
- `private static reliquary.items.BulletItem lambda$static$19();`
- `private static reliquary.items.BulletItem lambda$static$18();`
- `private static reliquary.items.BulletItem lambda$static$17();`
- `private static reliquary.items.BulletItem lambda$static$16();`
- `private static reliquary.items.BulletItem lambda$static$15();`
- `private static reliquary.items.BulletItem lambda$static$14();`
- `private static reliquary.items.MagazineItem lambda$static$13();`
- `private static reliquary.items.MagazineItem lambda$static$12();`
- `private static reliquary.items.MagazineItem lambda$static$11();`
- `private static reliquary.items.MagazineItem lambda$static$10();`
- `private static reliquary.items.MagazineItem lambda$static$9();`
- `private static reliquary.items.MagazineItem lambda$static$8();`
- `private static reliquary.items.MagazineItem lambda$static$7();`

`reliquary.compat.curios.CuriosCompat` methods:
- `private void registerLayerDefinitions(net.minecraftforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions);`
- `private void sendImc(net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent);`
- `public void onAttachCapabilities(net.minecraftforge.event.AttachCapabilitiesEvent<net.minecraft.world.item.ItemStack>);`
- `private boolean isCuriosItem(net.minecraft.world.item.Item);`
- `private void addCuriosCapability(net.minecraftforge.event.AttachCapabilitiesEvent<net.minecraft.world.item.ItemStack>, net.minecraft.world.item.ItemStack);`
- `private void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent);`
- `public static java.util.Optional<net.minecraft.world.item.ItemStack> getStackInSlot(net.minecraft.world.entity.LivingEntity, java.lang.String, int);`
- `public static void setStackInSlot(net.minecraft.world.entity.LivingEntity, java.lang.String, int, net.minecraft.world.item.ItemStack);`
- `private static void lambda$setStackInSlot$12(java.lang.String, int, net.minecraft.world.item.ItemStack, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static void lambda$setStackInSlot$11(int, net.minecraft.world.item.ItemStack, top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`
- `private static java.util.Optional lambda$getStackInSlot$10(java.lang.String, int, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static net.minecraft.world.item.ItemStack lambda$getStackInSlot$9(int, top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`
- `private static net.minecraftforge.items.IItemHandler lambda$setup$8(net.minecraft.world.entity.player.Player, reliquary.items.util.ICuriosItem$Type);`
- `private static top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler lambda$setup$7(reliquary.items.util.ICuriosItem$Type, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static java.lang.Runnable lambda$setup$6();`
- `private static void lambda$setup$5();`
- `private static java.lang.Object lambda$sendImc$4();`
- `private static java.lang.Object lambda$sendImc$3();`
- `private static java.lang.Object lambda$sendImc$2();`
- `private java.lang.Runnable lambda$new$1(net.minecraftforge.eventbus.api.IEventBus);`
- `private void lambda$new$0(net.minecraftforge.eventbus.api.IEventBus);`

`reliquary.compat.jei.ItemDescriptionBuilder` methods:
- `public static void addIngredientInfo(mezz.jei.api.registration.IRecipeRegistration);`
- `private static void registerItemDescription(mezz.jei.api.registration.IRecipeRegistration, net.minecraft.world.item.Item);`
- `private static void addStacksIngredientInfo(mezz.jei.api.registration.IRecipeRegistration, net.minecraft.world.item.Item, java.util.List<net.minecraft.world.item.ItemStack>, java.lang.String...);`
- `private static void addStacksIngredientInfo(mezz.jei.api.registration.IRecipeRegistration, java.util.List<net.minecraft.world.item.ItemStack>, java.lang.String...);`
- `private static net.minecraft.network.chat.Component[] getTranslationKeys(java.lang.String...);`
- `private static void registerCharmFragmentItemsDescription(mezz.jei.api.registration.IRecipeRegistration);`
- `private static void registerCharmItemsDescription(mezz.jei.api.registration.IRecipeRegistration);`
- `private static void registerCharmBasedItems(mezz.jei.api.registration.IRecipeRegistration, net.minecraft.world.item.Item, java.util.function.Function<net.minecraft.world.item.ItemStack, net.minecraft.resources.ResourceLocation>);`
- `private static void registerPotionAmmoItemsDescription(mezz.jei.api.registration.IRecipeRegistration, net.minecraft.world.item.Item);`
- `private static net.minecraft.world.item.ItemStack lambda$addIngredientInfo$1(net.minecraftforge.registries.RegistryObject);`
- `private static net.minecraft.world.item.ItemStack lambda$addIngredientInfo$0(net.minecraftforge.registries.RegistryObject);`

## forge-1.18.2 — artifacts

Version: `1.0.0`

Missing targets:
- `artifacts.effect.MagnetismMobEffect`

Highest-signal candidate classes:
- none

## forge-1.18.2 — projecte

Version: `1.18.2-1.1.1`

Missing targets:
- `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand`

Highest-signal candidate classes:
- `cool/furry/mc/forge/projectexpansion/events/RenderingEvent.class`
- `cool/furry/mc/forge/projectexpansion/item/ItemKnowledgeSharingBook.class`
- `cool/furry/mc/forge/projectexpansion/rendering/ChestRenderer.class`
- `cool/furry/mc/forge/projectexpansion/rendering/package-info.class`
- `cool/furry/mc/forge/projectexpansion/Main$1.class`
- `cool/furry/mc/forge/projectexpansion/Main.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockAdvancedAlchemicalChest$ContainerProvider.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockAdvancedAlchemicalChest.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockCollector.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockCompactSun.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockEMCLink.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockMatter.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockPowerFlower.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockRelay.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockTransmutationInterface.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityAdvancedAlchemicalChest.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityCollector$CollectorItemHandlerProvider.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityCollector.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMC$CompactableStackHandler.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMC.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityEMCLink.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityNBTFilterable.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityOwnable$ActivationType.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityOwnable.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityPowerFlower.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityRelay.class`
- `cool/furry/mc/forge/projectexpansion/block/entity/BlockEntityTransmutationInterface.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$DimensionNotFoundError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$DuplicateNameError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$NameNotFoundError.class`

`cool.furry.mc.forge.projectexpansion.events.RenderingEvent` methods:
- `public static void registerRenderers(net.minecraftforge.client.event.EntityRenderersEvent$RegisterRenderers);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerRenderers$1(net.minecraft.world.item.DyeColor, net.minecraftforge.registries.RegistryObject, net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static net.minecraftforge.registries.RegistryObject lambda$registerRenderers$0(net.minecraftforge.registries.RegistryObject);`

`cool.furry.mc.forge.projectexpansion.item.ItemKnowledgeSharingBook` methods:
- `public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> m_7203_(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand);`
- `public boolean m_5812_(net.minecraft.world.item.ItemStack);`
- `public void m_7373_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`

`cool.furry.mc.forge.projectexpansion.rendering.ChestRenderer` methods:
- `public void m_6922_(BE, float, com.mojang.blaze3d.vertex.PoseStack, net.minecraft.client.renderer.MultiBufferSource, int, int);`
- `private static boolean lambda$new$0(java.util.function.Supplier, net.minecraft.world.level.block.Block);`

`cool.furry.mc.forge.projectexpansion.Main$1` methods:
- `public net.minecraft.world.item.ItemStack m_6976_();`

`cool.furry.mc.forge.projectexpansion.Main` methods:
- `private void registerCapabilities(net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent);`
- `private void serverTick(net.minecraftforge.event.TickEvent$ServerTickEvent);`
- `public static net.minecraft.resources.ResourceLocation rl(java.lang.String);`

`cool.furry.mc.forge.projectexpansion.block.BlockAdvancedAlchemicalChest$ContainerProvider` methods:
- `public net.minecraft.world.inventory.AbstractContainerMenu m_7208_(int, net.minecraft.world.entity.player.Inventory, net.minecraft.world.entity.player.Player);`
- `public net.minecraft.network.chat.Component m_5446_();`
- `public final java.lang.String toString();`
- `public final int hashCode();`
- `public final boolean equals(java.lang.Object);`
- `public cool.furry.mc.forge.projectexpansion.block.entity.BlockEntityAdvancedAlchemicalChest blockEntity();`
- `public net.minecraft.world.InteractionHand hand();`

`cool.furry.mc.forge.projectexpansion.block.BlockAdvancedAlchemicalChest` methods:
- `public net.minecraft.world.item.DyeColor getColor();`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.block.RenderShape m_7514_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public void m_6402_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.phys.shapes.VoxelShape m_5940_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.phys.shapes.CollisionContext);`
- `public void m_7458_(net.minecraft.world.level.block.state.BlockState, net.minecraft.server.level.ServerLevel, net.minecraft.core.BlockPos, java.util.Random);`
- `protected void m_7926_(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `public boolean m_7357_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.pathfinder.PathComputationType);`
- `public boolean m_7278_(net.minecraft.world.level.block.state.BlockState);`
- `public int m_6782_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public net.minecraft.world.level.block.state.BlockState m_5573_(net.minecraft.world.item.context.BlockPlaceContext);`
- `public net.minecraft.world.level.material.FluidState m_5888_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.block.state.BlockState m_7417_(net.minecraft.world.level.block.state.BlockState, net.minecraft.core.Direction, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.LevelAccessor, net.minecraft.core.BlockPos, net.minecraft.core.BlockPos);`
- `public boolean m_8133_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, int, int);`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `private static void lambda$use$1(net.minecraft.world.InteractionHand, net.minecraft.world.entity.player.Player, net.minecraft.core.BlockPos, net.minecraft.network.FriendlyByteBuf);`
- `private static int lambda$new$0(net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockCollector` methods:
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction m_5537_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public boolean m_7278_(net.minecraft.world.level.block.state.BlockState);`
- `public int m_6782_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public void m_6810_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, boolean);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockCompactSun` methods:
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.level.material.PushReaction m_5537_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `public void m_141947_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.Entity);`
- `public static boolean adjacent(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos);`
- `public static boolean adjacent(net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.core.Direction);`
- `private static int lambda$new$0(net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockEMCLink` methods:
- `public net.minecraft.world.level.block.entity.BlockEntity m_142194_(net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState);`
- `public void m_5871_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.BlockGetter, java.util.List<net.minecraft.network.chat.Component>, net.minecraft.world.item.TooltipFlag);`
- `public net.minecraft.world.InteractionResult m_6227_(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand, net.minecraft.world.phys.BlockHitResult);`
- `public void m_6402_(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.entity.LivingEntity, net.minecraft.world.item.ItemStack);`
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `protected void m_7926_(net.minecraft.world.level.block.state.StateDefinition$Builder<net.minecraft.world.level.block.Block, net.minecraft.world.level.block.state.BlockState>);`
- `public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> m_142354_(net.minecraft.world.level.Level, net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.block.entity.BlockEntityType<T>);`
- `public net.minecraft.world.level.material.PushReaction m_5537_(net.minecraft.world.level.block.state.BlockState);`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockMatter` methods:
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public net.minecraft.world.level.material.MaterialColor getMapColor(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.BlockGetter, net.minecraft.core.BlockPos, net.minecraft.world.level.material.MaterialColor);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.world.level.block.state.BlockState);`

## forge-1.18.2 — reliquary

Version: `1.18.2-2.0.19.1161`

Missing targets:
- `reliquary.item.FortuneCoinItem`

Highest-signal candidate classes:
- `reliquary/items/FortuneCoinItem.class`
- `reliquary/compat/curios/CuriosFortuneCoinToggler.class`
- `reliquary/items/FortuneCoinToggler.class`
- `reliquary/network/PacketFortuneCoinTogglePressed.class`
- `reliquary/items/FortuneCoinItem$IFortuneCoinPickupChecker.class`
- `reliquary/network/PacketFortuneCoinTogglePressed$1.class`
- `reliquary/network/PacketFortuneCoinTogglePressed$InventoryType.class`
- `reliquary/reference/Settings$Common$ItemSettings$FortuneCoinSettings.class`
- `reliquary/handler/ClientEventHandler.class`
- `reliquary/init/ModItems.class`
- `reliquary/compat/curios/CuriosCompat.class`
- `reliquary/compat/jei/ItemDescriptionBuilder.class`
- `reliquary/data/ModRecipeProvider.class`
- `reliquary/network/PacketHandler.class`
- `reliquary/reference/Settings$Common$ItemSettings.class`
- `reliquary/reference/Settings.class`

`reliquary.items.FortuneCoinItem` methods:
- `public static void addFortuneCoinPickupChecker(reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker);`
- `public void onEquipped(java.lang.String, net.minecraft.world.entity.LivingEntity);`
- `public reliquary.items.util.ICuriosItem$Type getCuriosType();`
- `public void onWornTick(net.minecraft.world.item.ItemStack, net.minecraft.world.entity.LivingEntity);`
- `protected void addMoreInformation(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, java.util.List<net.minecraft.network.chat.Component>);`
- `protected boolean hasMoreInformation(net.minecraft.world.item.ItemStack);`
- `public net.minecraft.world.item.Rarity m_41460_(net.minecraft.world.item.ItemStack);`
- `public boolean m_5812_(net.minecraft.world.item.ItemStack);`
- `public static boolean isEnabled(net.minecraft.world.item.ItemStack);`
- `public void m_6883_(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, net.minecraft.world.entity.Entity, int, boolean);`
- `private void scanForEntitiesInRange(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, double);`
- `private boolean canPickupItem(net.minecraft.world.entity.item.ItemEntity, java.util.List<net.minecraft.core.BlockPos>, boolean);`
- `private boolean isInDisabledRange(net.minecraft.world.entity.item.ItemEntity, java.util.List<net.minecraft.core.BlockPos>);`
- `private java.util.List<net.minecraft.core.BlockPos> getDisablePositions(net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `private void teleportEntityToPlayer(net.minecraft.world.entity.Entity, net.minecraft.world.entity.player.Player);`
- `private boolean checkForRoom(net.minecraft.world.item.ItemStack, net.minecraft.world.entity.player.Player);`
- `public void onUsingTick(net.minecraft.world.item.ItemStack, net.minecraft.world.entity.LivingEntity, int);`
- `private double getLongRangePullDistance();`
- `private double getStandardPullDistance();`
- `public int m_8105_(net.minecraft.world.item.ItemStack);`
- `public net.minecraft.world.item.UseAnim m_6164_(net.minecraft.world.item.ItemStack);`
- `public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> m_7203_(net.minecraft.world.level.Level, net.minecraft.world.entity.player.Player, net.minecraft.world.InteractionHand);`
- `public void update(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, reliquary.api.IPedestal);`
- `private void pickupItems(reliquary.api.IPedestal, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `private void pickupXp(reliquary.api.IPedestal, net.minecraft.world.level.Level, net.minecraft.core.BlockPos);`
- `public void onRemoved(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, reliquary.api.IPedestal);`
- `public void stop(net.minecraft.world.item.ItemStack, net.minecraft.world.level.Level, reliquary.api.IPedestal);`
- `public void toggle(net.minecraft.world.item.ItemStack);`

`reliquary.compat.curios.CuriosFortuneCoinToggler` methods:
- `public boolean findAndToggle();`
- `public void registerSelf();`
- `private static java.lang.Boolean lambda$findAndToggle$1(top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static java.lang.Boolean lambda$findAndToggle$0(top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`

`reliquary.items.FortuneCoinToggler` methods:
- `public static void setCoinToggler(reliquary.items.FortuneCoinToggler);`
- `public static void handleKeyInputEvent(net.minecraftforge.event.TickEvent$ClientTickEvent);`
- `public boolean findAndToggle();`

`reliquary.network.PacketFortuneCoinTogglePressed` methods:
- `static void encode(reliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.network.FriendlyByteBuf);`
- `static reliquary.network.PacketFortuneCoinTogglePressed decode(net.minecraft.network.FriendlyByteBuf);`
- `static void onMessage(reliquary.network.PacketFortuneCoinTogglePressed, java.util.function.Supplier<net.minecraftforge.network.NetworkEvent$Context>);`
- `private static void handleMessage(reliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.server.level.ServerPlayer);`
- `private static void showMessage(net.minecraft.server.level.ServerPlayer, net.minecraft.world.item.ItemStack);`
- `private static void run(java.util.function.Supplier<java.lang.Runnable>);`
- `private static java.lang.Runnable lambda$handleMessage$3(net.minecraft.server.level.ServerPlayer, reliquary.network.PacketFortuneCoinTogglePressed);`
- `private static void lambda$handleMessage$2(net.minecraft.server.level.ServerPlayer, reliquary.network.PacketFortuneCoinTogglePressed);`
- `private static void lambda$handleMessage$1(net.minecraft.server.level.ServerPlayer, reliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.world.item.ItemStack);`
- `private static void lambda$onMessage$0(reliquary.network.PacketFortuneCoinTogglePressed, net.minecraftforge.network.NetworkEvent$Context);`

`reliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker` methods:
- `public abstract boolean canPickup(net.minecraft.world.entity.item.ItemEntity);`

`reliquary.network.PacketFortuneCoinTogglePressed$InventoryType` methods:
- `public static reliquary.network.PacketFortuneCoinTogglePressed$InventoryType[] values();`
- `public static reliquary.network.PacketFortuneCoinTogglePressed$InventoryType valueOf(java.lang.String);`
- `private static reliquary.network.PacketFortuneCoinTogglePressed$InventoryType[] $values();`

`reliquary.handler.ClientEventHandler` methods:
- `public static void registerHandlers();`
- `private static void onRenderLiving(net.minecraftforge.client.event.RenderLivingEvent$Pre<net.minecraft.world.entity.player.Player, net.minecraft.client.model.PlayerModel<net.minecraft.world.entity.player.Player>>);`
- `private static void registerLayer(net.minecraftforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions);`
- `private static void setHandgunArmPoses(net.minecraftforge.client.event.RenderLivingEvent$Pre<net.minecraft.world.entity.player.Player, net.minecraft.client.model.PlayerModel<net.minecraft.world.entity.player.Player>>, net.minecraft.world.entity.player.Player, boolean, boolean);`
- `private static net.minecraft.world.InteractionHand getActiveHandgunHand(net.minecraft.world.entity.player.Player, boolean, boolean);`
- `private static boolean isHandgunActive(net.minecraft.world.entity.player.Player, boolean, boolean);`
- `private static boolean isValidTimeFrame(net.minecraft.world.level.Level, net.minecraft.world.item.ItemStack);`
- `private static void onRenderTick(net.minecraftforge.event.TickEvent$RenderTickEvent);`
- `private static void onMouseScrolled(net.minecraftforge.client.event.InputEvent$MouseScrollEvent);`
- `private static void renderHUDComponents(com.mojang.blaze3d.vertex.PoseStack);`
- `private static void initHUDComponents();`
- `private static void registerEntityRenderers(net.minecraftforge.client.event.EntityRenderersEvent$RegisterRenderers);`
- `private static void clientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent);`
- `private static void registerPropertyToItems(net.minecraft.resources.ResourceLocation, net.minecraft.client.renderer.item.ItemPropertyFunction, net.minecraft.world.item.Item...);`
- `private static boolean isPotionAttached(net.minecraft.world.item.ItemStack);`
- `private static void loadComplete(net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent);`
- `private static void lambda$loadComplete$36();`
- `private static void lambda$clientSetup$35();`
- `private static float lambda$clientSetup$34(net.minecraft.world.item.ItemStack, net.minecraft.client.multiplayer.ClientLevel, net.minecraft.world.entity.LivingEntity, int);`
- `private static void lambda$clientSetup$33();`
- `private static float lambda$clientSetup$32(net.minecraft.world.item.ItemStack, net.minecraft.client.multiplayer.ClientLevel, net.minecraft.world.entity.LivingEntity, int);`
- `private static void lambda$clientSetup$31();`
- `private static float lambda$clientSetup$30(net.minecraft.world.item.ItemStack, net.minecraft.client.multiplayer.ClientLevel, net.minecraft.world.entity.LivingEntity, int);`
- `private static void lambda$clientSetup$29();`
- `private static float lambda$clientSetup$28(net.minecraft.world.item.ItemStack, net.minecraft.client.multiplayer.ClientLevel, net.minecraft.world.entity.LivingEntity, int);`
- `private static void lambda$clientSetup$27();`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerEntityRenderers$26(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerEntityRenderers$25(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static net.minecraft.client.renderer.blockentity.BlockEntityRenderer lambda$registerEntityRenderers$24(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context);`
- `private static java.lang.String lambda$initHUDComponents$23(net.minecraft.world.item.ItemStack);`

`reliquary.init.ModItems` methods:
- `public static void registerContainers(net.minecraftforge.event.RegistryEvent$Register<net.minecraft.world.inventory.MenuType<?>>);`
- `public static void registerDispenseBehaviors();`
- `public static void registerRecipeSerializers(net.minecraftforge.event.RegistryEvent$Register<net.minecraft.world.item.crafting.RecipeSerializer<?>>);`
- `public static void registerHandgunMagazines();`
- `public static boolean isEnabled(net.minecraft.world.item.Item...);`
- `public static void registerListeners(net.minecraftforge.eventbus.api.IEventBus);`
- `private static java.lang.Runnable lambda$registerContainers$29();`
- `private static void lambda$registerContainers$28();`
- `private static net.minecraft.world.inventory.MenuType lambda$static$27();`
- `private static net.minecraft.world.inventory.MenuType lambda$static$26();`
- `private static reliquary.common.gui.AlkahestTomeMenu lambda$static$25(int, net.minecraft.world.entity.player.Inventory, net.minecraft.network.FriendlyByteBuf);`
- `private static reliquary.items.ItemBase lambda$static$24();`
- `private static reliquary.items.BulletItem lambda$static$23();`
- `private static reliquary.items.BulletItem lambda$static$22();`
- `private static reliquary.items.BulletItem lambda$static$21();`
- `private static reliquary.items.BulletItem lambda$static$20();`
- `private static reliquary.items.BulletItem lambda$static$19();`
- `private static reliquary.items.BulletItem lambda$static$18();`
- `private static reliquary.items.BulletItem lambda$static$17();`
- `private static reliquary.items.BulletItem lambda$static$16();`
- `private static reliquary.items.BulletItem lambda$static$15();`
- `private static reliquary.items.BulletItem lambda$static$14();`
- `private static reliquary.items.MagazineItem lambda$static$13();`
- `private static reliquary.items.MagazineItem lambda$static$12();`
- `private static reliquary.items.MagazineItem lambda$static$11();`
- `private static reliquary.items.MagazineItem lambda$static$10();`
- `private static reliquary.items.MagazineItem lambda$static$9();`
- `private static reliquary.items.MagazineItem lambda$static$8();`
- `private static reliquary.items.MagazineItem lambda$static$7();`
- `private static reliquary.items.MagazineItem lambda$static$6();`

`reliquary.compat.curios.CuriosCompat` methods:
- `private void registerLayerDefinitions(net.minecraftforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions);`
- `private void sendImc(net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent);`
- `public void onAttachCapabilities(net.minecraftforge.event.AttachCapabilitiesEvent<net.minecraft.world.item.ItemStack>);`
- `private boolean isCuriosItem(net.minecraft.world.item.Item);`
- `private void addCuriosCapability(net.minecraftforge.event.AttachCapabilitiesEvent<net.minecraft.world.item.ItemStack>, net.minecraft.world.item.ItemStack);`
- `private void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent);`
- `public static java.util.Optional<net.minecraft.world.item.ItemStack> getStackInSlot(net.minecraft.world.entity.LivingEntity, java.lang.String, int);`
- `public static void setStackInSlot(net.minecraft.world.entity.LivingEntity, java.lang.String, int, net.minecraft.world.item.ItemStack);`
- `private static void lambda$setStackInSlot$12(java.lang.String, int, net.minecraft.world.item.ItemStack, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static void lambda$setStackInSlot$11(int, net.minecraft.world.item.ItemStack, top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`
- `private static java.util.Optional lambda$getStackInSlot$10(java.lang.String, int, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static net.minecraft.world.item.ItemStack lambda$getStackInSlot$9(int, top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`
- `private static net.minecraftforge.items.IItemHandler lambda$setup$8(net.minecraft.world.entity.player.Player, reliquary.items.util.ICuriosItem$Type);`
- `private static top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler lambda$setup$7(reliquary.items.util.ICuriosItem$Type, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static java.lang.Runnable lambda$setup$6();`
- `private static void lambda$setup$5();`
- `private static java.lang.Object lambda$sendImc$4();`
- `private static java.lang.Object lambda$sendImc$3();`
- `private static java.lang.Object lambda$sendImc$2();`
- `private java.lang.Runnable lambda$new$1(net.minecraftforge.eventbus.api.IEventBus);`
- `private void lambda$new$0(net.minecraftforge.eventbus.api.IEventBus);`

`reliquary.compat.jei.ItemDescriptionBuilder` methods:
- `public static void addIngredientInfo(mezz.jei.api.registration.IRecipeRegistration);`
- `private static void registerItemDescription(mezz.jei.api.registration.IRecipeRegistration, net.minecraft.world.item.Item);`
- `private static void addStacksIngredientInfo(mezz.jei.api.registration.IRecipeRegistration, net.minecraft.world.item.Item, java.util.List<net.minecraft.world.item.ItemStack>, java.lang.String...);`
- `private static void addStacksIngredientInfo(mezz.jei.api.registration.IRecipeRegistration, java.util.List<net.minecraft.world.item.ItemStack>, java.lang.String...);`
- `private static net.minecraft.network.chat.Component[] getTranslationKeys(java.lang.String...);`
- `private static void registerCharmFragmentItemsDescription(mezz.jei.api.registration.IRecipeRegistration);`
- `private static void registerCharmItemsDescription(mezz.jei.api.registration.IRecipeRegistration);`
- `private static void registerCharmBasedItems(mezz.jei.api.registration.IRecipeRegistration, net.minecraft.world.item.Item, java.util.function.Function<net.minecraft.world.item.ItemStack, net.minecraft.resources.ResourceLocation>);`
- `private static void registerPotionAmmoItemsDescription(mezz.jei.api.registration.IRecipeRegistration, net.minecraft.world.item.Item);`
- `private static net.minecraft.world.item.ItemStack lambda$addIngredientInfo$1(net.minecraftforge.registries.RegistryObject);`
- `private static net.minecraft.world.item.ItemStack lambda$addIngredientInfo$0(net.minecraftforge.registries.RegistryObject);`

## forge-1.16.5 — artifacts

Version: `1.0.0`

Missing targets:
- `artifacts.effect.MagnetismMobEffect`

Highest-signal candidate classes:
- none

## forge-1.16.5 — projecte

Version: `1.16.5-1.0.41`

Missing targets:
- `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand`

Highest-signal candidate classes:
- `cool/furry/mc/forge/projectexpansion/events/RenderingEvent.class`
- `cool/furry/mc/forge/projectexpansion/item/ItemKnowledgeSharingBook.class`
- `cool/furry/mc/forge/projectexpansion/rendering/ChestRenderer.class`
- `cool/furry/mc/forge/projectexpansion/rendering/package-info.class`
- `cool/furry/mc/forge/projectexpansion/Main$1.class`
- `cool/furry/mc/forge/projectexpansion/Main.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockAdvancedAlchemicalChest.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockCollector.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockEMCLink.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockPowerFlower.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockRelay.class`
- `cool/furry/mc/forge/projectexpansion/block/BlockTransmutationInterface.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$DimensionNotFoundError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$DuplicateNameError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$NameNotFoundError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$NotEnoughEMCError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$OwnerOfflineError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError$Type.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$BookError.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$Provider.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations$TeleportLocation.class`
- `cool/furry/mc/forge/projectexpansion/capability/CapabilityAlchemicalBookLocations.class`
- `cool/furry/mc/forge/projectexpansion/capability/IAlchemicalBookLocationsProvider.class`
- `cool/furry/mc/forge/projectexpansion/commands/CommandBook$BookTarget.class`
- `cool/furry/mc/forge/projectexpansion/commands/CommandBook.class`
- `cool/furry/mc/forge/projectexpansion/commands/CommandEMC$ActionType.class`
- `cool/furry/mc/forge/projectexpansion/commands/CommandEMC.class`
- `cool/furry/mc/forge/projectexpansion/commands/CommandKnowledge$ActionType.class`
- `cool/furry/mc/forge/projectexpansion/commands/CommandKnowledge.class`
- `cool/furry/mc/forge/projectexpansion/commands/CommandRegistry.class`

`cool.furry.mc.forge.projectexpansion.events.RenderingEvent` methods:
- `public static void clientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent);`
- `private static net.minecraft.client.renderer.tileentity.TileEntityRenderer lambda$clientSetup$3(net.minecraft.item.DyeColor, net.minecraftforge.fml.RegistryObject, net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher);`
- `private static boolean lambda$null$2(net.minecraftforge.fml.RegistryObject, net.minecraft.block.Block);`
- `private static java.util.function.BiFunction lambda$clientSetup$1();`
- `private static net.minecraft.client.gui.screen.Screen lambda$null$0(net.minecraft.client.Minecraft, net.minecraft.client.gui.screen.Screen);`

`cool.furry.mc.forge.projectexpansion.item.ItemKnowledgeSharingBook` methods:
- `public net.minecraft.util.ActionResult<net.minecraft.item.ItemStack> func_77659_a(net.minecraft.world.World, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.Hand);`
- `public boolean func_77636_d(net.minecraft.item.ItemStack);`
- `public void func_77624_a(net.minecraft.item.ItemStack, net.minecraft.world.World, java.util.List<net.minecraft.util.text.ITextComponent>, net.minecraft.client.util.ITooltipFlag);`

`cool.furry.mc.forge.projectexpansion.rendering.ChestRenderer` methods:
- `public void render(cool.furry.mc.forge.projectexpansion.tile.TileAdvancedAlchemicalChest, float, com.mojang.blaze3d.matrix.MatrixStack, net.minecraft.client.renderer.IRenderTypeBuffer, int, int);`
- `public void func_225616_a_(net.minecraft.tileentity.TileEntity, float, com.mojang.blaze3d.matrix.MatrixStack, net.minecraft.client.renderer.IRenderTypeBuffer, int, int);`

`cool.furry.mc.forge.projectexpansion.Main$1` methods:
- `public net.minecraft.item.ItemStack func_78016_d();`

`cool.furry.mc.forge.projectexpansion.Main` methods:
- `private void commonSetup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent);`
- `private void serverTick(net.minecraftforge.event.TickEvent$ServerTickEvent);`
- `public static net.minecraft.util.ResourceLocation rl(java.lang.String);`

`cool.furry.mc.forge.projectexpansion.block.BlockAdvancedAlchemicalChest` methods:
- `public net.minecraft.item.DyeColor getColor();`
- `public void func_190948_a(net.minecraft.item.ItemStack, net.minecraft.world.IBlockReader, java.util.List<net.minecraft.util.text.ITextComponent>, net.minecraft.client.util.ITooltipFlag);`
- `public boolean hasTileEntity(net.minecraft.block.BlockState);`
- `public net.minecraft.tileentity.TileEntity createTileEntity(net.minecraft.block.BlockState, net.minecraft.world.IBlockReader);`
- `public net.minecraft.util.ActionResultType func_225533_a_(net.minecraft.block.BlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.Hand, net.minecraft.util.math.BlockRayTraceResult);`
- `public void func_180633_a(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.BlockState, net.minecraft.entity.LivingEntity, net.minecraft.item.ItemStack);`
- `public net.minecraft.util.math.shapes.VoxelShape func_220053_a(net.minecraft.block.BlockState, net.minecraft.world.IBlockReader, net.minecraft.util.math.BlockPos, net.minecraft.util.math.shapes.ISelectionContext);`
- `public net.minecraft.block.BlockRenderType func_149645_b(net.minecraft.block.BlockState);`
- `public int func_180641_l(net.minecraft.block.BlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos);`
- `public net.minecraft.block.BlockState func_196258_a(net.minecraft.item.BlockItemUseContext);`
- `public net.minecraft.fluid.FluidState func_204507_t(net.minecraft.block.BlockState);`
- `public net.minecraft.block.BlockState func_196271_a(net.minecraft.block.BlockState, net.minecraft.util.Direction, net.minecraft.block.BlockState, net.minecraft.world.IWorld, net.minecraft.util.math.BlockPos, net.minecraft.util.math.BlockPos);`
- `public boolean func_189539_a(net.minecraft.block.BlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, int, int);`
- `protected void func_206840_a(net.minecraft.state.StateContainer$Builder<net.minecraft.block.Block, net.minecraft.block.BlockState>);`
- `private static void lambda$use$1(net.minecraft.util.Hand, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.math.BlockPos, net.minecraft.network.PacketBuffer);`
- `private static int lambda$new$0(net.minecraft.block.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockCollector` methods:
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public boolean hasTileEntity(net.minecraft.block.BlockState);`
- `public net.minecraft.tileentity.TileEntity createTileEntity(net.minecraft.block.BlockState, net.minecraft.world.IBlockReader);`
- `public void func_190948_a(net.minecraft.item.ItemStack, net.minecraft.world.IBlockReader, java.util.List<net.minecraft.util.text.ITextComponent>, net.minecraft.client.util.ITooltipFlag);`
- `public net.minecraft.block.material.PushReaction func_149656_h(net.minecraft.block.BlockState);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.block.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockEMCLink` methods:
- `public boolean hasTileEntity(net.minecraft.block.BlockState);`
- `public net.minecraft.tileentity.TileEntity createTileEntity(net.minecraft.block.BlockState, net.minecraft.world.IBlockReader);`
- `public void func_190948_a(net.minecraft.item.ItemStack, net.minecraft.world.IBlockReader, java.util.List<net.minecraft.util.text.ITextComponent>, net.minecraft.client.util.ITooltipFlag);`
- `public net.minecraft.util.ActionResultType func_225533_a_(net.minecraft.block.BlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.Hand, net.minecraft.util.math.BlockRayTraceResult);`
- `public void func_180633_a(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.BlockState, net.minecraft.entity.LivingEntity, net.minecraft.item.ItemStack);`
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `protected void func_206840_a(net.minecraft.state.StateContainer$Builder<net.minecraft.block.Block, net.minecraft.block.BlockState>);`
- `public net.minecraft.block.material.PushReaction func_149656_h(net.minecraft.block.BlockState);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.block.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockPowerFlower` methods:
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public boolean hasTileEntity(net.minecraft.block.BlockState);`
- `public net.minecraft.tileentity.TileEntity createTileEntity(net.minecraft.block.BlockState, net.minecraft.world.IBlockReader);`
- `public net.minecraft.util.math.shapes.VoxelShape func_220053_a(net.minecraft.block.BlockState, net.minecraft.world.IBlockReader, net.minecraft.util.math.BlockPos, net.minecraft.util.math.shapes.ISelectionContext);`
- `public void func_190948_a(net.minecraft.item.ItemStack, net.minecraft.world.IBlockReader, java.util.List<net.minecraft.util.text.ITextComponent>, net.minecraft.client.util.ITooltipFlag);`
- `public net.minecraft.util.ActionResultType func_225533_a_(net.minecraft.block.BlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.Hand, net.minecraft.util.math.BlockRayTraceResult);`
- `public void func_180633_a(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.BlockState, net.minecraft.entity.LivingEntity, net.minecraft.item.ItemStack);`
- `public net.minecraft.block.material.PushReaction func_149656_h(net.minecraft.block.BlockState);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.block.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockRelay` methods:
- `public cool.furry.mc.forge.projectexpansion.util.Matter getMatter();`
- `public boolean hasTileEntity(net.minecraft.block.BlockState);`
- `public net.minecraft.tileentity.TileEntity createTileEntity(net.minecraft.block.BlockState, net.minecraft.world.IBlockReader);`
- `public void func_190948_a(net.minecraft.item.ItemStack, net.minecraft.world.IBlockReader, java.util.List<net.minecraft.util.text.ITextComponent>, net.minecraft.client.util.ITooltipFlag);`
- `public net.minecraft.block.material.PushReaction func_149656_h(net.minecraft.block.BlockState);`
- `private static int lambda$new$0(cool.furry.mc.forge.projectexpansion.util.Matter, net.minecraft.block.BlockState);`

`cool.furry.mc.forge.projectexpansion.block.BlockTransmutationInterface` methods:
- `public boolean hasTileEntity(net.minecraft.block.BlockState);`
- `public net.minecraft.tileentity.TileEntity createTileEntity(net.minecraft.block.BlockState, net.minecraft.world.IBlockReader);`
- `public void func_190948_a(net.minecraft.item.ItemStack, net.minecraft.world.IBlockReader, java.util.List<net.minecraft.util.text.ITextComponent>, net.minecraft.client.util.ITooltipFlag);`
- `public net.minecraft.util.ActionResultType func_225533_a_(net.minecraft.block.BlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.Hand, net.minecraft.util.math.BlockRayTraceResult);`
- `public void func_180633_a(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.BlockState, net.minecraft.entity.LivingEntity, net.minecraft.item.ItemStack);`
- `protected void func_206840_a(net.minecraft.state.StateContainer$Builder<net.minecraft.block.Block, net.minecraft.block.BlockState>);`
- `public net.minecraft.block.material.PushReaction func_149656_h(net.minecraft.block.BlockState);`
- `private static int lambda$new$0(net.minecraft.block.BlockState);`

## forge-1.16.5 — reliquary

Version: `1.16.5-1.3.5.1124`

Missing targets:
- `reliquary.item.FortuneCoinItem`

Highest-signal candidate classes:
- `xreliquary/items/FortuneCoinItem.class`
- `xreliquary/compat/curios/CuriosFortuneCoinToggler.class`
- `xreliquary/items/FortuneCoinToggler.class`
- `xreliquary/network/PacketFortuneCoinTogglePressed.class`
- `xreliquary/items/FortuneCoinItem$IFortuneCoinPickupChecker.class`
- `xreliquary/network/PacketFortuneCoinTogglePressed$1.class`
- `xreliquary/network/PacketFortuneCoinTogglePressed$InventoryType.class`
- `xreliquary/reference/Settings$Common$ItemSettings$FortuneCoinSettings.class`
- `xreliquary/client/ClientProxy.class`
- `xreliquary/init/ModItems.class`
- `xreliquary/compat/botania/BotaniaCompat.class`
- `xreliquary/compat/curios/CuriosCompat.class`
- `xreliquary/compat/jei/ItemDescriptionBuilder.class`
- `xreliquary/data/ModRecipeProvider.class`
- `xreliquary/network/PacketHandler.class`
- `xreliquary/reference/Settings$Common$ItemSettings.class`

`xreliquary.items.FortuneCoinItem` methods:
- `public static void addFortuneCoinPickupChecker(xreliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker);`
- `public void onEquipped(java.lang.String, net.minecraft.entity.LivingEntity);`
- `public xreliquary.items.util.IBaubleItem$Type getBaubleType();`
- `public void onWornTick(net.minecraft.item.ItemStack, net.minecraft.entity.LivingEntity);`
- `protected void addMoreInformation(net.minecraft.item.ItemStack, net.minecraft.world.World, java.util.List<net.minecraft.util.text.ITextComponent>);`
- `protected boolean hasMoreInformation(net.minecraft.item.ItemStack);`
- `public net.minecraft.item.Rarity func_77613_e(net.minecraft.item.ItemStack);`
- `public boolean func_77636_d(net.minecraft.item.ItemStack);`
- `public static boolean isEnabled(net.minecraft.item.ItemStack);`
- `public void func_77663_a(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.Entity, int, boolean);`
- `private void scanForEntitiesInRange(net.minecraft.world.World, net.minecraft.entity.player.PlayerEntity, double);`
- `private boolean canPickupItem(net.minecraft.entity.item.ItemEntity, java.util.List<net.minecraft.util.math.BlockPos>, boolean);`
- `private boolean isInDisabledRange(net.minecraft.entity.item.ItemEntity, java.util.List<net.minecraft.util.math.BlockPos>);`
- `private java.util.List<net.minecraft.util.math.BlockPos> getDisablePositions(net.minecraft.world.World, net.minecraft.util.math.BlockPos);`
- `private void teleportEntityToPlayer(net.minecraft.entity.Entity, net.minecraft.entity.player.PlayerEntity);`
- `private boolean checkForRoom(net.minecraft.item.ItemStack, net.minecraft.entity.player.PlayerEntity);`
- `public void onUsingTick(net.minecraft.item.ItemStack, net.minecraft.entity.LivingEntity, int);`
- `private double getLongRangePullDistance();`
- `private double getStandardPullDistance();`
- `public int func_77626_a(net.minecraft.item.ItemStack);`
- `public net.minecraft.item.UseAction func_77661_b(net.minecraft.item.ItemStack);`
- `public net.minecraft.util.ActionResult<net.minecraft.item.ItemStack> func_77659_a(net.minecraft.world.World, net.minecraft.entity.player.PlayerEntity, net.minecraft.util.Hand);`
- `public void update(net.minecraft.item.ItemStack, xreliquary.api.IPedestal);`
- `private void pickupItems(xreliquary.api.IPedestal, net.minecraft.world.World, net.minecraft.util.math.BlockPos);`
- `private void pickupXp(xreliquary.api.IPedestal, net.minecraft.world.World, net.minecraft.util.math.BlockPos);`
- `public void onRemoved(net.minecraft.item.ItemStack, xreliquary.api.IPedestal);`
- `public void stop(net.minecraft.item.ItemStack, xreliquary.api.IPedestal);`
- `public void toggle(net.minecraft.item.ItemStack);`

`xreliquary.compat.curios.CuriosFortuneCoinToggler` methods:
- `public boolean findAndToggle();`
- `public void registerSelf();`
- `private static java.lang.Boolean lambda$findAndToggle$1(top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static java.lang.Boolean lambda$findAndToggle$0(top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`

`xreliquary.items.FortuneCoinToggler` methods:
- `public static void setCoinToggler(xreliquary.items.FortuneCoinToggler);`
- `public static void handleKeyInputEvent(net.minecraftforge.event.TickEvent$ClientTickEvent);`
- `public boolean findAndToggle();`

`xreliquary.network.PacketFortuneCoinTogglePressed` methods:
- `static void encode(xreliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.network.PacketBuffer);`
- `static xreliquary.network.PacketFortuneCoinTogglePressed decode(net.minecraft.network.PacketBuffer);`
- `static void onMessage(xreliquary.network.PacketFortuneCoinTogglePressed, java.util.function.Supplier<net.minecraftforge.fml.network.NetworkEvent$Context>);`
- `private static void handleMessage(xreliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.entity.player.ServerPlayerEntity);`
- `private static void showMessage(net.minecraft.entity.player.ServerPlayerEntity, net.minecraft.item.ItemStack);`
- `private static void run(java.util.function.Supplier<java.lang.Runnable>);`
- `private static java.lang.Runnable lambda$handleMessage$3(net.minecraft.entity.player.ServerPlayerEntity, xreliquary.network.PacketFortuneCoinTogglePressed);`
- `private static void lambda$handleMessage$2(net.minecraft.entity.player.ServerPlayerEntity, xreliquary.network.PacketFortuneCoinTogglePressed);`
- `private static void lambda$handleMessage$1(net.minecraft.entity.player.ServerPlayerEntity, xreliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.item.ItemStack);`
- `private static void lambda$onMessage$0(xreliquary.network.PacketFortuneCoinTogglePressed, net.minecraftforge.fml.network.NetworkEvent$Context);`

`xreliquary.items.FortuneCoinItem$IFortuneCoinPickupChecker` methods:
- `public abstract boolean canPickup(net.minecraft.entity.item.ItemEntity);`

`xreliquary.network.PacketFortuneCoinTogglePressed$InventoryType` methods:
- `public static xreliquary.network.PacketFortuneCoinTogglePressed$InventoryType[] values();`
- `public static xreliquary.network.PacketFortuneCoinTogglePressed$InventoryType valueOf(java.lang.String);`

`xreliquary.client.ClientProxy` methods:
- `public void registerHandlers();`
- `private void registerEntityRenderers(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent);`
- `private void clientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent);`
- `public void registerPropertyToItems(net.minecraft.util.ResourceLocation, net.minecraft.item.IItemPropertyGetter, net.minecraft.item.Item...);`
- `private boolean isPotionAttached(net.minecraft.item.ItemStack);`
- `private void loadComplete(net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent);`
- `private void registerTileRenderers(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent);`
- `private static void lambda$loadComplete$17();`
- `private void lambda$clientSetup$16();`
- `private float lambda$clientSetup$15(net.minecraft.item.ItemStack, net.minecraft.client.world.ClientWorld, net.minecraft.entity.LivingEntity);`
- `private static void lambda$clientSetup$14();`
- `private static float lambda$clientSetup$13(net.minecraft.item.ItemStack, net.minecraft.client.world.ClientWorld, net.minecraft.entity.LivingEntity);`
- `private static void lambda$clientSetup$12();`
- `private static float lambda$clientSetup$11(net.minecraft.item.ItemStack, net.minecraft.client.world.ClientWorld, net.minecraft.entity.LivingEntity);`
- `private static void lambda$clientSetup$10();`
- `private static float lambda$clientSetup$9(net.minecraft.item.ItemStack, net.minecraft.client.world.ClientWorld, net.minecraft.entity.LivingEntity);`
- `private static void lambda$clientSetup$8();`
- `private static net.minecraft.client.renderer.entity.EntityRenderer lambda$registerEntityRenderers$7(net.minecraft.client.renderer.entity.EntityRendererManager);`
- `private static net.minecraft.client.renderer.entity.EntityRenderer lambda$registerEntityRenderers$6(net.minecraft.client.renderer.entity.EntityRendererManager);`
- `private static net.minecraft.client.renderer.entity.EntityRenderer lambda$registerEntityRenderers$5(net.minecraft.client.renderer.entity.EntityRendererManager);`
- `private static net.minecraft.client.renderer.entity.EntityRenderer lambda$registerEntityRenderers$4(net.minecraft.client.renderer.entity.EntityRendererManager);`
- `private static net.minecraft.client.renderer.entity.EntityRenderer lambda$registerEntityRenderers$3(net.minecraft.client.renderer.entity.EntityRendererManager);`
- `private static net.minecraft.client.renderer.entity.EntityRenderer lambda$registerEntityRenderers$2(net.minecraft.client.renderer.entity.EntityRendererManager);`
- `private static net.minecraft.client.renderer.entity.EntityRenderer lambda$registerEntityRenderers$1(net.minecraft.client.renderer.entity.EntityRendererManager);`
- `private static net.minecraft.client.renderer.entity.EntityRenderer lambda$registerEntityRenderers$0(net.minecraft.client.renderer.entity.EntityRendererManager);`

`xreliquary.init.ModItems` methods:
- `public static void registerContainers(net.minecraftforge.event.RegistryEvent$Register<net.minecraft.inventory.container.ContainerType<?>>);`
- `public static void registerDispenseBehaviors();`
- `public static void registerRecipeSerializers(net.minecraftforge.event.RegistryEvent$Register<net.minecraft.item.crafting.IRecipeSerializer<?>>);`
- `public static void registerHandgunMagazines();`
- `public static boolean isEnabled(net.minecraft.item.Item...);`
- `public static void registerListeners(net.minecraftforge.eventbus.api.IEventBus);`
- `private static java.lang.Runnable lambda$registerContainers$29();`
- `private static void lambda$registerContainers$28();`
- `private static net.minecraft.inventory.container.ContainerType lambda$static$27();`
- `private static net.minecraft.inventory.container.ContainerType lambda$static$26();`
- `private static xreliquary.common.gui.ContainerAlkahestTome lambda$static$25(int, net.minecraft.entity.player.PlayerInventory, net.minecraft.network.PacketBuffer);`
- `private static xreliquary.items.ItemBase lambda$static$24();`
- `private static xreliquary.items.BulletItem lambda$static$23();`
- `private static xreliquary.items.BulletItem lambda$static$22();`
- `private static xreliquary.items.BulletItem lambda$static$21();`
- `private static xreliquary.items.BulletItem lambda$static$20();`
- `private static xreliquary.items.BulletItem lambda$static$19();`
- `private static xreliquary.items.BulletItem lambda$static$18();`
- `private static xreliquary.items.BulletItem lambda$static$17();`
- `private static xreliquary.items.BulletItem lambda$static$16();`
- `private static xreliquary.items.BulletItem lambda$static$15();`
- `private static xreliquary.items.BulletItem lambda$static$14();`
- `private static xreliquary.items.MagazineItem lambda$static$13();`
- `private static xreliquary.items.MagazineItem lambda$static$12();`
- `private static xreliquary.items.MagazineItem lambda$static$11();`
- `private static xreliquary.items.MagazineItem lambda$static$10();`
- `private static xreliquary.items.MagazineItem lambda$static$9();`
- `private static xreliquary.items.MagazineItem lambda$static$8();`
- `private static xreliquary.items.MagazineItem lambda$static$7();`
- `private static xreliquary.items.MagazineItem lambda$static$6();`

`xreliquary.compat.botania.BotaniaCompat` methods:
- `public void setup();`
- `private static boolean lambda$setup$0(net.minecraft.entity.item.ItemEntity);`

`xreliquary.compat.curios.CuriosCompat` methods:
- `private void sendImc(net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent);`
- `public void onAttachCapabilities(net.minecraftforge.event.AttachCapabilitiesEvent<net.minecraft.item.ItemStack>);`
- `public void setup();`
- `public static java.util.Optional<net.minecraft.item.ItemStack> getStackInSlot(net.minecraft.entity.LivingEntity, java.lang.String, int);`
- `public static void setStackInSlot(net.minecraft.entity.LivingEntity, java.lang.String, int, net.minecraft.item.ItemStack);`
- `private static void lambda$setStackInSlot$10(java.lang.String, int, net.minecraft.item.ItemStack, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static void lambda$setStackInSlot$9(int, net.minecraft.item.ItemStack, top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`
- `private static java.util.Optional lambda$getStackInSlot$8(java.lang.String, int, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static net.minecraft.item.ItemStack lambda$getStackInSlot$7(int, top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler);`
- `private static net.minecraftforge.items.IItemHandler lambda$setup$6(net.minecraft.entity.player.PlayerEntity, xreliquary.items.util.IBaubleItem$Type);`
- `private static top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler lambda$setup$5(xreliquary.items.util.IBaubleItem$Type, top.theillusivec4.curios.api.type.capability.ICuriosItemHandler);`
- `private static java.lang.Runnable lambda$setup$4();`
- `private static void lambda$setup$3();`
- `private static java.lang.Object lambda$sendImc$2();`
- `private static java.lang.Object lambda$sendImc$1();`
- `private static java.lang.Object lambda$sendImc$0();`

## forge-1.12.2 — enderio

Version: `5.3.72`

Missing targets:
- `com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity`
- `com.enderio.enderio.content.tools.ElectromagnetItem`

Highest-signal candidate classes:
- `crazypants/enderio/machines/config/config/VacuumConfig.class`
- `crazypants/enderio/machines/machine/vacuum/chest/BlockVacuumChest.class`
- `crazypants/enderio/machines/machine/vacuum/chest/ContainerVacuumChest$1.class`
- `crazypants/enderio/machines/machine/vacuum/chest/ContainerVacuumChest.class`
- `crazypants/enderio/machines/machine/vacuum/chest/ContainerVacuumChestProxy$doOpenFilterGui.class`
- `crazypants/enderio/machines/machine/vacuum/chest/ContainerVacuumChestProxy.class`
- `crazypants/enderio/machines/machine/vacuum/chest/GuiVacuumChest$1.class`
- `crazypants/enderio/machines/machine/vacuum/chest/GuiVacuumChest$2.class`
- `crazypants/enderio/machines/machine/vacuum/chest/GuiVacuumChest.class`
- `crazypants/enderio/machines/machine/vacuum/chest/PacketVaccumChest$Handler.class`
- `crazypants/enderio/machines/machine/vacuum/chest/PacketVaccumChest.class`
- `crazypants/enderio/machines/machine/vacuum/chest/TileVacuumChest$1.class`
- `crazypants/enderio/machines/machine/vacuum/chest/TileVacuumChest$2.class`
- `crazypants/enderio/machines/machine/vacuum/chest/TileVacuumChest.class`
- `crazypants/enderio/machines/machine/vacuum/chest/VacuumRenderMapper.class`
- `crazypants/enderio/machines/machine/vacuum/xp/BlockXPVacuum.class`
- `crazypants/enderio/machines/machine/vacuum/xp/ContainerXPVacuum.class`
- `crazypants/enderio/machines/machine/vacuum/xp/GuiXPVacuum$1.class`
- `crazypants/enderio/machines/machine/vacuum/xp/GuiXPVacuum.class`
- `crazypants/enderio/machines/machine/vacuum/xp/IVacuumRangeRemoteExec$Container.class`
- `crazypants/enderio/machines/machine/vacuum/xp/IVacuumRangeRemoteExec$GUI.class`
- `crazypants/enderio/machines/machine/vacuum/xp/IVacuumRangeRemoteExec.class`
- `crazypants/enderio/machines/machine/vacuum/xp/TileXPVacuum$1.class`
- `crazypants/enderio/machines/machine/vacuum/xp/TileXPVacuum.class`
- `crazypants/enderio/machines/machine/vacuum/xp/XPRenderMapper.class`
- `crazypants/enderio/base/integration/top/TOPData.class`
- `crazypants/enderio/base/machine/modes/EntityAction$Implementer.class`
- `crazypants/enderio/machines/config/Config.class`
- `crazypants/enderio/machines/init/MachineObject.class`
- `crazypants/enderio/machines/init/MachineTileEntity.class`

`crazypants.enderio.machines.machine.vacuum.chest.BlockVacuumChest` methods:
- `public static crazypants.enderio.machines.machine.vacuum.chest.BlockVacuumChest create(crazypants.enderio.api.IModObject);`
- `protected void initDefaultState();`
- `public void func_189540_a(net.minecraft.block.state.IBlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.Block, net.minecraft.util.math.BlockPos);`
- `protected void init();`
- `protected void registerInSmartModelAttacher();`
- `protected net.minecraft.block.state.BlockStateContainer func_180661_e();`
- `public net.minecraft.block.state.IBlockState func_176203_a(int);`
- `public int func_176201_c(net.minecraft.block.state.IBlockState);`
- `public net.minecraft.block.state.IBlockState func_176221_a(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos);`
- `public final net.minecraft.block.state.IBlockState getExtendedState(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos);`
- `protected void setBlockStateWrapperCache(crazypants.enderio.base.render.IBlockStateWrapper, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos, crazypants.enderio.machines.machine.vacuum.chest.TileVacuumChest);`
- `protected crazypants.enderio.base.render.pipeline.BlockStateWrapperBase createBlockStateWrapper(net.minecraft.block.state.IBlockState, net.minecraft.world.IBlockAccess, net.minecraft.util.math.BlockPos);`
- `public crazypants.enderio.base.render.IRenderMapper$IItemRenderMapper getItemRenderMapper();`
- `public crazypants.enderio.base.render.IRenderMapper$IBlockRenderMapper getBlockRenderMapper();`
- `public net.minecraft.util.BlockRenderLayer func_180664_k();`
- `public boolean func_149662_c(net.minecraft.block.state.IBlockState);`
- `public net.minecraft.inventory.Container getServerGuiElement(net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing, int);`
- `public net.minecraft.client.gui.GuiScreen getClientGuiElement(net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.util.EnumFacing, int);`
- `public java.lang.String getUnlocalizedNameForTooltip(net.minecraft.item.ItemStack);`
- `public boolean canRenderInLayer(net.minecraft.block.state.IBlockState, net.minecraft.util.BlockRenderLayer);`
- `public boolean addHitEffects(net.minecraft.block.state.IBlockState, net.minecraft.world.World, net.minecraft.util.math.RayTraceResult, net.minecraft.client.particle.ParticleManager);`
- `public boolean addDestroyEffects(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.client.particle.ParticleManager);`
- `public void registerRenderers(crazypants.enderio.api.IModObject);`
- `public boolean func_149740_M(net.minecraft.block.state.IBlockState);`
- `public int func_180641_l(net.minecraft.block.state.IBlockState, net.minecraft.world.World, net.minecraft.util.math.BlockPos);`

`crazypants.enderio.machines.machine.vacuum.chest.ContainerVacuumChest$1` methods:
- `public void func_75218_e();`

`crazypants.enderio.machines.machine.vacuum.chest.ContainerVacuumChest` methods:
- `protected void addSlots();`
- `public void createGhostSlots(java.util.List<com.enderio.core.client.gui.widget.GhostSlot>);`
- `public java.awt.Point getPlayerInventoryOffset();`
- `public void doOpenFilterGui(int);`
- `public crazypants.enderio.base.filter.item.IItemFilter getFilter(int);`
- `public crazypants.enderio.base.filter.IFilter getFilter(int);`

`crazypants.enderio.machines.machine.vacuum.chest.ContainerVacuumChestProxy$doOpenFilterGui` methods:
- `public static void register(net.minecraftforge.event.RegistryEvent$Register<crazypants.enderio.base.network.ExecPacket$IServerExec>);`
- `public static java.util.function.Consumer<io.netty.buffer.ByteBuf> makeWriter(int);`
- `public java.util.function.Consumer<net.minecraft.entity.player.EntityPlayerMP> apply(io.netty.buffer.ByteBuf);`
- `public java.lang.Object apply(java.lang.Object);`
- `private static void lambda$apply$1(int, int, net.minecraft.entity.player.EntityPlayerMP);`
- `private static void lambda$makeWriter$0(int, io.netty.buffer.ByteBuf);`

`crazypants.enderio.machines.machine.vacuum.chest.ContainerVacuumChestProxy` methods:
- `public default void doOpenFilterGui(int);`

`crazypants.enderio.machines.machine.vacuum.chest.GuiVacuumChest$1` methods:
- `public java.util.List<java.lang.String> getToolTipText();`

`crazypants.enderio.machines.machine.vacuum.chest.GuiVacuumChest$2` methods:
- `public void run();`

`crazypants.enderio.machines.machine.vacuum.chest.GuiVacuumChest` methods:
- `public void func_73866_w_();`
- `public void func_146284_a(net.minecraft.client.gui.GuiButton);`
- `private void setRange(int);`
- `protected void func_146976_a(float, int, int);`
- `static com.enderio.core.client.gui.button.ToggleButton access$000(crazypants.enderio.machines.machine.vacuum.chest.GuiVacuumChest);`

`crazypants.enderio.machines.machine.vacuum.chest.PacketVaccumChest$Handler` methods:
- `public net.minecraftforge.fml.common.network.simpleimpl.IMessage onMessage(crazypants.enderio.machines.machine.vacuum.chest.PacketVaccumChest, net.minecraftforge.fml.common.network.simpleimpl.MessageContext);`
- `public net.minecraftforge.fml.common.network.simpleimpl.IMessage onMessage(net.minecraftforge.fml.common.network.simpleimpl.IMessage, net.minecraftforge.fml.common.network.simpleimpl.MessageContext);`

`crazypants.enderio.machines.machine.vacuum.chest.PacketVaccumChest` methods:
- `public static crazypants.enderio.machines.machine.vacuum.chest.PacketVaccumChest setRange(crazypants.enderio.machines.machine.vacuum.chest.TileVacuumChest, int);`
- `public void fromBytes(io.netty.buffer.ByteBuf);`
- `public void toBytes(io.netty.buffer.ByteBuf);`
- `static net.minecraft.tileentity.TileEntity access$000(crazypants.enderio.machines.machine.vacuum.chest.PacketVaccumChest, net.minecraft.world.World);`
- `static int access$100(crazypants.enderio.machines.machine.vacuum.chest.PacketVaccumChest);`
- `static int access$200(crazypants.enderio.machines.machine.vacuum.chest.PacketVaccumChest);`

`crazypants.enderio.machines.machine.vacuum.chest.TileVacuumChest$1` methods:
- `public boolean doApply(net.minecraft.item.ItemStack);`

## forge-1.12.2 — reliquary

Version: `1.12.2-1.3.4.796`

Missing targets:
- `reliquary.item.FortuneCoinItem`

Highest-signal candidate classes:
- `xreliquary/items/ItemFortuneCoin.class`
- `xreliquary/network/PacketFortuneCoinTogglePressed$1.class`
- `xreliquary/network/PacketFortuneCoinTogglePressed$InventoryType.class`
- `xreliquary/network/PacketFortuneCoinTogglePressed.class`
- `xreliquary/reference/Settings$ItemSettings$FortuneCoinSettings.class`
- `xreliquary/client/init/ItemModels.class`
- `xreliquary/init/ModItems.class`
- `xreliquary/reference/Settings$ItemSettings.class`
- `xreliquary/client/ClientProxy.class`
- `xreliquary/network/PacketHandler.class`
- `xreliquary/reference/Names$Items.class`

`xreliquary.items.ItemFortuneCoin` methods:
- `public void onEquipped(net.minecraft.item.ItemStack, net.minecraft.entity.EntityLivingBase);`
- `public baubles.api.BaubleType getBaubleType(net.minecraft.item.ItemStack);`
- `public void onWornTick(net.minecraft.item.ItemStack, net.minecraft.entity.EntityLivingBase);`
- `protected void addMoreInformation(net.minecraft.item.ItemStack, net.minecraft.world.World, java.util.List<java.lang.String>);`
- `public net.minecraft.item.EnumRarity func_77613_e(net.minecraft.item.ItemStack);`
- `public boolean func_77636_d(net.minecraft.item.ItemStack);`
- `private boolean isEnabled(net.minecraft.item.ItemStack);`
- `public void func_77663_a(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.Entity, int, boolean);`
- `private void scanForEntitiesInRange(net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, double);`
- `private boolean canPickupItem(net.minecraft.entity.item.EntityItem, java.util.List<net.minecraft.util.math.BlockPos>);`
- `private boolean isInDisabledRange(net.minecraft.entity.item.EntityItem, java.util.List<net.minecraft.util.math.BlockPos>);`
- `private java.util.List<net.minecraft.util.math.BlockPos> getDisablePositions(net.minecraft.world.World, net.minecraft.util.math.BlockPos);`
- `private void teleportEntityToPlayer(net.minecraft.entity.Entity, net.minecraft.entity.player.EntityPlayer);`
- `private boolean checkForRoom(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer);`
- `public void onUsingTick(net.minecraft.item.ItemStack, net.minecraft.entity.EntityLivingBase, int);`
- `private double getLongRangePullDistance();`
- `private double getStandardPullDistance();`
- `public int func_77626_a(net.minecraft.item.ItemStack);`
- `public net.minecraft.item.EnumAction func_77661_b(net.minecraft.item.ItemStack);`
- `public net.minecraft.util.ActionResult<net.minecraft.item.ItemStack> func_77659_a(net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer, net.minecraft.util.EnumHand);`
- `private boolean disabledAudio();`
- `public void update(net.minecraft.item.ItemStack, xreliquary.api.IPedestal);`
- `public void onRemoved(net.minecraft.item.ItemStack, xreliquary.api.IPedestal);`
- `public void stop(net.minecraft.item.ItemStack, xreliquary.api.IPedestal);`
- `public void toggle(net.minecraft.item.ItemStack);`
- `public void handleKeyInputEvent(net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent);`

`xreliquary.network.PacketFortuneCoinTogglePressed$InventoryType` methods:
- `public static xreliquary.network.PacketFortuneCoinTogglePressed$InventoryType[] values();`
- `public static xreliquary.network.PacketFortuneCoinTogglePressed$InventoryType valueOf(java.lang.String);`

`xreliquary.network.PacketFortuneCoinTogglePressed` methods:
- `public void fromBytes(io.netty.buffer.ByteBuf);`
- `public void toBytes(io.netty.buffer.ByteBuf);`
- `public net.minecraftforge.fml.common.network.simpleimpl.IMessage onMessage(xreliquary.network.PacketFortuneCoinTogglePressed, net.minecraftforge.fml.common.network.simpleimpl.MessageContext);`
- `private void handleMessage(xreliquary.network.PacketFortuneCoinTogglePressed, net.minecraft.entity.player.EntityPlayerMP);`
- `public net.minecraftforge.fml.common.network.simpleimpl.IMessage onMessage(net.minecraftforge.fml.common.network.simpleimpl.IMessage, net.minecraftforge.fml.common.network.simpleimpl.MessageContext);`
- `private void lambda$onMessage$0(xreliquary.network.PacketFortuneCoinTogglePressed, net.minecraftforge.fml.common.network.simpleimpl.MessageContext);`

`xreliquary.client.init.ItemModels` methods:
- `public static void registerItemModels();`
- `private static void registerItemModelForAllVariants(net.minecraft.item.Item, java.lang.String, net.minecraft.client.renderer.ItemMeshDefinition);`
- `private static void registerItemModel(net.minecraft.item.Item, java.lang.String);`
- `private static void registerItemModel(net.minecraft.item.Item, java.lang.String, int, boolean);`
- `public static void onModelBake(net.minecraftforge.client.event.ModelBakeEvent);`
- `private static net.minecraft.client.renderer.block.model.ModelResourceLocation lambda$registerItemModels$7(net.minecraft.item.ItemStack);`
- `private static net.minecraft.client.renderer.block.model.ModelResourceLocation lambda$registerItemModels$6(net.minecraft.item.ItemStack);`
- `private static net.minecraft.client.renderer.block.model.ModelResourceLocation lambda$registerItemModels$5(net.minecraft.item.ItemStack);`
- `private static net.minecraft.client.renderer.block.model.ModelResourceLocation lambda$registerItemModels$4(net.minecraft.item.ItemStack);`
- `private static net.minecraft.client.renderer.block.model.ModelResourceLocation lambda$registerItemModels$3(net.minecraft.item.ItemStack);`
- `private static net.minecraft.client.renderer.block.model.ModelResourceLocation lambda$registerItemModels$2(net.minecraft.item.ItemStack);`
- `private static net.minecraft.client.renderer.block.model.ModelResourceLocation lambda$registerItemModels$1(net.minecraft.item.ItemStack);`
- `private static net.minecraft.client.renderer.block.model.ModelResourceLocation lambda$registerItemModels$0(net.minecraft.item.ItemStack);`

`xreliquary.init.ModItems` methods:
- `public static void register(net.minecraftforge.event.RegistryEvent$Register<net.minecraft.item.Item>);`
- `private static <T extends net.minecraft.item.Item> T registerItem(net.minecraftforge.registries.IForgeRegistry<net.minecraft.item.Item>, T, java.lang.String);`
- `private static <T extends net.minecraft.item.Item> T registerItem(net.minecraftforge.registries.IForgeRegistry<net.minecraft.item.Item>, T, java.lang.String, boolean);`

`xreliquary.client.ClientProxy` methods:
- `public void registerJEI(net.minecraft.block.Block, java.lang.String, boolean);`
- `public void initSpecialJEIDescriptions();`
- `public void registerJEI(net.minecraft.item.Item, java.lang.String);`
- `public static void registerModels(net.minecraftforge.client.event.ModelRegistryEvent);`
- `private static void registerEntityRenderers();`
- `public void init();`
- `public void postInit();`
- `private void registerBeltRender();`
- `private void registerRenderers();`
- `public void initColors();`
- `private static net.minecraft.client.renderer.entity.Render lambda$registerEntityRenderers$6(net.minecraft.client.renderer.entity.RenderManager);`
- `private static net.minecraft.client.renderer.entity.Render lambda$registerEntityRenderers$5(net.minecraft.client.renderer.entity.RenderManager);`
- `private static net.minecraft.client.renderer.entity.Render lambda$registerEntityRenderers$4(net.minecraft.client.renderer.entity.RenderManager);`
- `private static net.minecraft.client.renderer.entity.Render lambda$registerEntityRenderers$3(net.minecraft.client.renderer.entity.RenderManager);`
- `private static net.minecraft.client.renderer.entity.Render lambda$registerEntityRenderers$2(net.minecraft.client.renderer.entity.RenderManager);`
- `private static net.minecraft.client.renderer.entity.Render lambda$registerEntityRenderers$1(net.minecraft.client.renderer.entity.RenderManager);`
- `private static net.minecraft.client.renderer.entity.Render lambda$registerEntityRenderers$0(net.minecraft.client.renderer.entity.RenderManager);`

`xreliquary.network.PacketHandler` methods:
- `public static void init();`

