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
import com.vasconcellos.voxybases.util.TimeFormatter;
import net.milkbowl.vault.economy.Economy;
import net.voxymc.clans.model.Clan;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SecurityInventory extends SimpleInventory {

    private final Safe safe;

    public SecurityInventory(Safe safe) {
        super("inventory.security", "COFRE - Sistemas de Segurança", 9 * 3);

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
        ).defaultCallback(click -> safe.open(player)));

        for (int i = 10; i <= 14; i++) {
            int level = i - 9;

            editor.setItem(i, InventoryItem.of(
                    new ItemBuilder(safe.getSecurity() >= level ? SkullCreator.ALARM_ON.clone()
                            : SkullCreator.ALARM_OFF.clone())
                            .displayname((safe.getSecurity() >= level ? "§a" : "§c")
                                    + "SEGURANÇA #" + level)
                            .lore(safe.getSecurity() >= level ?
                                    Arrays.asList(" ", "§7Nível: §e" + level, "§7Tempo de proteção: §c" +
                                                    TimeFormatter.format(safe.getTime()),
                                            " ", "§7STATUS: §a§oDESBLOQUEADO!", " ") :
                                    Arrays.asList(" ", "§7Nível: §e" + level, "§7Tempo de proteção: §c" +
                                                    TimeFormatter.format(safe.getTime()),
                                            " ", "§7STATUS: §c§oBLOQUEADO!", "§7CUSTO: " + getPrice(level),
                                            " ", "§7Clique aqui para desbloquear!"))
                            .build()
            ).defaultCallback(click -> {
                if(safe.getSecurity() >= level) return;
                if(level != safe.getSecurity() + 1) {
                    player.sendMessage("§6• §cPara desbloquear este nível de segurança," +
                            " é necessário desbloquear o anterior!");
                    player.closeInventory();

                    return;
                }

                if(safe instanceof PersonalSafe) {
                    Economy economy = VoxyBases.getInstance().getEconomy();

                    if(!economy.has(player, getPrice(level))) {
                        player.sendMessage("§6• §cVocê não tem saldo suficiente para evoluir.");
                        player.closeInventory();

                        return;
                    }

                    economy.withdrawPlayer(player, getPrice(level));

                } else {
                    ClanSafe clanSafe = (ClanSafe) safe;

                    Base base = clanSafe.getBase();
                    Clan clan = base.getClan();

                    if(clan == null) return;

                    if(clan.getMoney() < getPrice(level)) {
                        player.sendMessage("§6• §cSeu clan não tem saldo suficiente para evoluir.");
                        player.closeInventory();

                        return;
                    }

                    clan.setMoney(clan.getMoney() - getPrice(level));

                }

                safe.setSecurity(safe.getSecurity() + 1);
                safe.updateHologram();

                player.sendMessage("§6• §aNível de segurança aumentado para " + safe.getSecurity());
                player.closeInventory();
            }));
        }
    }

    public double getPrice(int level) {
        if(safe instanceof PersonalSafe)
            switch (level) {
                case 2: return SafeValue.get(SafeValue::levelTwoSecurityHackPrice);
                case 3: return SafeValue.get(SafeValue::levelThreeSecurityHackPrice);
                case 4: return SafeValue.get(SafeValue::levelFourSecurityHackPrice);
                case 5: return SafeValue.get(SafeValue::levelFiveSecurityHackPrice);
                default: return 0;
            }
        else switch (level) {
            case 2: return BaseValue.get(BaseValue::levelTwoSecurityHackPrice);
            case 3: return BaseValue.get(BaseValue::levelThreeSecurityHackPrice);
            case 4: return BaseValue.get(BaseValue::levelFourSecurityHackPrice);
            case 5: return BaseValue.get(BaseValue::levelFiveSecurityHackPrice);
            default: return 0;
        }
    }
}