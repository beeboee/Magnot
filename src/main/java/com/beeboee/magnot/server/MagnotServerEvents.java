package com.beeboee.magnot.server;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousMagnetRules;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.nio.file.Path;

public final class MagnotServerEvents {
    private static final ResourceLocation ARTIFACTS_MAGNETISM = ResourceLocation.fromNamespaceAndPath("artifacts", "magnetism");
    private static final double ARTIFACTS_MAGNETISM_FALLBACK_RANGE = 12.0D;
    private static final double ARTIFACTS_MAGNETISM_FALLBACK_RANGE_SQR = ARTIFACTS_MAGNETISM_FALLBACK_RANGE * ARTIFACTS_MAGNETISM_FALLBACK_RANGE;

    private MagnotServerEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("magnot")
                        .then(Commands.literal("log")
                                .executes(context -> {
                                    Path logPath = Path.of("logs", "latest.log").toAbsolutePath().normalize();
                                    Component link = Component.literal(logPath.toString())
                                            .withStyle(ChatFormatting.UNDERLINE)
                                            .withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, logPath.toString())));
                                    context.getSource().sendSuccess(() -> Component.literal("Magnot log: ").append(link), false);
                                    return 1;
                                }))
        );
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack held = player.getItemInHand(hand);
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntityTickPost(EntityTickEvent.Post event) {
        if (!ModList.get().isLoaded("artifacts")) {
            return;
        }

        if (!(event.getEntity() instanceof ItemEntity itemEntity) || !(itemEntity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 motion = itemEntity.getDeltaMovement();
        if (motion.lengthSqr() < 1.0E-6D) {
            return;
        }

        Vec3 itemPosition = itemEntity.position();
        Vec3 itemCenter = itemEntity.getBoundingBox().getCenter();
        for (Player player : serverLevel.getEntitiesOfClass(Player.class, itemEntity.getBoundingBox().inflate(ARTIFACTS_MAGNETISM_FALLBACK_RANGE), MagnotServerEvents::hasArtifactsMagnetism)) {
            Vec3 source = player.position().add(0.0D, 0.75D, 0.0D);
            Vec3 itemToSource = source.subtract(itemCenter);
            if (itemToSource.lengthSqr() > ARTIFACTS_MAGNETISM_FALLBACK_RANGE_SQR || itemToSource.lengthSqr() < 1.0E-6D) {
                continue;
            }

            if (motion.normalize().dot(itemToSource.normalize()) <= 0.2D) {
                continue;
            }

            if (!FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, itemPosition)
                    && !FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, itemCenter)) {
                continue;
            }

            itemEntity.setDeltaMovement(Vec3.ZERO);
            Vec3 previousPosition = new Vec3(itemEntity.xo, itemEntity.yo, itemEntity.zo);
            if (previousPosition.distanceToSqr(itemPosition) <= 4.0D
                    && FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, previousPosition)) {
                itemEntity.setPos(previousPosition.x, previousPosition.y, previousPosition.z);
            }
            return;
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            MagnotNetwork.syncTo(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            MagnotNetwork.syncTo(serverPlayer);
        }
    }

    private static boolean hasArtifactsMagnetism(Player player) {
        return player.getActiveEffects().stream()
                .anyMatch(effect -> ARTIFACTS_MAGNETISM.equals(BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value())));
    }
}
