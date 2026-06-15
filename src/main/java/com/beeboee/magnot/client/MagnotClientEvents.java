package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.compat.sable.MagnotSableClientCompat;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.ConfigureFerrousRegionFilterPayload;
import com.beeboee.magnot.network.RemoveClosestFerrousRegionPayload;
import com.beeboee.magnot.network.ToggleFerrousTubeFilterModePayload;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = Magnot.MOD_ID, value = Dist.CLIENT)
public final class MagnotClientEvents {
    private static final Object SELECTION_OUTLINE_SLOT = new Object();
    private static final int FERROUS_RED = 0xBD2537;
    private static final int LIMIT_YELLOW = 0xFFD43B;
    private static final int HUD_BACKGROUND = 0xA0000000;
    private static final int HUD_BORDER = 0xCCBD2537;
    private static final int HUD_TEXT = 0xFFFFFFFF;
    private static final int HUD_PADDING = 3;
    private static final int HUD_CELL_SIZE = 18;
    private static final int HUD_MAX_COLUMNS = 9;
    private static final int HUD_MAX_ROWS = 2;
    private static final int HUD_MAX_VISIBLE_ITEMS = HUD_MAX_COLUMNS * HUD_MAX_ROWS;
    private static final int HUD_MAX_EXPANDED_MATCHES = 180;
    private static final int HUD_CYCLE_TICKS = 20;
    private static final double REGION_REVEAL_RADIUS = 25.0D;
    private static final double REGION_REVEAL_RADIUS_SQR = REGION_REVEAL_RADIUS * REGION_REVEAL_RADIUS;
    private static long nextRegionRemovalTick = 0L;
    private static long nextRegionFilterTick = 0L;
    private static FerrousRegion filterPreviewRegion;
    private static ItemStack cachedPreviewFilterStack = ItemStack.EMPTY;
    private static List<ItemStack> cachedPreviewStacks = List.of();

    private MagnotClientEvents() {
    }

    @SubscribeEvent
    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (!holdingFerrousTube()) {
            return;
        }

        if (event.isAttack()) {
            event.setCanceled(true);
            event.setSwingHand(true);

            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player == null || minecraft.level == null) {
                return;
            }

            long gameTime = player.level().getGameTime();
            if (gameTime < nextRegionRemovalTick) {
                return;
            }
            nextRegionRemovalTick = gameTime + 5L;

            selectedRegion(player).ifPresent(region -> PacketDistributor.sendToServer(new RemoveClosestFerrousRegionPayload(region.id())));
            return;
        }

        if (!event.isUseItem()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return;
        }

        ItemStack held = player.getMainHandItem();
        if (FerrousTubeItem.getFirstCorner(held).isPresent()) {
            return;
        }

        ItemStack filterStack = player.getOffhandItem();
        long gameTime = player.level().getGameTime();
        if (player.isShiftKeyDown() && !filterStack.isEmpty()) {
            event.setCanceled(true);
            event.setSwingHand(true);
            if (gameTime >= nextRegionFilterTick) {
                nextRegionFilterTick = gameTime + 5L;
                PacketDistributor.sendToServer(new ToggleFerrousTubeFilterModePayload());
            }
            return;
        }

        Optional<FerrousRegion> selectedRegion = selectedRegion(player);
        if (selectedRegion.isEmpty()) {
            return;
        }

        event.setCanceled(true);
        event.setSwingHand(true);

        if (gameTime < nextRegionFilterTick) {
            return;
        }
        nextRegionFilterTick = gameTime + 5L;

        boolean clear = filterStack.isEmpty();
        PacketDistributor.sendToServer(new ConfigureFerrousRegionFilterPayload(selectedRegion.get().id(), filterStack, clear, false));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            filterPreviewRegion = null;
            return;
        }

        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            filterPreviewRegion = null;
            return;
        }

        Optional<FerrousRegion> selectedRegion = selectedRegion(player);
        for (FerrousRegion region : ClientFerrousRegionStore.regions()) {
            renderRegion(player, minecraft.level, region, selectedRegion, region.id());
        }

        var firstCorner = FerrousTubeItem.getFirstCorner(held);
        if (firstCorner.isEmpty()) {
            filterPreviewRegion = selectedRegion.filter(FerrousRegion::hasFilter).orElse(null);
            return;
        }

        filterPreviewRegion = null;
        HitResult hitResult = minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }

        BlockPos clicked = blockHitResult.getBlockPos();
        BlockPos clampedCorner = FerrousTubeItem.clampToRegionLimit(firstCorner.get(), clicked);
        boolean overLimit = FerrousTubeItem.exceedsRegionLimit(firstCorner.get(), clicked);
        int color = overLimit ? LIMIT_YELLOW : FERROUS_RED;

        player.displayClientMessage(
                Component.translatable("message.magnot.click_to_confirm").withStyle(style -> style.withColor(color)),
                true
        );

        AABB selectionBox = boxBetween(firstCorner.get(), clampedCorner);
        Outliner.getInstance()
                .showAABB(SELECTION_OUTLINE_SLOT, selectionBox)
                .colored(color)
                .withFaceTextures(MagnotSpecialTextures.FERROUS_REGION, MagnotSpecialTextures.FERROUS_REGION)
                .disableLineNormals()
                .lineWidth(1.0F / 16.0F);
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui || filterPreviewRegion == null || !holdingFerrousTube()) {
            return;
        }

        ItemStack filterStack = filterPreviewRegion.filterStack();
        if (filterStack.isEmpty()) {
            return;
        }

        List<ItemStack> previewStacks = visiblePreviewStacks(filterPreviewStacks(minecraft.level, filterStack), minecraft.level == null ? 0L : minecraft.level.getGameTime());
        if (previewStacks.isEmpty()) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        String text = filterPreviewMode(filterPreviewRegion);
        int columns = Math.min(HUD_MAX_COLUMNS, previewStacks.size());
        int rows = (previewStacks.size() + HUD_MAX_COLUMNS - 1) / HUD_MAX_COLUMNS;
        int gridWidth = columns * HUD_CELL_SIZE;
        int gridHeight = rows * HUD_CELL_SIZE;
        int textWidth = minecraft.font.width(text);
        int width = Math.max(gridWidth, textWidth) + HUD_PADDING * 2;
        int height = HUD_PADDING + gridHeight + 2 + minecraft.font.lineHeight + HUD_PADDING;
        int x = (minecraft.getWindow().getGuiScaledWidth() - width) / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2 + 13;
        int gridX = x + (width - gridWidth) / 2;
        int gridY = y + HUD_PADDING;
        int textX = x + (width - textWidth) / 2;
        int textY = gridY + gridHeight + 2;

        graphics.fill(x, y, x + width, y + height, HUD_BACKGROUND);
        graphics.fill(x, y, x + width, y + 1, HUD_BORDER);
        graphics.fill(x, y + height - 1, x + width, y + height, HUD_BORDER);
        graphics.fill(x, y, x + 1, y + height, HUD_BORDER);
        graphics.fill(x + width - 1, y, x + width, y + height, HUD_BORDER);

        for (int i = 0; i < previewStacks.size(); i++) {
            int column = i % HUD_MAX_COLUMNS;
            int row = i / HUD_MAX_COLUMNS;
            graphics.renderItem(previewStacks.get(i), gridX + column * HUD_CELL_SIZE + 1, gridY + row * HUD_CELL_SIZE + 1);
        }

        graphics.drawString(minecraft.font, text, textX, textY, HUD_TEXT, true);
    }

    private static List<ItemStack> filterPreviewStacks(Level level, ItemStack filterStack) {
        if (ItemStack.isSameItemSameComponents(cachedPreviewFilterStack, filterStack)) {
            return cachedPreviewStacks;
        }

        List<ItemStack> stacks = expandedFilterPreviewStacks(level, filterStack);
        cachedPreviewFilterStack = filterStack.copyWithCount(1);
        cachedPreviewStacks = List.copyOf(stacks);
        return cachedPreviewStacks;
    }

    private static List<ItemStack> expandedFilterPreviewStacks(Level level, ItemStack filterStack) {
        if (filterStack.getItem() instanceof FilterItem filterItem) {
            try {
                ItemStack[] filterItems = filterItem.getFilterItems(filterStack);
                List<ItemStack> stacks = new ArrayList<>(filterItems.length);
                for (ItemStack stack : filterItems) {
                    if (!stack.isEmpty()) {
                        stacks.add(stack.copyWithCount(1));
                    }
                }
                if (!stacks.isEmpty()) {
                    return stacks;
                }
            } catch (RuntimeException ignored) {
            }

            List<ItemStack> matchingStacks = expandMatchingFilterStacks(level, filterStack);
            if (!matchingStacks.isEmpty()) {
                return matchingStacks;
            }
        }

        return List.of(filterStack.copyWithCount(1));
    }

    private static List<ItemStack> expandMatchingFilterStacks(Level level, ItemStack filterStack) {
        if (level == null) {
            return List.of();
        }

        FilterItemStack wrappedFilter;
        try {
            wrappedFilter = FilterItemStack.of(filterStack.copy());
        } catch (RuntimeException ignored) {
            return List.of();
        }

        List<ItemStack> stacks = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack candidate = item.getDefaultInstance();
            if (candidate.isEmpty()) {
                continue;
            }

            try {
                if (wrappedFilter.test(level, candidate)) {
                    stacks.add(candidate.copyWithCount(1));
                    if (stacks.size() >= HUD_MAX_EXPANDED_MATCHES) {
                        break;
                    }
                }
            } catch (RuntimeException ignored) {
                return List.of();
            }
        }
        return stacks;
    }

    private static List<ItemStack> visiblePreviewStacks(List<ItemStack> stacks, long gameTime) {
        if (stacks.size() <= HUD_MAX_VISIBLE_ITEMS) {
            return stacks;
        }

        List<ItemStack> visible = new ArrayList<>(HUD_MAX_VISIBLE_ITEMS);
        int offset = (int) ((gameTime / HUD_CYCLE_TICKS) % stacks.size());
        for (int i = 0; i < HUD_MAX_VISIBLE_ITEMS; i++) {
            visible.add(stacks.get((offset + i) % stacks.size()));
        }
        return visible;
    }

    private static String filterPreviewMode(FerrousRegion region) {
        return region.whitelistMode() ? "Allow" : "Block";
    }

    private static boolean renderRegion(LocalPlayer player, net.minecraft.world.level.Level level, FerrousRegion region, Optional<FerrousRegion> selectedRegion, Object renderSlot) {
        AABB displayBounds = ModList.get().isLoaded("sable")
                ? MagnotSableClientCompat.displayBounds(level, region)
                : region.bounds();

        if (!isNearPlayer(player, displayBounds)) {
            return false;
        }

        boolean selected = selectedRegion.map(FerrousRegion::id).filter(region.id()::equals).isPresent();
        MagnotSpecialTextures faceTexture = selected ? MagnotSpecialTextures.FERROUS_REGION : null;
        float lineWidth = selected ? 1.0F / 16.0F : 1.0F / 64.0F;

        if (ModList.get().isLoaded("sable")
                && MagnotSableClientCompat.showRegionOutline(level, renderSlot, region, FERROUS_RED, faceTexture, lineWidth)) {
            return true;
        }

        Outliner.getInstance()
                .showAABB(renderSlot, displayBounds)
                .colored(FERROUS_RED)
                .withFaceTextures(faceTexture, faceTexture)
                .disableLineNormals()
                .lineWidth(lineWidth);

        if (ModList.get().isLoaded("sable")) {
            MagnotSableClientCompat.disableOutlineTransform(renderSlot);
        }

        return true;
    }

    private static boolean holdingFerrousTube() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        return player != null && player.getMainHandItem().is(MagnotItems.FERROUS_TUBE.get());
    }

    private static Optional<FerrousRegion> selectedRegion() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return Optional.empty();
        }

        return selectedRegion(player);
    }

    private static Optional<FerrousRegion> selectedRegion(LocalPlayer player) {
        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return Optional.empty();
        }

        Vec3 from = player.getEyePosition();
        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 to = from.add(player.getLookAngle().scale(range));

        Optional<FerrousRegion> selected = ClientFerrousRegionStore.closestIntersecting(from, to);
        if (selected.isEmpty() && ModList.get().isLoaded("sable")) {
            selected = MagnotSableClientCompat.closestIntersecting(player.level(), from, to);
        }

        return selected;
    }

    private static boolean isNearPlayer(LocalPlayer player, AABB bounds) {
        Vec3 playerPosition = player.position();
        double dx = Math.max(Math.max(bounds.minX - playerPosition.x, 0.0D), playerPosition.x - bounds.maxX);
        double dy = Math.max(Math.max(bounds.minY - playerPosition.y, 0.0D), playerPosition.y - bounds.maxY);
        double dz = Math.max(Math.max(bounds.minZ - playerPosition.z, 0.0D), playerPosition.z - bounds.maxZ);
        return dx * dx + dy * dy + dz * dz <= REGION_REVEAL_RADIUS_SQR;
    }

    private static AABB boxBetween(BlockPos first, BlockPos second) {
        int minX = Math.min(first.getX(), second.getX());
        int minY = Math.min(first.getY(), second.getY());
        int minZ = Math.min(first.getZ(), second.getZ());
        int maxX = Math.max(first.getX(), second.getX()) + 1;
        int maxY = Math.max(first.getY(), second.getY()) + 1;
        int maxZ = Math.max(first.getZ(), second.getZ()) + 1;
        return new AABB(new Vec3(minX, minY, minZ), new Vec3(maxX, maxY, maxZ));
    }
}
