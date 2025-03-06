package com.vasconcellos.voxybases.util;

import com.google.common.collect.Maps;
import com.vasconcellos.voxybases.VoxyBases;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ChatHelper {

    public static final Map<UUID, Consumer<AsyncPlayerChatEvent>> CALLBACK = Maps.newHashMap();

    private final Player player;
    private String cancelMessage;

    static {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
                Consumer<AsyncPlayerChatEvent> consumer = ChatHelper.CALLBACK.get(event.getPlayer().getUniqueId());

                if (consumer != null) consumer.accept(event);
            }
        }, VoxyBases.getInstance());
    }

    public void execute(Consumer<String> consumer) {
        CALLBACK.putIfAbsent(player.getUniqueId(), event -> {
            event.setCancelled(true);
            cancel();

            if (event.getMessage().startsWith("cancel")) {
                if(cancelMessage != null) player.sendMessage(cancelMessage);

                return;
            }

            consumer.accept(event.getMessage());
        });
    }

    public ChatHelper cancelMessage(String message) {
        this.cancelMessage = message;

        return this;
    }

    private void cancel() {
        CALLBACK.remove(player.getUniqueId());
    }
}