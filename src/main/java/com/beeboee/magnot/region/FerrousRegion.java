package com.beeboee.magnot.region;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.logistics.filter.AttributeFilterItem;
import com.simibubi.create.content.logistics.filter.AttributeFilterWhitelistMode;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.logistics.filter.ListFilterItem;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute.ItemAttributeEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record FerrousRegion(UUID id, UUID groupId, BlockPos min, BlockPos max, UUID subLevelId, ItemStack filterStack, boolean whitelistMode) {
    private static final String FILTER_STACK = "FilterStack";
    private static final String FILTER_ITEM = "FilterItem";
    private static final String WHITELIST_MODE = "WhitelistMode";

    public FerrousRegion {
        filterStack = filterStack == null ? ItemStack.EMPTY : filterStack.copyWithCount(1);
        if (filterStack.isEmpty()) {
            whitelistMode = false;
        }
    }

    public FerrousRegion(UUID id, UUID groupId, BlockPos min, BlockPos max, UUID subLevelId) {
        this(id, groupId, min, max, subLevelId, ItemStack.EMPTY, false);
    }

    public FerrousRegion(UUID id, BlockPos min, BlockPos max) {
        this(id, id, min, max, null);
    }

    public FerrousRegion(UUID id, BlockPos min, BlockPos max, UUID subLevelId) {
        this(id, id, min, max, subLevelId);
    }

    public static FerrousRegion fromCorners(BlockPos first, BlockPos second) {
        return fromCorners(UUID.randomUUID(), first, second, null);
    }

    public static FerrousRegion fromCorners(UUID id, BlockPos first, BlockPos second) {
        return fromCorners(id, first, second, null);
    }

    public static FerrousRegion fromCorners(UUID id, BlockPos first, BlockPos second, UUID subLevelId) {
        return fromCorners(id, id, first, second, subLevelId);
    }

    public static FerrousRegion fromCorners(UUID id, UUID groupId, BlockPos first, BlockPos second, UUID subLevelId) {
        return fromCorners(id, groupId, first, second, subLevelId, ItemStack.EMPTY, false);
    }

    public static FerrousRegion fromCorners(UUID id, UUID groupId, BlockPos first, BlockPos second, UUID subLevelId, ItemStack filterStack, boolean whitelistMode) {
        BlockPos min = new BlockPos(
                Math.min(first.getX(), second.getX()),
                Math.min(first.getY(), second.getY()),
                Math.min(first.getZ(), second.getZ())
        );
        BlockPos max = new BlockPos(
                Math.max(first.getX(), second.getX()),
                Math.max(first.getY(), second.getY()),
                Math.max(first.getZ(), second.getZ())
        );
        return new FerrousRegion(id, groupId, min, max, subLevelId, filterStack, whitelistMode);
    }

    public boolean isWorldRegion() {
        return subLevelId == null;
    }

    public boolean belongsToSubLevel(UUID id) {
        return subLevelId != null && subLevelId.equals(id);
    }

    public boolean belongsToSameGroupAs(FerrousRegion other) {
        return groupId.equals(other.groupId);
    }

    public FerrousRegion inSubLevel(UUID id) {
        return new FerrousRegion(this.id, this.groupId, this.min, this.max, id, this.filterStack, this.whitelistMode);
    }

    public FerrousRegion inWorld() {
        return new FerrousRegion(this.id, this.groupId, this.min, this.max, null, this.filterStack, this.whitelistMode);
    }

    public FerrousRegion withFilter(ItemStack filterStack, boolean whitelistMode) {
        return new FerrousRegion(this.id, this.groupId, this.min, this.max, this.subLevelId, filterStack, whitelistMode);
    }

    public FerrousRegion withoutFilter() {
        return withFilter(ItemStack.EMPTY, false);
    }

    public FerrousRegion toggledFilterMode() {
        if (filterStack.isEmpty()) {
            return this;
        }
        return withFilter(filterStack, !whitelistMode);
    }

    public boolean hasFilter() {
        return !filterStack.isEmpty();
    }

    public AABB bounds() {
        return new AABB(
                min.getX(), min.getY(), min.getZ(),
                max.getX() + 1.0D, max.getY() + 1.0D, max.getZ() + 1.0D
        );
    }

    public boolean contains(Vec3 pos) {
        return bounds().contains(pos);
    }

    public boolean intersectsBlock(BlockPos pos) {
        return bounds().intersects(new AABB(
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D
        ));
    }

    public Optional<Vec3> clip(Vec3 from, Vec3 to) {
        if (contains(from)) {
            return Optional.of(from);
        }
        return bounds().clip(from, to);
    }

    public Optional<Double> hitDistanceSqr(Vec3 from, Vec3 to) {
        return clip(from, to).map(hit -> hit.distanceToSqr(from));
    }

    public boolean intersectsSegment(Vec3 from, Vec3 to) {
        return clip(from, to).isPresent();
    }

    public boolean blocksItemPull(Level level, Vec3 from, Vec3 to, ItemStack pulledStack) {
        if (!contains(from) && !contains(to) && !intersectsSegment(from, to)) {
            return false;
        }
        return blocksStack(level, pulledStack);
    }

    public boolean blocksStack(Level level, ItemStack pulledStack) {
        if (filterStack.isEmpty()) {
            return true;
        }

        boolean matches = pulledStack != null && !pulledStack.isEmpty() && matchesFilter(level, pulledStack);
        return whitelistMode ? !matches : matches;
    }

    private boolean matchesFilter(Level level, ItemStack pulledStack) {
        return matchesFilterStack(level, filterStack, pulledStack);
    }

    public static boolean matchesFilterStack(Level level, ItemStack filterStack, ItemStack pulledStack) {
        if (filterStack == null || filterStack.isEmpty() || pulledStack == null || pulledStack.isEmpty()) {
            return false;
        }

        if (filterStack.getItem() instanceof ListFilterItem listFilterItem) {
            return matchesListFilter(level, listFilterItem, filterStack, pulledStack);
        }

        if (filterStack.getItem() instanceof AttributeFilterItem) {
            return matchesAttributeFilter(level, filterStack, pulledStack);
        }

        if (filterStack.getItem() instanceof FilterItem) {
            if (level == null) {
                return false;
            }
            try {
                return FilterItemStack.of(filterStack.copy()).test(level, pulledStack);
            } catch (RuntimeException ignored) {
                return false;
            }
        }

        return FilterItem.testDirect(filterStack, pulledStack, false);
    }

    private static boolean matchesListFilter(Level level, ListFilterItem filterItem, ItemStack filterStack, ItemStack pulledStack) {
        ItemStackHandler items = filterItem.getFilterItemHandler(filterStack);
        boolean respectNbt = filterStack.getOrDefault(AllDataComponents.FILTER_ITEMS_RESPECT_NBT, false);

        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack entry = items.getStackInSlot(i);
            if (entry.isEmpty()) {
                continue;
            }

            boolean matches = entry.getItem() instanceof FilterItem
                    ? matchesFilterStack(level, entry, pulledStack)
                    : FilterItem.testDirect(entry, pulledStack, respectNbt);
            if (matches) {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesAttributeFilter(Level level, ItemStack filterStack, ItemStack pulledStack) {
        if (level == null) {
            return false;
        }

        List<ItemAttributeEntry> attributes = filterStack.getOrDefault(AllDataComponents.ATTRIBUTE_FILTER_MATCHED_ATTRIBUTES, List.of());
        if (attributes.isEmpty()) {
            return false;
        }

        AttributeFilterWhitelistMode mode = filterStack.getOrDefault(AllDataComponents.ATTRIBUTE_FILTER_WHITELIST_MODE, AttributeFilterWhitelistMode.WHITELIST_DISJ);
        boolean requireAll = mode == AttributeFilterWhitelistMode.WHITELIST_CONJ;
        boolean sawAttribute = false;

        for (ItemAttributeEntry entry : attributes) {
            ItemAttribute attribute = entry.attribute();
            if (attribute == null) {
                continue;
            }

            sawAttribute = true;
            boolean matches;
            try {
                matches = attribute.appliesTo(pulledStack, level) != entry.inverted();
            } catch (RuntimeException ignored) {
                return false;
            }

            if (requireAll && !matches) {
                return false;
            }
            if (!requireAll && matches) {
                return true;
            }
        }

        return requireAll && sawAttribute;
    }

    public boolean sameShapeAndSpace(FerrousRegion other) {
        return id.equals(other.id)
                && groupId.equals(other.groupId)
                && min.equals(other.min)
                && max.equals(other.max)
                && java.util.Objects.equals(subLevelId, other.subLevelId);
    }

    public CompoundTag save(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", id);
        tag.putUUID("GroupId", groupId);
        tag.putInt("MinX", min.getX());
        tag.putInt("MinY", min.getY());
        tag.putInt("MinZ", min.getZ());
        tag.putInt("MaxX", max.getX());
        tag.putInt("MaxY", max.getY());
        tag.putInt("MaxZ", max.getZ());
        if (subLevelId != null) {
            tag.putUUID("SubLevelId", subLevelId);
        }
        if (!filterStack.isEmpty()) {
            tag.put(FILTER_STACK, filterStack.saveOptional(registries));
            tag.putBoolean(WHITELIST_MODE, whitelistMode);
        }
        return tag;
    }

    public static FerrousRegion load(CompoundTag tag, HolderLookup.Provider registries) {
        UUID id = tag.hasUUID("Id") ? tag.getUUID("Id") : UUID.randomUUID();
        UUID groupId = tag.hasUUID("GroupId") ? tag.getUUID("GroupId") : id;
        BlockPos min = new BlockPos(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ"));
        BlockPos max = new BlockPos(tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ"));
        UUID subLevelId = tag.hasUUID("SubLevelId") ? tag.getUUID("SubLevelId") : null;
        ItemStack filterStack = loadFilterStack(tag, registries);
        boolean whitelistMode = !filterStack.isEmpty() && tag.getBoolean(WHITELIST_MODE);
        return new FerrousRegion(id, groupId, min, max, subLevelId, filterStack, whitelistMode);
    }

    private static ItemStack loadFilterStack(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains(FILTER_STACK, Tag.TAG_COMPOUND)) {
            return ItemStack.parseOptional(registries, tag.getCompound(FILTER_STACK));
        }

        if (!tag.contains(FILTER_ITEM)) {
            return ItemStack.EMPTY;
        }

        ResourceLocation id = ResourceLocation.tryParse(tag.getString(FILTER_ITEM));
        if (id == null) {
            return ItemStack.EMPTY;
        }

        Item item = BuiltInRegistries.ITEM.get(id);
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }
}
