package com.vasconcellos.voxybases.adapter;

import com.google.gson.*;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.object.Base;
import com.vasconcellos.voxybases.object.Gate;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import com.vasconcellos.voxybases.util.LocationUtils;
import net.voxymc.clans.api.VoxyClansAPI;
import net.voxymc.clans.model.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

public class BaseAdapter implements JsonSerializer<Base>, JsonDeserializer<Base> {

    @Override
    public Base deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();

        World world = Bukkit.getWorld(object.get("world").getAsString());
        ProtectedRegion region = VoxyBases.getInstance().getWorldGuard().getRegionManager(world)
                .getRegion(object.get("region").getAsString());

        JsonElement tag = object.get("tag");
        Clan clan = tag == null ? null : VoxyClansAPI.voxyclansapi.getClan(tag.getAsString());

        Map<String, Gate> gates = object.getAsJsonObject("gates").entrySet().stream().collect(Collectors
                .toMap(Entry::getKey, entry -> Gate.fromJson(entry.getValue().getAsJsonObject())));

        List<Location> banners = new ArrayList<>();
        object.getAsJsonArray("banners").forEach(location ->
                banners.add(LocationUtils.deserialize(location)));

        JsonElement id = object.get("safe");
        ClanSafe safe = id == null ? null : (ClanSafe) VoxyBases.getInstance()
                .getSafeManager().getById(UUID.fromString(id.getAsString()));

        JsonElement location = object.get("spawn");
        Location spawn = location == null ? null : LocationUtils.deserialize(location);

        Base base = new Base(world, region);

        base.setClan(clan);
        base.setGates(gates);
        base.setBanners(banners);
        base.setSafe(safe);
        base.setSpawn(spawn);

        if(safe != null) {
            safe.setBase(base);
            safe.updateHologram();
        }

        return base;
    }

    @Override
    public JsonElement serialize(Base base, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("world", base.getWorld().getName());
        object.addProperty("region", base.getRegion().getId());
        object.addProperty("tag", base.getClan() == null ? null : base.getClan().getTag());

        JsonObject gates = new JsonObject();
        base.getGates().forEach((key, value) -> gates.add(key, value.toJson()));

        object.add("gates", gates);

        JsonArray banners = new JsonArray();
        base.getBanners().forEach(location -> banners.add(LocationUtils.serialize(location)));

        object.add("banners", banners);

        object.addProperty("safe", base.getSafe() == null ? null
                : base.getSafe().getId().toString());

        object.add("spawn", base.getSpawn() == null ? null
                : LocationUtils.serialize(base.getSpawn()));

        return object;
    }
}
