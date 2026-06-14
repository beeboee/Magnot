package com.beeboee.magnot.server;

import com.beeboee.magnot.debug.MagnotDebug;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.nio.file.Path;

public final class MagnotServerEvents {
    private MagnotServerEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("magnot")
                        .then(Commands.literal("log")
                                .executes(context -> {
                                    Path logPath = MagnotDebug.logPath();
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
}
