package com.vasconcellos.voxybases.adapter;

import com.google.gson.*;
import com.vasconcellos.voxybases.object.Safe;
import com.vasconcellos.voxybases.object.safe.ClanSafe;
import com.vasconcellos.voxybases.object.safe.PersonalSafe;
import com.vasconcellos.voxybases.util.InventoryUtils;
import com.vasconcellos.voxybases.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SafeAdapter implements JsonSerializer<Safe>, JsonDeserializer<Safe> {

    @Override
    public Safe deserialize(JsonElement element, Type t, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();
        Safe safe;

        UUID id = UUID.fromString(object.get("id").getAsString());
        int type = object.get("type").getAsInt();

        Location location = LocationUtils.deserialize(object.get("location"));
        int security = object.get("security").getAsInt();

        Map<Integer, Inventory> storages = object.getAsJsonObject("storages").entrySet().stream()
                .collect(Collectors.toMap(entry -> Integer.parseInt(entry.getKey()), entry ->
                        InventoryUtils.deserialize(entry.getValue().getAsJsonPrimitive())));

        if(type == 0) {
            safe = new PersonalSafe(id, location, UUID.fromString(object.get("owner").getAsString()),
                    object.get("name").getAsString());

            JsonArray array = object.get("trusted").getAsJsonArray();
            for(int i = 0; i < array.size(); i++)
                ((PersonalSafe) safe).add(array.get(i).getAsString());
        } else safe = new ClanSafe(id, location);

        safe.setSecurity(security);
        safe.setStorages(storages);

        if(type == 0) safe.updateHologram();

        return safe;
    }

    public JsonElement serialize(Safe safe, Type t, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("id", safe.getId().toString());
        obj.addProperty("type", safe instanceof PersonalSafe ? 0 : 1);
        obj.add("location", LocationUtils.serialize(safe.getLocation()));

        obj.addProperty("security", safe.getSecurity());

        JsonObject storages = new JsonObject();
        safe.getStorages().forEach((key, value) -> storages.add(String.valueOf(key),
                InventoryUtils.serialize(value)));

        obj.add("storages", storages);

        if(safe instanceof PersonalSafe) {
            PersonalSafe personal = (PersonalSafe) safe;

            obj.addProperty("owner", personal.getOwner().toString());
            obj.addProperty("name", personal.getName());

            JsonArray array = new JsonArray();
            for(int i = 0; i < personal.getTrusted().size(); i++)
                array.add(new JsonPrimitive(personal.getTrusted()
                        .get(i).toString()));

            obj.add("trusted", array);
        } else obj.addProperty("base", ((ClanSafe) safe).getBase().getId());

        return obj;
    }
}