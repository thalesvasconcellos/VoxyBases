package com.vasconcellos.voxybases.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class InventoryUtils {

    public static Inventory deserialize(JsonPrimitive json) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(json.getAsString()));
            BukkitObjectInputStream data = new BukkitObjectInputStream(stream);
            Inventory inventory = Bukkit.createInventory(null, data.readInt(), data.readObject().toString());
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) data.readObject());
            data.close();

            return inventory;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonElement serialize(Inventory inventory) {
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            BukkitObjectOutputStream data = new BukkitObjectOutputStream(str);

            data.writeInt(inventory.getSize());
            data.writeObject(inventory.getName());

            for (int i = 0; i < inventory.getSize(); i++)
                data.writeObject(inventory.getItem(i));

            data.close();

            return new JsonPrimitive(Base64.getEncoder().encodeToString(str.toByteArray()));
        } catch (Exception e) {
            return null;
        }
    }
}
