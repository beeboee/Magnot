package com.beeboee.magnot.entity;

import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FerrousRegionEntity extends Entity implements IEntityWithComplexSpawn {
    private UUID regionId = UUID.randomUUID();
    private UUID groupId = regionId;
    private UUID subLevelId;

    public FerrousRegionEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public FerrousRegionEntity(Level level, FerrousRegion region) {
        this(MagnotEntityTypes.ferrousRegion(), level);
        this.regionId = region.id();
        this.groupId = region.groupId();
        this.subLevelId = region.subLevelId();
        setBoundingBox(region.bounds());
        resetPositionToBB();
    }

    public static FerrousRegionEntity spawn(ServerLevel level, FerrousRegion region) {
        FerrousRegionEntity entity = new FerrousRegionEntity(level, region);
        level.addFreshEntity(entity);
        return entity;
    }

    public FerrousRegion asRegion() {
        AABB bounds = getBoundingBox();
        BlockPos min = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos max = BlockPos.containing(bounds.maxX - 1.0E-6D, bounds.maxY - 1.0E-6D, bounds.maxZ - 1.0E-6D);
        return FerrousRegion.fromCorners(regionId, groupId, min, max, subLevelId);
    }

    public UUID regionId() {
        return regionId;
    }

    public UUID groupId() {
        return groupId;
    }

    public UUID subLevelId() {
        return subLevelId;
    }

    public void setSubLevelId(UUID subLevelId) {
        this.subLevelId = subLevelId;
    }

    public boolean contains(BlockPos pos) {
        return contains(Vec3.atCenterOf(pos));
    }

    public boolean contains(Vec3 pos) {
        return getBoundingBox().contains(pos);
    }

    public boolean blocksMagnet(Vec3 source, Vec3 targetPosition) {
        AABB bounds = getBoundingBox();
        return bounds.contains(source)
                || bounds.contains(targetPosition)
                || bounds.clip(source, targetPosition).isPresent();
    }

    public void resetPositionToBB() {
        AABB bounds = getBoundingBox();
        setPosRaw(bounds.getCenter().x, bounds.minY, bounds.getCenter().z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        if (!level().isClientSide()) {
            discard();
            return;
        }

        xRotO = getXRot();
        yRotO = getYRot();
        walkDistO = walkDist;
        xo = getX();
        yo = getY();
        zo = getZ();
    }

    @Override
    public void setPos(double x, double y, double z) {
        AABB bounds = getBoundingBox();
        setPosRaw(x, y, z);
        Vec3 center = bounds.getCenter();
        setBoundingBox(bounds.move(-center.x, -bounds.minY, -center.z).move(x, y, z));
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        if (!level().isClientSide() && isAlive() && pos.lengthSqr() > 0.0D) {
            discard();
        }
    }

    @Override
    public void push(double x, double y, double z) {
        if (!level().isClientSide() && isAlive() && x * x + y * y + z * z > 0.0D) {
            discard();
        }
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return super.getDimensions(pose).withEyeHeight(0.0F);
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putUUID("RegionId", regionId);
        tag.putUUID("GroupId", groupId);
        if (subLevelId != null) {
            tag.putUUID("SubLevelId", subLevelId);
        }

        AABB relativeBounds = getBoundingBox().move(position().scale(-1));
        tag.putDouble("MinX", relativeBounds.minX);
        tag.putDouble("MinY", relativeBounds.minY);
        tag.putDouble("MinZ", relativeBounds.minZ);
        tag.putDouble("MaxX", relativeBounds.maxX);
        tag.putDouble("MaxY", relativeBounds.maxY);
        tag.putDouble("MaxZ", relativeBounds.maxZ);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        regionId = tag.hasUUID("RegionId") ? tag.getUUID("RegionId") : UUID.randomUUID();
        groupId = tag.hasUUID("GroupId") ? tag.getUUID("GroupId") : regionId;
        subLevelId = tag.hasUUID("SubLevelId") ? tag.getUUID("SubLevelId") : null;

        AABB relativeBounds = new AABB(
                tag.getDouble("MinX"), tag.getDouble("MinY"), tag.getDouble("MinZ"),
                tag.getDouble("MaxX"), tag.getDouble("MaxY"), tag.getDouble("MaxZ")
        );
        setBoundingBox(relativeBounds.move(position()));
    }

    @Override
    public float rotate(Rotation rotation) {
        AABB relativeBounds = getBoundingBox().move(position().scale(-1));
        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
            setBoundingBox(new AABB(
                    relativeBounds.minZ,
                    relativeBounds.minY,
                    relativeBounds.minX,
                    relativeBounds.maxZ,
                    relativeBounds.maxY,
                    relativeBounds.maxX
            ).move(position()));
        }
        return super.rotate(rotation);
    }

    @Override
    public float mirror(Mirror mirror) {
        return super.mirror(mirror);
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        addAdditionalSaveData(tag);
        buffer.writeNbt(tag);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readNbt();
        if (tag != null) {
            readAdditionalSaveData(tag);
        }
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }
}
