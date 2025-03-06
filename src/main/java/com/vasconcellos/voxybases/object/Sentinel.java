package com.vasconcellos.voxybases.object;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vasconcellos.voxybases.config.BaseValue;
import com.vasconcellos.voxybases.util.LocationUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

@Getter
public class Sentinel {

    private final Location base, top;
    private ArmorStand stand;

    @Setter private int ammo;
    @Setter private long lastShot;

    public Sentinel(Location base) {
        this.base = base;

        top = base.clone();

        top.setX(top.getBlockX() + 0.5D);
        top.setY(top.getBlockY() + BaseValue.get(BaseValue::sentinelHeight));
        top.setZ(top.getBlockZ() + 0.5D);

        render();
    }

    public Sentinel(Location base, int ammo) {
        this(base);

        this.ammo = ammo;
    }

    public void render() {
        base.getBlock().setType(Material.EMERALD_BLOCK);

        stand = (ArmorStand) top.getWorld().getNearbyEntities(top, 1.0, 1.0, 1.0).stream().filter(entity ->
                entity.getType() == EntityType.ARMOR_STAND).findFirst().orElse(null);

        if(stand == null) stand = (ArmorStand) top.getWorld().spawnEntity(top, EntityType.ARMOR_STAND);

        stand.setGravity(false);
        stand.setVisible(false);
        stand.setBasePlate(false);

        stand.setHelmet(new ItemStack(Material.OBSIDIAN));
    }

    public void destroy() {
        stand.remove();

        base.getBlock().setType(Material.AIR);
    }

    public Location getLocation() {
        return base;
    }

    public JsonElement toJson() {
        JsonObject object = new JsonObject();

        object.add("location", LocationUtils.serialize(base));
        object.addProperty("ammo", ammo);

        return object;
    }

    public static Sentinel fromJson(JsonObject element) {
        return new Sentinel(LocationUtils.deserialize(element.get("location")),
                element.get("ammo").getAsInt());
    }
}