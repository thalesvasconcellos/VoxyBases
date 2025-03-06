package com.vasconcellos.voxybases.object.safe;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.google.common.base.Strings;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.inventory.ClanSafeInventory;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.util.TimeFormatter;
import lombok.Getter;
import lombok.Setter;
import net.voxymc.clans.api.VoxyClansAPI;
import net.voxymc.clans.enums.Roles;
import net.voxymc.clans.model.Clan;
import net.voxymc.clans.model.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ClanSafe extends Safe {

    @Getter @Setter private Base base;

    public ClanSafe(UUID id, Location location) {
        super(id, location);

        double x = location.getX();
        double z = location.getZ();

        hologram = HologramsAPI.createHologram(VoxyBases.getInstance(), location.clone()
                .add(0.5, BaseValue.get(BaseValue::safeHologramHeight), 0.5));
    }

    public boolean isLeader(String name) {
        Clan clan = base.getClan();

        if(clan == null) return false;

        return clan.getLeader().equalsIgnoreCase(name);
    }

    @Override
    public void open(Player player) {
        ClanSafeInventory inventory = new ClanSafeInventory(this, player);

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
                lines = base.getClan() == null ? BaseValue.get(BaseValue::safeNoClanHologram) :
                        BaseValue.get(BaseValue::safeHologram);

                break;
            case HACKING:
                lines = BaseValue.get(BaseValue::safeHackingHologram);

                lines = lines.stream().map(line -> line.replace("{tempo_segurança}", TimeFormatter.format
                                        (hacking.getEnd() - System.currentTimeMillis()))
                                .replace("{tempo_segurança_animação}", getProgressBar(hacking.getStartedAt(),
                                        hacking.getEnd(),10, ":", "§a", "§7")))
                        .collect(Collectors.toList());

                break;
            case HACKED:
                lines = BaseValue.get(BaseValue::safeHackedHologram);

                lines = lines.stream().map(line -> line.replace("{time}", TimeFormatter
                                .format(hacking.getTime() * 1000L)))
                        .collect(Collectors.toList());

                break;
            case INTERRUPTED:
                lines = BaseValue.get(BaseValue::safeStopHologram);

                break;
            case TAKED:
                lines = BaseValue.get(BaseValue::safeTakedHologram);
                lines = lines.stream().map(line -> line.replace("{clan_colored_tag}", base.getClan()
                        .getColoredTag())).collect(Collectors.toList());

                break;
        }

        lines = lines.stream().map(line -> line.replace("{clan_colored_tag}", base.getClan() == null
                ? "" : base.getClan().getColoredTag()).replace("{nivel_segurança}",
                String.valueOf(security))).collect(Collectors.toList());

        AtomicInteger index = new AtomicInteger();

        lines.forEach(line -> {
            TextLine textLine;

            try {
                textLine = (TextLine) hologram.getLine(index.getAndIncrement());
                textLine.setText(line);
            } catch (IndexOutOfBoundsException exception) {
                textLine = hologram.appendTextLine(line);
            }

            if(base.getClan() == null)
                textLine.setTouchHandler(player -> {
                    ClanPlayer clanPlayer = VoxyClansAPI.voxyclansapi.getPlayer(player);

                    if(!clanPlayer.hasClan()) {
                        player.sendMessage("§6• §cVocê precisa de um clan para dominar esta base.");

                        return;
                    }

                    if(clanPlayer.getRole() != Roles.LEADER) {
                        player.sendMessage("§6• §cVocê precisa ser o líder para dominar uma base.");

                        return;
                    }

                    Clan clan = clanPlayer.getClan();

                    if(VoxyBases.getInstance().getBaseManager().getByClan(clan) != null) {
                        player.sendMessage(BaseValue.get(BaseValue::safeAlreadyHaveMessage));

                        return;
                    }

                    Bukkit.getOnlinePlayers().stream()
                            .filter(target -> !clan.getMembers().contains(target.getName()))
                            .filter(target -> base.getRegion().contains(
                                            target.getLocation().getBlockX(),
                                            target.getLocation().getBlockY(),
                                            target.getLocation().getBlockZ()
                                    )).forEach(target -> target.teleport(base.getSpawn()));

                    base.setClan(clan);
                    base.setupBanners();

                    hologram.clearLines();
                    updateHologram();

                    base.close(true);

                    player.sendMessage("§6• §aBase dominada pelo seu clan com sucesso!");
                });
        });
    }

    @Override
    public boolean isTrusted(Player player) {
        return base.getClan() != null && (base.getClan().getMembers()
                .contains(player.getName()) || base.getClan().getLeader()
                .equalsIgnoreCase(player.getName()));
    }

    private String getProgressBar(long current, long max, int totalBars, String symbol, String color0, String color1) {
        float percent = Math.abs((float) (((current - System.currentTimeMillis()) * 100 / (max - current))) / 100);
        int progressBars = (int) (totalBars * percent);

        return color0 + Strings.repeat(symbol, progressBars) + color1 +
                Strings.repeat(symbol, totalBars - progressBars);
    }
}