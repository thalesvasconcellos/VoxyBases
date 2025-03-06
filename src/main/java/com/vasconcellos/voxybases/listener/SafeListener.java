package com.vasconcellos.voxybases.listener;

import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.config.SafeValue;
import com.vasconcellos.voxybases.inventory.HackInventory;
import com.vasconcellos.voxybases.object.*;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class SafeListener implements Listener {

    private final VoxyBases plugin;

    @SneakyThrows
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        if(!item.isSimilar(Safe.getItem())) return;
        if(plugin.getSafeManager().getSafesByOwner(player.getUniqueId()).size()
                >= SafeValue.get(SafeValue::safeQuantity)) {
            player.sendMessage("§6• §cVocê não pode colocar mais cofres!");
            event.setCancelled(true);

            return;
        }

        Block block = event.getBlock();
        Location location = block.getLocation();

        int radius = SafeValue.get(SafeValue::safeRadius);

        for(double x = location.getX() - radius; x <= location.getX() + radius; x++)
            for(double y = location.getY() - radius; y <= location.getY() + radius; y++)
                for(double z = location.getZ() - radius; z <= location.getZ() + radius; z++)
                    if(plugin.getSafeManager().get(new Location(block.getWorld(), x, y, z)) != null) {
                        player.sendMessage("§6• §cVocê não pode colocar um cofre a menos de " + radius + " blocos de outro!");
                        event.setCancelled(true);

                        return;
                    }

        PersonalSafe safe = new PersonalSafe(UUID.randomUUID(), block.getLocation(), player);

        safe.getStorages().put(1, Bukkit.createInventory(null, 9 * 6, "Armazém #1"));
        safe.updateHologram();

        File file = new File(plugin.getDataFolder(), "cofres" +
                File.separator + safe.getId() + ".json");
        file.createNewFile();

        safe.setFile(file);

        plugin.getSafeService().save(safe);
        plugin.getSafeManager().add(safe);

        player.sendMessage("§6• §aCofre colocado com sucesso!");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        Block block = event.getClickedBlock();

        Safe safe = plugin.getSafeManager().get(block.getLocation());

        if(safe == null) return; event.setCancelled(true);

        if(safe.isTrusted(player) || player.hasPermission(SafeValue.get(SafeValue::openPermission))) {
            if(safe.getState() == SafeState.HACKING) {
                if(!player.getItemInHand().isSimilar(safe instanceof PersonalSafe ? Safe.getStop() : Base.getStop())) {
                    player.sendMessage("§6• §cO cofre foi hackeado e não possível acessa-lo, impeça " +
                            "o hackeamento com um computador, para depois acessar o cofre novamente!");

                    return;
                }

                player.sendMessage("§6• §aVocê parou o hack do cofre com sucesso!");
                safe.getHacking().getHacker().sendMessage("§6• §eHack interrompido por §7" + player.getName() + "§e!");

                if (item.getAmount() == 1)
                    player.setItemInHand(null);
                else item.setAmount(item.getAmount() - 1);

                safe.getHacking().stop();

                return;
            }

            if(safe.getState() == SafeState.HACKED) {
                player.sendMessage("§6• §cO cofre está sendo hackeado, aguarde para acessa-lo!");

                return;
            }

            safe.open(player);
        } else {
            if(safe.getState() == SafeState.HACKING && safe.getHacking().isHacker(player)) {
                player.sendMessage("§6• §cHack em andamento, aguarde para ter acesso ao cofre.");

                return;
            }

            if(safe.getState() == SafeState.HACKED && safe.getHacking().getHacker().equals(player)) {
                HackInventory inventory = new HackInventory(safe);

                inventory.init();
                inventory.openInventory(player);

                return;
            }

            if(item == null ||  !item.isSimilar(safe instanceof PersonalSafe ? Safe.getHack() : Base.getHack())) {
                player.sendMessage("§6• §cÉ necessário de um computador para hackear este cofre.");

                return;
            }

            if(safe.getState() == SafeState.HACKING || safe.getState() == SafeState.HACKED) {
                player.sendMessage("§6• §cEste cofre já está sendo hackeado.");

                return;
            }

            if(safe instanceof PersonalSafe) {
                if (SafeValue.get(SafeValue::hackOfflinePlayer) && Bukkit.getPlayer
                        (((PersonalSafe) safe).getOwner()) == null) {
                    player.sendMessage(SafeValue.get(SafeValue::offlineMessage));

                    return;
                }

                player.sendMessage("§6• §aHack iniciado, aguarde para ter acesso ao cofre.");

                Player owner = Bukkit.getPlayer(((PersonalSafe) safe).getOwner());

                if(owner != null)
                    Bukkit.getPlayer(((PersonalSafe) safe).getOwner()).sendTitle(
                            SafeValue.get(SafeValue::alertTitle).get(0),
                            SafeValue.get(SafeValue::alertTitle).get(1)
                    );
            } else if(BaseValue.get(BaseValue::needToHack)) {
                ClanSafe clanSafe = (ClanSafe) safe;

                Base base = clanSafe.getBase();
                Invasion invasion = base.getInvasion();

                if(BaseValue.get(BaseValue::gateInvadeWithoutOnlinePlayers) && base.getClan().getMembers().stream()
                        .map(Bukkit::getPlayerExact).noneMatch(Objects::nonNull)) {
                    player.sendMessage(BaseValue.get(BaseValue::gateCantInvadeMessage));

                    return;
                }


                if(invasion == null || invasion.getStatus() != Invasion.InvasionStatus.COMPLETED) {
                    player.sendMessage("§6• §cVocê precisa primeiro hackear o" +
                            " portão para hackear o cofre desta base!");

                    return;
                }
            }

            if (item.getAmount() == 1)
                player.setItemInHand(null);
            else item.setAmount(item.getAmount() - 1);

            Hacking hacking = new Hacking(plugin.getBaseManager().getBySafe(safe), safe, player);
            hacking.start();
        }
    }
}