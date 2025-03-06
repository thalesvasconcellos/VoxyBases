package com.vasconcellos.voxybases.object;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import com.vasconcellos.voxybases.util.Banners;
import com.vasconcellos.voxybases.util.ItemBuilder;
import lombok.Data;
import lombok.Getter;
import net.voxymc.clans.api.VoxyClansAPI;
import net.voxymc.clans.model.Clan;
import net.voxymc.clans.model.ClanPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Base {

    @Getter
    private static ItemStack hack, stop, gateHack, gateStop;

    static {
        hack = new ItemBuilder(Material.getMaterial(BaseValue.get(BaseValue
                ::safeHackMaterial).split(":")[0].toUpperCase()))
                .displayname(BaseValue.get(BaseValue::safeHackName))
                .lore(BaseValue.get(BaseValue::safeHackLore))
                .damage(Short.parseShort(BaseValue.get(BaseValue
                        ::safeHackMaterial).split(":")[1]))
                .build();

        stop = new ItemBuilder(Material.getMaterial(BaseValue.get(BaseValue
                ::safeStopMaterial).split(":")[0].toUpperCase()))
                .displayname(BaseValue.get(BaseValue::safeStopName))
                .lore(BaseValue.get(BaseValue::safeStopLore))
                .damage(Short.parseShort(BaseValue.get(BaseValue
                        ::safeStopMaterial).split(":")[1]))
                .build();

        gateHack = new ItemBuilder(Material.getMaterial(BaseValue.get(BaseValue
                ::gateHackMaterial).split(":")[0].toUpperCase()))
                .displayname(BaseValue.get(BaseValue::gateHackName))
                .lore(BaseValue.get(BaseValue::gateHackLore))
                .damage(Short.parseShort(BaseValue.get(BaseValue
                        ::gateHackMaterial).split(":")[1]))
                .build();

        gateStop = new ItemBuilder(Material.getMaterial(BaseValue.get(BaseValue
                ::gateStopMaterial).split(":")[0].toUpperCase()))
                .displayname(BaseValue.get(BaseValue::gateStopName))
                .lore(BaseValue.get(BaseValue::gateStopLore))
                .damage(Short.parseShort(BaseValue.get(BaseValue
                        ::gateStopMaterial).split(":")[1]))
                .build();
    }

    private final World world;
    private final ProtectedRegion region;

    private Clan clan;

    private Map<String, Gate> gates = new HashMap<>();
    private List<Location> banners = new ArrayList<>();

    private ClanSafe safe;

    private Invasion invasion, lastInvasion;
    private Alarm alarm;

    private Location spawn;

    public boolean open = false;

    private File file;

    public String getId() {
        return region.getId();
    }

    public void open(boolean animation,Runnable progress) { open = true;
        gates.values().forEach(gate -> gate.open(animation, progress));
    }

    public void open(boolean animation) {
        open(animation, null);
    }

    public void close(boolean animation, Runnable progress) { open = false;
        gates.values().forEach(gate -> gate.close(animation, progress));
    }

    public void close(boolean animation) {
       close(animation, null);
    }

    public boolean isAlly(Clan target) {
        return clan != null && clan.getAllies().contains(target.getTag());
    }

    public boolean isAlly(Player player) {
        ClanPlayer clanPlayer = VoxyClansAPI.voxyclansapi.getPlayer(player);
        return clan != null && clanPlayer.hasClan() && isAlly(clanPlayer.getClan());
    }

    public void setupBanners() {
        String tag = clan == null ? null : clan.getTag();

        for(int i = 0; i < 3; i++) {
            if(banners.size() < i + 1) continue;

            Block block = banners.get(i).getBlock();

            if(!block.getType().name().endsWith("_BANNER")) continue;

            Banner banner = (Banner) block.getState();

            if(tag == null) {
                banner.setBaseColor(DyeColor.WHITE);
                banner.setPatterns(new ArrayList<>());

                banner.update();

                continue;
            }

            Banners pattern = Banners.getByChar(tag.charAt(i));

            banner.setBaseColor(pattern.getBase());
            banner.setPatterns(pattern.getPatterns());

            banner.update();
        }
    }
}