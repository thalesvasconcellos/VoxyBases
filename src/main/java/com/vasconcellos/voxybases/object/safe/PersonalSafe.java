package com.vasconcellos.voxybases.object.safe;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.google.common.base.Strings;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.SafeValue;
import com.vasconcellos.voxybases.inventory.PersonalSafeInventory;
import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.util.TimeFormatter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public class PersonalSafe extends Safe {

    private final UUID owner;
    private final String name;

    private final List<String> trusted = new ArrayList<>();

    public PersonalSafe(UUID id, Location location, Player player) {
        this(id, location, player.getUniqueId(), player.getName());
    }

    public PersonalSafe(UUID id, Location location, UUID owner, String name) {
        super(id, location);

        this.owner = owner;
        this.name = name;

        double x = location.getX();
        double z = location.getZ();

        hologram = HologramsAPI.createHologram(VoxyBases.getInstance(), location.clone()
                .add(0.5, SafeValue.get(SafeValue::safeHologramHeight), 0.5));
    }

    public void add(String name) {
        trusted.add(name);
    }

    public void remove(String name) {
        trusted.remove(name);
    }

    public boolean isFriend(String name) {
        return trusted.contains(name);
    }

    public boolean isOwner(UUID uniqueId) {
        return owner.equals(uniqueId) || Bukkit.getPlayer(uniqueId)
                .hasPermission(SafeValue.get(SafeValue::openPermission));
    }

    @Override
    public void open(Player player) {
        PersonalSafeInventory inventory = new PersonalSafeInventory(this, player);

        inventory.init();
        inventory.openInventory(player);
    }

    @Override
    public void openStorage(Player player, int index) {
        Inventory inventory = storages.get(index);

        if(inventory.getViewers().size() > 0) {
            player.sendMessage("§6• §cUm jogador já está com este armazém aberto, aguarde.");
            player.closeInventory();

            return;
        }

        player.openInventory(inventory);
    }

    @Override
    public void updateHologram() {
        List<String> lines = new ArrayList<>();

        switch (state) {
            case NORMAL:
                lines = SafeValue.get(SafeValue::safeHologram);

                break;
            case HACKING:
                lines = SafeValue.get(SafeValue::safeHackingHologram);

                lines = lines.stream().map(line -> line.replace("{player}", name)
                                .replace("{tempo_segurança}", TimeFormatter.format
                                        (hacking.getEnd() - System.currentTimeMillis()))
                                .replace("{tempo_segurança_animação}", getProgressBar(hacking.getStartedAt(),
                                        hacking.getEnd(),10, ":", "§a", "§7")))
                        .collect(Collectors.toList());

                break;
            case HACKED:
                lines = SafeValue.get(SafeValue::safeHackedHologram);

                lines = lines.stream().map(line -> line.replace("{player}", name)
                                .replace("{time}", TimeFormatter.format
                                        (hacking.getTime() * 1000L)))
                        .collect(Collectors.toList());

                break;
            case INTERRUPTED:
                lines = SafeValue.get(SafeValue::safeStopHologram);

                break;
        }

        lines = lines.stream().map(line -> line.replace("{player}", name)
                .replace("{nivel_segurança}", String.valueOf(security)))
                .collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger();

        lines.forEach(line -> {
            try {
                TextLine textLine = (TextLine) hologram.getLine(index.getAndIncrement());
                textLine.setText(line);
            } catch (IndexOutOfBoundsException exception) {
                hologram.appendTextLine(line);
            };
        });
    }

    @Override
    public boolean isTrusted(Player player) {
        return owner.equals(player.getUniqueId()) ||
                trusted.contains(player.getName());
    }

    private String getProgressBar(long current, long max, int totalBars, String symbol, String color0, String color1) {
        float percent = Math.abs((float) (((current - System.currentTimeMillis()) * 100 / (max - current))) / 100);
        int progressBars = (int) (totalBars * percent);

        return color0 + Strings.repeat(symbol, progressBars) + color1 +
                Strings.repeat(symbol, totalBars - progressBars);
    }
}