package com.vasconcellos.voxybases.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.config.SafeValue;
import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import com.vasconcellos.voxybases.util.ChatHelper;
import com.vasconcellos.voxybases.util.ItemBuilder;
import com.vasconcellos.voxybases.util.SkullCreator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PersonalSafeInventory extends SimpleInventory {

    private final PersonalSafe safe;
    private final Player player;

    public PersonalSafeInventory(PersonalSafe safe, Player player) {
        super("inventory.personal.inventory", "§7COFRE - PESSOAL" + (safe.isOwner(player.getUniqueId())
                ? "" : " (AMIGO)"), 9 * (safe.isOwner(player.getUniqueId()) ? 6 : 3));

        this.safe = safe;
        this.player = player;
    }

    @Override
    protected void configureInventory(Viewer viewer, InventoryEditor editor) {
        boolean owner = safe.isOwner(player.getUniqueId());

        editor.setItem(11, InventoryItem.of(
                new ItemBuilder(SkullCreator.CHEST01.clone())
                        .displayname("§eArmazéns")
                        .lore(" ", "§aClique aqui para acessar!", " ")
                        .build()
        ).defaultCallback(click -> {
            StorageInventory inventory = new StorageInventory(safe);

            inventory.init();
            inventory.openInventory(player);
        }));

        if(owner) {
            editor.setItem(15, InventoryItem.of(
                    new ItemBuilder(SkullCreator.ALARM_ON.clone())
                            .displayname("§cSistema de Segurança")
                            .lore(" ", "§aClique aqui para acessar!", " ")
                            .build()
            ).defaultCallback(click -> {
                SecurityInventory inventory = new SecurityInventory(safe);

                inventory.init();
                inventory.openInventory(player);
            }));

            editor.setItem(29, InventoryItem.of(
                    new ItemBuilder(SkullCreator.HEAD01.clone())
                            .displayname("§aAdicionar AMIGO")
                            .lore(" ", "§7Com esta opção, você pode adicionar amigos",
                                    "§7em seus Armazéns, dando acesso total!", " ",
                                    "§aClique aqui para adicionar amigos!", " ")
                            .build()
            ).defaultCallback(click -> {
                player.sendMessage("§6• §7Digite o nome do jogador que deseja adicionar no chat!");
                player.sendMessage("§6• §7Digite §c§lCANCELAR §7para cancelar a ação.");

                player.closeInventory();

                new ChatHelper(player)
                        .cancelMessage("§6• §cAção cancelada com sucesso!")
                        .execute(name -> {
                            if (name.equalsIgnoreCase(player.getName())) {
                                player.sendMessage("§6• §cVocê não pode adicionar" +
                                        " você mesmo na lista de amigos!");

                                return;
                            }

                            Player target = Bukkit.getPlayerExact(name);

                            if (target == null) {
                                player.sendMessage("§6• §cO jogador §e" + name
                                        + " §cnão encontra-se on-line!");

                                return;
                            }

                            if(safe.isFriend(name)) {
                                player.sendMessage("§6• §cO jogador §e" + name
                                        + " §cjá está adicionado a este cofre!");

                                return;
                            }

                            safe.add(name);

                            player.sendMessage("§6• §7Jogador §a" + name + " §7adicionado com sucesso!");
                        });
            }));

            editor.setItem(38, InventoryItem.of(
                    new ItemBuilder(SkullCreator.HEAD02.clone())
                            .displayname("§cRemover AMIGO")
                            .lore(" ", "§7Com esta opção, você pode remover amigos",
                                    "§7que foram adicionados em seus Armazéns.", " ",
                                    "§aClique aqui para remover amigos!", " ")
                            .build()
            ).callback(ClickType.LEFT, click -> player.sendMessage("§6• §7Amigos: §e" + (safe.getTrusted().size()
                            <= 0 ? "Nenhum" : StringUtils.join(safe.getTrusted(), ", "))))
                    .callback(ClickType.RIGHT, click -> {
                        player.sendMessage("§6• §7Digite o nome do jogador que deseja remover no chat!");
                        player.sendMessage("§6• §7Digite §c§lCANCELAR §7para cancelar a ação.");

                        player.closeInventory();

                        new ChatHelper(player)
                                .cancelMessage("§6• §cAção cancelada com sucesso!")
                                .execute(name -> {
                                    if (name.equalsIgnoreCase(player.getName())) {
                                        player.sendMessage("§6• §cVocê não pode remover" +
                                                " você mesmo da lista de amigos!");

                                        return;
                                    }

                                    Player target = Bukkit.getPlayerExact(name);

                                    if (target == null) {
                                        player.sendMessage("§6• §cO jogador §e" + name
                                                + " §cnão encontra-se on-line!");

                                        return;
                                    }

                                    if(!safe.isFriend(name)) {
                                        player.sendMessage("§6• §cO jogador §e" + name
                                                + " §cnão está adicionado a este cofre!");

                                        return;
                                    }

                                    safe.remove(name);

                                    player.sendMessage("§6• §7Jogador §a" + name + " §7removido com sucesso!");
                                });
                    }));

            editor.setItem(33, InventoryItem.of(
                    new ItemBuilder(SkullCreator.KEYHOLE.clone())
                            .displayname("§cRecolher Cofre")
                            .lore(" ", "§aClique aqui para recolher!", " ")
                            .build()
            ).defaultCallback(click -> {
                for(Inventory inventory : safe.getStorages().values())
                    if(!isClear(inventory)) {
                        player.sendMessage(SafeValue.get(SafeValue::recallFullSafeMessage));
                        player.closeInventory();

                        return;
                    }

                safe.getLocation().getBlock().setType(Material.AIR);
                safe.getHologram().delete();

                VoxyBases.getInstance().getSafeService().delete(safe);
                VoxyBases.getInstance().getSafeManager().remove(safe);

                player.sendMessage(SafeValue.get(SafeValue::recallSafeMessage));

                player.getInventory().addItem(Safe.getItem());
                player.closeInventory();
            }));
        }

        editor.setItem(owner ? 42 : 15, InventoryItem.of(
                new ItemBuilder(Material.INK_SACK)
                        .damage((short) 8)
                        .displayname("§cFECHAR")
                        .lore(" ", "§cClique aqui para fechar o menu!", " ")
                        .build()
        ).defaultCallback(click -> player.closeInventory()));
    }

    private static boolean isClear(Inventory inventory) {
        for (ItemStack item : inventory.getContents())
            if (item != null && item.getType() != Material.AIR)
                return false;

        return true;
    }
}