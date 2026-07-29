package com.beeboee.magnot.server;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

public final class FerrousEffects {
    private static final DustParticleOptions DUST=new DustParticleOptions(new Vector3f(0.741F,0.145F,0.216F),1.0F);
    private FerrousEffects() {}
    public static void firstCorner(ServerLevel level, BlockPos pos) { level.playSound(null,pos,SoundEvents.SLIME_BLOCK_PLACE,SoundSource.BLOCKS,0.5F,0.85F); for(int i=0;i<12;i++) level.sendParticles(DUST,pos.getX()+level.random.nextDouble(),pos.getY()+level.random.nextDouble(),pos.getZ()+level.random.nextDouble(),1,0,0,0,0); }
    public static void confirmation(ServerLevel level, BlockPos pos, FerrousRegion region) { level.playSound(null,pos,SoundEvents.SLIME_BLOCK_PLACE,SoundSource.BLOCKS,0.5F,0.95F); edges(level,region); }
    public static void removal(ServerLevel level, FerrousRegion region) { level.playSound(null,BlockPos.containing(region.bounds().getCenter()),SoundEvents.SLIME_BLOCK_BREAK,SoundSource.BLOCKS,0.5F,0.75F); edges(level,region); }
    private static void edges(ServerLevel level, FerrousRegion region) { AABB b=region.bounds(); line(level,b.minX,b.minY,b.minZ,b.maxX,b.minY,b.minZ); line(level,b.minX,b.maxY,b.minZ,b.maxX,b.maxY,b.minZ); line(level,b.minX,b.minY,b.maxZ,b.maxX,b.minY,b.maxZ); line(level,b.minX,b.maxY,b.maxZ,b.maxX,b.maxY,b.maxZ); line(level,b.minX,b.minY,b.minZ,b.minX,b.maxY,b.minZ); line(level,b.maxX,b.minY,b.minZ,b.maxX,b.maxY,b.minZ); line(level,b.minX,b.minY,b.maxZ,b.minX,b.maxY,b.maxZ); line(level,b.maxX,b.minY,b.maxZ,b.maxX,b.maxY,b.maxZ); line(level,b.minX,b.minY,b.minZ,b.minX,b.minY,b.maxZ); line(level,b.maxX,b.minY,b.minZ,b.maxX,b.minY,b.maxZ); line(level,b.minX,b.maxY,b.minZ,b.minX,b.maxY,b.maxZ); line(level,b.maxX,b.maxY,b.minZ,b.maxX,b.maxY,b.maxZ); }
    private static void line(ServerLevel level,double x1,double y1,double z1,double x2,double y2,double z2) { int steps=Math.max(4,(int)Math.ceil(Math.max(Math.abs(x2-x1),Math.max(Math.abs(y2-y1),Math.abs(z2-z1)))*2)); for(int i=0;i<=steps;i++){double t=i/(double)steps;level.sendParticles(DUST,x1+(x2-x1)*t,y1+(y2-y1)*t,z1+(z2-z1)*t,1,0,0,0,0);} }
}
