package com.vasconcellos.voxybases.object;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.vasconcellos.voxybases.VoxyBases;
import com.vasconcellos.voxybases.listener.HologramListener;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionType;
import eu.decentsoftware.holograms.api.actions.ClickType;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class Hologram {

    public final static boolean TYPE;

    static {
        TYPE = VoxyBases.getInstance().getServer().getPluginManager()
                .getPlugin("HolographicDisplays") != null;
    }

    private final Location location;
    private final String name = RandomStringUtils.randomAlphabetic(5);

    private Object hologram;

    public Hologram(Location location) {
        this.location = location;

        hologram = TYPE ? HologramsAPI.createHologram(VoxyBases.getInstance(), location)
                : DHAPI.createHologram(name, location);

        if(!TYPE) getDecent().getPage(0).addAction(ClickType.RIGHT,
                new Action(ActionType.NONE, null));
    }

    public void set(int index, String text) {
        if(TYPE) {
            TextLine textLine = (TextLine) getHolographic().getLine(index);
            textLine.setText(text);
        } else DHAPI.setHologramLine(getDecent(), index, text);
    }

    public void append(String text) {
        if(TYPE) getHolographic().appendTextLine(text);
        else DHAPI.addHologramLine(getDecent(), text);
    }

    public void click(int index, Consumer<Player> consumer) {
        if(TYPE) ((TextLine) getHolographic().getLine(index)).setTouchHandler(consumer::accept);
        else HologramListener.CONSUMERS.put(name, consumer);
    }

    public void clear() {
        if(TYPE) getHolographic().clearLines();
        else HologramListener.CONSUMERS.remove(name);
    }

    public void delete() {
        if(TYPE) getHolographic().delete();
        else getDecent().delete();
    }

    private com.gmail.filoghost.holographicdisplays.api.Hologram getHolographic() {
        return (com.gmail.filoghost.holographicdisplays.api.Hologram) hologram;
    }

    private eu.decentsoftware.holograms.api.holograms.Hologram getDecent() {
        return (eu.decentsoftware.holograms.api.holograms.Hologram) hologram;
    }
}