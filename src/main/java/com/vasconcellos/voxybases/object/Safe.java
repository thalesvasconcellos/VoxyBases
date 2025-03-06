package com.vasconcellos.voxybases.object;


import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.config.SafeValue;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import com.vasconcellos.voxybases.util.ItemBuilder;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public abstract class Safe {

    @Getter private static ItemStack item, hack, stop;

    static {
        item = new ItemBuilder(Material.getMaterial(SafeValue.get(SafeValue
                ::safeMaterial).split(":")[0].toUpperCase()))
                .displayname(SafeValue.get(SafeValue::safeName))
                .lore(SafeValue.get(SafeValue::safeLore))
                .damage(Short.parseShort(SafeValue.get(SafeValue
                        ::safeMaterial).split(":")[1]))
                .build();

        hack = new ItemBuilder(Material.getMaterial(SafeValue.get(SafeValue
                ::hackMaterial).split(":")[0].toUpperCase()))
                .displayname(SafeValue.get(SafeValue::hackName))
                .lore(SafeValue.get(SafeValue::hackLore))
                .damage(Short.parseShort(SafeValue.get(SafeValue
                        ::hackMaterial).split(":")[1]))
                .build();

        stop = new ItemBuilder(Material.getMaterial(SafeValue.get(SafeValue
                ::stopMaterial).split(":")[0].toUpperCase()))
                .displayname(SafeValue.get(SafeValue::stopName))
                .lore(SafeValue.get(SafeValue::stopLore))
                .damage(Short.parseShort(SafeValue.get(SafeValue
                        ::stopMaterial).split(":")[1]))
                .build();
    }

    protected final UUID id;

    protected final Location location;

    protected int security = 1;
    protected Map<Integer, Inventory> storages = new HashMap<>();

    protected SafeState state = SafeState.NORMAL;
    protected Hologram hologram;

    protected Hacking hacking;

    private File file;

    public abstract void open(Player player);
    public abstract void openStorage(Player player, int index);

    public abstract void updateHologram();

    public abstract boolean isTrusted(Player player);

    public void setState(SafeState state) {
        this.state = state;

        hologram.clearLines();
    }

    public long getTime() {
        if(this instanceof PersonalSafe)
            switch (security) {
                case 1: return SafeValue.get(SafeValue::levelOneSecurityHackTime) * 1000L;
                case 2: return SafeValue.get(SafeValue::levelTwoSecurityHackTime) * 1000L;
                case 3: return SafeValue.get(SafeValue::levelThreeSecurityHackTime) * 1000L;
                case 4: return SafeValue.get(SafeValue::levelFourSecurityHackTime) * 1000L;
                case 5: return SafeValue.get(SafeValue::levelFiveSecurityHackTime) * 1000L;
                default: return 0;
            }
        else switch (security) {
            case 1: return BaseValue.get(BaseValue::levelOneSecurityHackTime) * 1000L;
            case 2: return BaseValue.get(BaseValue::levelTwoSecurityHackTime) * 1000L;
            case 3: return BaseValue.get(BaseValue::levelThreeSecurityHackTime) * 1000L;
            case 4: return BaseValue.get(BaseValue::levelFourSecurityHackTime) * 1000L;
            case 5: return BaseValue.get(BaseValue::levelFiveSecurityHackTime) * 1000L;
            default: return 0;
        }
    }
}