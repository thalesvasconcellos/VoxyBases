package com.vasconcellos.voxybases.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.config.SafeValue;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import com.vasconcellos.voxybases.util.ItemBuilder;
import com.vasconcellos.voxybases.util.SkullCreator;
import net.milkbowl.vault.economy.Economy;
import net.voxymc.clans.model.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StorageInventory extends SimpleInventory {

    private final Safe safe;

    public StorageInventory(Safe safe) {
        super("inventory.storage","§7COFRE - Armazéns" , 9 * 3);

        this.safe = safe;
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        Player player = viewer.getPlayer();

        editor.setItem(16, InventoryItem.of(
                new ItemBuilder(Material.INK_SACK)
                        .displayname("§cVoltar")
                        .lore(" ", "§cClique aqui para voltar!", " ")
                        .damage((short) 8)
                        .build()
        ).defaultCallback(click -> {
            if(safe instanceof PersonalSafe) {
                if (!((PersonalSafe) safe).isOwner(player.getUniqueId()) &&
                        !((PersonalSafe) safe).isFriend(player.getName()))
                    player.closeInventory();
            } else if(safe instanceof ClanSafe) {
                ClanSafe clanSafe = (ClanSafe) safe;
                Base base = clanSafe.getBase();

                if(base.getClan() != null && !base.getClan().getMembers()
                        .contains(player.getName())) {
                    HackInventory inventory = new HackInventory(safe);

                    inventory.init();
                    inventory.openInventory(player);

                    return;
                }
            }

            safe.open(player);
        }));

        for (int i = 10; i <= 14; i++) {
            int level = i - 9;

            if(safe.getStorages().size() < level)
                if(safe instanceof PersonalSafe) {
                    if(!((PersonalSafe) safe).isOwner(player.getUniqueId())) continue;
                } else if (!safe.isTrusted(player)) continue;

            editor.setItem(i, InventoryItem.of(
                    new ItemBuilder(safe.getStorages().size() >= level ? SkullCreator.CHEST02.clone()
                            : SkullCreator.CHEST03.clone())
                            .displayname((safe.getStorages().size() >= level ? "§e" : "§c")
                                    + "Armazéns §7#" + level)
                            .lore(safe.getStorages().size() >= level ?
                                    Arrays.asList(" ", "§7STATUS: §a§oDESBLOQUEADO!", " ",
                                            "§7Clique aqui para utilizá-lo!") :
                                    Arrays.asList(" ", "§7STATUS: §c§oBLOQUEADO!",
                                            "§7CUSTO: " + getPrice(level), " ",
                                            "§7Clique aqui para desbloquear!"))
                            .build()
            ).defaultCallback(click -> {
                if(safe.getStorages().size() >= level) {
                    safe.openStorage(player, level);

                    return;
                }

                if(level != safe.getStorages().size() + 1) {
                    player.sendMessage("§6• §cPara desbloquear este armazém," +
                            " é necessário desbloquear o anterior!");
                    player.closeInventory();

                    return;
                }

                if(safe instanceof PersonalSafe) {
                    if(!((PersonalSafe) safe).isOwner(player.getUniqueId())) {
                        player.sendMessage("§6• §cVocê não pode adquirir este armazém " +
                                "pois você não é o dono do cofre.");
                        player.closeInventory();

                        return;
                    }

                    Economy economy = VoxyBases.getInstance().getEconomy();

                    if(!economy.has(player, getPrice(level))) {
                        player.sendMessage("§6• §cVocê não tem saldo suficiente" +
                                " para adquirir este armazém.");
                        player.closeInventory();

                        return;
                    }

                    economy.withdrawPlayer(player, getPrice(level));
                } else {
                    ClanSafe clanSafe = (ClanSafe) safe;

                    if(!clanSafe.isLeader(player.getName())) {
                        player.sendMessage("§6• §cVocê não pode adquirir este armazém " +
                                "pois você não é o líder do clan.");
                        player.closeInventory();
                        return;
                    }

                    Base base = clanSafe.getBase();
                    Clan clan = base.getClan();

                    if(clan == null) return;

                    if(clan.getMoney() < getPrice(level)) {
                        player.sendMessage("§6• §cSeu clan não tem saldo suficiente" +
                                " para adquirir este armazém.");
                        player.closeInventory();

                        return;
                    }

                    clan.setMoney(clan.getMoney() - getPrice(level));
                }

                safe.getStorages().put(safe.getStorages().size() + 1, Bukkit.createInventory(null,
                        9 * 6, "Armazém #" + (safe.getStorages().size() + 1)));

                player.sendMessage("§6• §aArmazém adquirido com sucesso.");
                player.closeInventory();
            }));
        }
    }

    public double getPrice( int level) {
        if(safe instanceof PersonalSafe)
            switch (level) {
                case 2: return SafeValue.get(SafeValue::levelTwoStorageHackPrice);
                case 3: return SafeValue.get(SafeValue::levelThreeStorageHackPrice);
                case 4: return SafeValue.get(SafeValue::levelFourStorageHackPrice);
                case 5: return SafeValue.get(SafeValue::levelFiveStorageHackPrice);
                default: return 0;
            }
        else switch (level) {
            case 2: return BaseValue.get(BaseValue::levelTwoStorageHackPrice);
            case 3: return BaseValue.get(BaseValue::levelThreeStorageHackPrice);
            case 4: return BaseValue.get(BaseValue::levelFourStorageHackPrice);
            case 5: return BaseValue.get(BaseValue::levelFiveStorageHackPrice);
            default: return 0;
        }
    }
}