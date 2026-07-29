package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

public record FerrousRegion(UUID id,UUID groupId,BlockPos min,BlockPos max){
    private static final double EPS=1.0E-12D;
    public FerrousRegion(UUID id,BlockPos min,BlockPos max){this(id,id,min,max);} public static FerrousRegion fromCorners(BlockPos a,BlockPos b){return fromCorners(UUID.randomUUID(),a,b);} public static FerrousRegion fromCorners(UUID id,BlockPos a,BlockPos b){return new FerrousRegion(id,id,new BlockPos(Math.min(a.getX(),b.getX()),Math.min(a.getY(),b.getY()),Math.min(a.getZ(),b.getZ())),new BlockPos(Math.max(a.getX(),b.getX()),Math.max(a.getY(),b.getY()),Math.max(a.getZ(),b.getZ())));}
    public AABB bounds(){return new AABB(min.getX(),min.getY(),min.getZ(),max.getX()+1.0D,max.getY()+1.0D,max.getZ()+1.0D);} public boolean contains(Vec3 p){AABB b=bounds();return p.x>=b.minX&&p.x<=b.maxX&&p.y>=b.minY&&p.y<=b.maxY&&p.z>=b.minZ&&p.z<=b.maxZ;} public boolean intersectsSegment(Vec3 a,Vec3 b){return clipParameter(a,b).isPresent();} public Optional<Double> hitDistanceSqr(Vec3 a,Vec3 b){OptionalDouble t=clipParameter(a,b);return t.isEmpty()?Optional.empty():Optional.of(a.distanceToSqr(b)*t.getAsDouble()*t.getAsDouble());}
    private OptionalDouble clipParameter(Vec3 a,Vec3 b){AABB box=bounds();double lo=0,hi=1;double[] s={a.x,a.y,a.z},d={b.x-a.x,b.y-a.y,b.z-a.z},mn={box.minX,box.minY,box.minZ},mx={box.maxX,box.maxY,box.maxZ};for(int i=0;i<3;i++){if(Math.abs(d[i])<EPS){if(s[i]<mn[i]||s[i]>mx[i])return OptionalDouble.empty();continue;}double x=(mn[i]-s[i])/d[i],y=(mx[i]-s[i])/d[i];if(x>y){double q=x;x=y;y=q;}lo=Math.max(lo,x);hi=Math.min(hi,y);if(lo>hi)return OptionalDouble.empty();}return hi<0||lo>1?OptionalDouble.empty():OptionalDouble.of(Math.max(0,lo));}
    public CompoundTag save(){CompoundTag t=new CompoundTag();t.putUUID("Id",id);t.putUUID("GroupId",groupId);t.putInt("MinX",min.getX());t.putInt("MinY",min.getY());t.putInt("MinZ",min.getZ());t.putInt("MaxX",max.getX());t.putInt("MaxY",max.getY());t.putInt("MaxZ",max.getZ());return t;} public static FerrousRegion load(CompoundTag t){UUID id=t.hasUUID("Id")?t.getUUID("Id"):UUID.randomUUID();UUID group=t.hasUUID("GroupId")?t.getUUID("GroupId"):id;return new FerrousRegion(id,group,new BlockPos(t.getInt("MinX"),t.getInt("MinY"),t.getInt("MinZ")),new BlockPos(t.getInt("MaxX"),t.getInt("MaxY"),t.getInt("MaxZ")));}
}
