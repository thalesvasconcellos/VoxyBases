package com.vasconcellos.voxybases.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static Location deserialize(JsonElement json) {
        if ( !json.isJsonObject()) return null;

        final JsonObject obj = (JsonObject) json;
        final JsonElement world = obj.get( "world" );
        final JsonElement x = obj.get( "x" );
        final JsonElement y = obj.get( "y" );
        final JsonElement z = obj.get( "z" );
        final JsonElement yaw = obj.get( "yaw" );
        final JsonElement pitch = obj.get( "pitch" );

        if (world == null || x == null || y == null || z == null || yaw == null || pitch == null) return null;

        if (!world.isJsonPrimitive() || !((JsonPrimitive) world).isString())  return null;

        if (!x.isJsonPrimitive() || !((JsonPrimitive) x).isNumber()) return null;
        if (!y.isJsonPrimitive() || !((JsonPrimitive) y).isNumber())  return null;
        if (!z.isJsonPrimitive() || !((JsonPrimitive) z).isNumber())  return null;

        if (!yaw.isJsonPrimitive() || !((JsonPrimitive) yaw).isNumber())  return null;
        if (!pitch.isJsonPrimitive() || !((JsonPrimitive) pitch).isNumber())  return null;

        World worldInstance = Bukkit.getWorld( world.getAsString() );
        if (worldInstance == null) return null;

        return new Location(worldInstance, x.getAsDouble(), y.getAsDouble(),
                z.getAsDouble(), yaw.getAsFloat(), pitch.getAsFloat());
    }

    public static JsonElement serialize( Location location) {
        final JsonObject obj = new JsonObject();

        obj.addProperty("world", location.getWorld().getName());
        obj.addProperty("x", location.getX());
        obj.addProperty("y", location.getY());
        obj.addProperty("z", location.getZ());
        obj.addProperty("yaw", location.getYaw());
        obj.addProperty("pitch", location.getPitch());

        return obj;
    }
}