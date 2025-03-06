package com.vasconcellos.voxybases.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullCreator {

    public static ItemStack CHEST01 = getSkull("24b953b2c0e952574f1ed29c81e82e53bcdb1ba683259c20daeef7d554a2a798");
    public static ItemStack CHEST02 = getSkull("b0b068709790d41b8927b8422d21bb52404b55b4ca352cdb7c68e4b36592721");
    public static ItemStack CHEST03 = getSkull("e8e5544af7f5489cc27491ca68fa92384b8ea5cf20b5c8198adb7bfd12bc2bc2");


    public static ItemStack ALARM_ON = getSkull("e03780dfc2b1bbf1abf0f31d9ea2e5c78593118e85febe6eb9e90a0a281b00be");
    public static ItemStack ALARM_OFF = getSkull("4dd12a6c13d1e9565827995e286c9782ba46f2dba73179f3574b7d0695cdb703");

    public static ItemStack HEAD01 = getSkull("d9df4c598a425ae0ffb6f74f6472e6e396b3bb376894f5f21689c04998a2c679");
    public static ItemStack HEAD02 = getSkull("1b8091d47565d60b4819d7c446f1b73c3f11372a3919a532f53f781b1fdc2df8");

    public static ItemStack KEYHOLE = getSkull("4fd1489f85cde3c7922b278749fd17cbb6b7d1479d2d6c29d833afb3ef594836");
    public static ItemStack MONITOR = getSkull("a28db7896663c52cd91e6daa1df9db638709f90c957e9b1010aafe269b293e55");
    public static ItemStack TOY_HOUSE = getSkull("5166101c1dab7b19de749f9d7473ae8a033bd4489faa5fd48bb1a7e1ca12d");

    public static ItemStack getSkull(String url) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        if ((url == null) || (url.isEmpty()))
            return skull;

        url = "http://textures.minecraft.net/texture/" + url;

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}",
                new Object[]{url}).getBytes());

        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");

            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);

            skull.setItemMeta(skullMeta);
        } catch (Exception  exception) {
            exception.printStackTrace();
        }

        return skull;
    }
}
