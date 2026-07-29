package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class MagnotClient implements ClientModInitializer {
    private static final Object PREVIEW=new Object(); private static final ResourceLocation TEXTURE=new ResourceLocation(Magnot.MOD_ID,"textures/special/ferrous_region.png");
    private static final int RED=0xBD2537,YELLOW=0xFFD43B; private static final double REVEAL=625.0D,OFFSET=1.0D/128.0D; private static final float FADE=0.18F,MIN=1.0E-3F;
    private static final int[][] EDGES={{0,1},{1,2},{2,3},{3,0},{4,5},{5,6},{6,7},{7,4},{0,4},{1,5},{2,6},{3,7}};
    private static final int[][] FACES={{0,3,2,1},{4,5,6,7},{5,1,0,4},{7,3,2,6},{4,0,3,7},{6,2,1,5}};
    private static final Map<Object,Outline> OUTLINES=new LinkedHashMap<>(); private static long nextRemoval,lastRender;

    @Override public void onInitializeClient(){
        ClientPlayNetworking.registerGlobalReceiver(MagnotNetwork.SYNC,(client,handler,buf,responseSender)->{List<FerrousRegion> regions=decode(buf);client.execute(()->ClientFerrousRegionStore.setRegions(regions));});
        ClientPlayConnectionEvents.DISCONNECT.register((handler,client)->{ClientFerrousRegionStore.clear();OUTLINES.clear();});
        ClientTickEvents.END_CLIENT_TICK.register(MagnotClient::tick);
        ClientPreAttackCallback.EVENT.register((client,player,clickCount)->attack(client,player,clickCount));
        WorldRenderEvents.AFTER_ENTITIES.register(MagnotClient::render);
    }

    private static List<FerrousRegion> decode(FriendlyByteBuf buf){int count=buf.readVarInt();List<FerrousRegion> regions=new ArrayList<>(count);for(int i=0;i<count;i++){UUID id=buf.readUUID(),group=buf.readUUID();regions.add(new FerrousRegion(id,group,buf.readBlockPos(),buf.readBlockPos()));}return regions;}
    private static void tick(Minecraft client){for(Outline outline:OUTLINES.values())outline.beginFrame();LocalPlayer player=client.player;if(player==null||client.level==null)return;ItemStack held=player.getMainHandItem();if(!held.is(MagnotItems.FERROUS_TUBE))return;Optional<FerrousRegion> selected=selected(client,player);for(FerrousRegion region:ClientFerrousRegionStore.regions()){AABB bounds=region.bounds();if(!near(player,bounds))continue;boolean active=selected.map(FerrousRegion::id).filter(region.id()::equals).isPresent();show(region.id(),bounds,RED,active,active?1.0F/16.0F:1.0F/64.0F);}Optional<BlockPos> first=FerrousTubeItem.getFirstCorner(held);if(first.isEmpty())return;HitResult hit=client.hitResult;if(!(hit instanceof BlockHitResult block)||hit.getType()!=HitResult.Type.BLOCK)return;BlockPos clicked=block.getBlockPos(),clamped=FerrousTubeItem.clampToRegionLimit(first.get(),clicked);boolean over=FerrousTubeItem.exceedsRegionLimit(first.get(),clicked);int color=over?YELLOW:RED;player.displayClientMessage(Component.translatable("message.magnot.click_to_confirm").withStyle(style->style.withColor(color)),true);show(PREVIEW,FerrousRegion.fromCorners(UUID.nameUUIDFromBytes("magnot-preview".getBytes()),first.get(),clamped).bounds(),color,true,1.0F/16.0F);}
    private static boolean attack(Minecraft client,LocalPlayer player,int clickCount){if(!player.getMainHandItem().is(MagnotItems.FERROUS_TUBE))return false;if(clickCount!=0&&client.level!=null&&client.level.getGameTime()>=nextRemoval){nextRemoval=client.level.getGameTime()+5;selected(client,player).ifPresent(region->MagnotNetwork.remove(region.id()));player.swing(net.minecraft.world.InteractionHand.MAIN_HAND);}return true;}
    private static Optional<FerrousRegion> selected(Minecraft client,LocalPlayer player){Vec3 from=player.getEyePosition();double range=client.gameMode==null?6.0D:client.gameMode.getPickRange()+1.0D;return ClientFerrousRegionStore.closestIntersecting(from,from.add(player.getViewVector(1.0F).scale(range)));}
    private static boolean near(LocalPlayer player,AABB b){Vec3 p=player.position();double dx=Math.max(Math.max(b.minX-p.x,0),p.x-b.maxX),dy=Math.max(Math.max(b.minY-p.y,0),p.y-b.maxY),dz=Math.max(Math.max(b.minZ-p.z,0),p.z-b.maxZ);return dx*dx+dy*dy+dz*dz<=REVEAL;}
    private static void show(Object slot,AABB bounds,int color,boolean selected,float width){Outline outline=OUTLINES.get(slot);Vec3[] corners=corners(bounds);if(outline==null)OUTLINES.put(slot,new Outline(corners,color,selected,width));else outline.update(corners,color,selected,width);}
    private static Vec3[] corners(AABB b){return new Vec3[]{new Vec3(b.minX,b.minY,b.minZ),new Vec3(b.maxX,b.minY,b.minZ),new Vec3(b.maxX,b.minY,b.maxZ),new Vec3(b.minX,b.minY,b.maxZ),new Vec3(b.minX,b.maxY,b.minZ),new Vec3(b.maxX,b.maxY,b.minZ),new Vec3(b.maxX,b.maxY,b.maxZ),new Vec3(b.minX,b.maxY,b.maxZ)};}

    private static void render(WorldRenderContext context){if(OUTLINES.isEmpty()||context.consumers()==null)return;long now=System.nanoTime();float delta=lastRender==0?0:Math.min((now-lastRender)/1_000_000_000.0F,0.1F);lastRender=now;Vec3 camera=context.camera().getPosition();PoseStack pose=context.matrixStack();MultiBufferSource consumers=context.consumers();Iterator<Outline> iterator=OUTLINES.values().iterator();while(iterator.hasNext()){Outline outline=iterator.next();outline.advance(delta);if(!outline.visible()){iterator.remove();continue;}float strength=smooth(outline.strength);if(strength>MIN)faces(pose,consumers,outline,camera,strength);lines(pose,consumers,outline,camera);if(strength>MIN)thick(pose,consumers,outline,camera,strength);}RenderSystem.lineWidth(1.0F);}
    private static void faces(PoseStack pose,MultiBufferSource consumers,Outline o,Vec3 camera,float strength){VertexConsumer c=consumers.getBuffer(RenderType.entityTranslucent(TEXTURE));Matrix4f m=pose.last().pose();Matrix3f n=pose.last().normal();int alpha=Math.max(1,Math.round(72*strength));Vec3 center=o.center();boolean inside=o.contains(camera);for(int[] f:FACES){Vec3 a=o.corners[f[0]],b=o.corners[f[1]],cc=o.corners[f[2]],d=o.corners[f[3]];Vec3 normal=b.subtract(a).cross(cc.subtract(a)).normalize();Vec3 fc=a.add(b).add(cc).add(d).scale(0.25);if(normal.dot(fc.subtract(center))<0)normal=normal.scale(-1);if(!inside&&normal.dot(camera.subtract(fc))<=0)continue;Vec3 off=normal.scale(inside?-OFFSET:OFFSET);vertex(c,m,n,a.add(off).subtract(camera),0,0,o,alpha,normal);vertex(c,m,n,b.add(off).subtract(camera),0,1,o,alpha,normal);vertex(c,m,n,cc.add(off).subtract(camera),1,1,o,alpha,normal);vertex(c,m,n,d.add(off).subtract(camera),1,0,o,alpha,normal);}}
    private static void vertex(VertexConsumer c,Matrix4f m,Matrix3f n,Vec3 p,float u,float v,Outline o,int alpha,Vec3 normal){c.vertex(m,(float)p.x,(float)p.y,(float)p.z).color(o.r(),o.g(),o.b(),alpha).uv(u,v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(n,(float)normal.x,(float)normal.y,(float)normal.z).endVertex();}
    private static void lines(PoseStack pose,MultiBufferSource consumers,Outline o,Vec3 camera){VertexConsumer c=consumers.getBuffer(RenderType.lines());Matrix4f m=pose.last().pose();Matrix3f n=pose.last().normal();for(int[] e:EDGES){Vec3 a=o.corners[e[0]].subtract(camera),b=o.corners[e[1]].subtract(camera),normal=b.subtract(a).normalize();c.vertex(m,(float)a.x,(float)a.y,(float)a.z).color(o.r(),o.g(),o.b(),255).normal(n,(float)normal.x,(float)normal.y,(float)normal.z).endVertex();c.vertex(m,(float)b.x,(float)b.y,(float)b.z).color(o.r(),o.g(),o.b(),255).normal(n,(float)normal.x,(float)normal.y,(float)normal.z).endVertex();}}
    private static void thick(PoseStack pose,MultiBufferSource consumers,Outline o,Vec3 camera,float strength){VertexConsumer c=consumers.getBuffer(RenderType.debugQuads());Matrix4f m=pose.last().pose();double half=o.width*0.5*strength;int alpha=Math.max(1,Math.round(255*strength));for(int[] e:EDGES){Vec3 from=o.corners[e[0]],to=o.corners[e[1]],dir=to.subtract(from);double len=dir.length();if(len<1e-6)continue;dir=dir.scale(1/len);Vec3 side=dir.cross(camera.subtract(from.add(to).scale(0.5)));if(side.lengthSqr()<1e-12)side=dir.cross(Math.abs(dir.y)<0.9?new Vec3(0,1,0):new Vec3(1,0,0));side=side.normalize().scale(half);quad(c,m,from.add(side).subtract(camera),o,alpha);quad(c,m,from.subtract(side).subtract(camera),o,alpha);quad(c,m,to.subtract(side).subtract(camera),o,alpha);quad(c,m,to.add(side).subtract(camera),o,alpha);}}
    private static void quad(VertexConsumer c,Matrix4f m,Vec3 p,Outline o,int alpha){c.vertex(m,(float)p.x,(float)p.y,(float)p.z).color(o.r(),o.g(),o.b(),alpha).endVertex();}
    private static float smooth(float value){float x=Math.max(0,Math.min(1,value));return x*x*(3-2*x);}

    private static final class Outline{Vec3[] corners;int color;float width;boolean selected,refreshed;float strength;Outline(Vec3[] c,int color,boolean selected,float width){update(c,color,selected,width);}void beginFrame(){refreshed=false;selected=false;}void update(Vec3[] c,int color,boolean selected,float width){corners=c;this.color=color;refreshed=true;this.selected=selected;if(selected){this.width=width;strength=1;}}void advance(float d){if(!selected&&strength>0)strength=Math.max(0,strength-d/FADE);}boolean visible(){return refreshed||strength>MIN;}Vec3 center(){Vec3 c=Vec3.ZERO;for(Vec3 p:corners)c=c.add(p);return c.scale(1.0/corners.length);}boolean contains(Vec3 p){return p.x>=corners[0].x&&p.x<=corners[6].x&&p.y>=corners[0].y&&p.y<=corners[6].y&&p.z>=corners[0].z&&p.z<=corners[6].z;}int r(){return color>>16&255;}int g(){return color>>8&255;}int b(){return color&255;}}
}
