package io.github.hartorn.Pegasus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import net.minecraft.util.com.google.gson.reflect.TypeToken;
import net.minecraft.util.com.google.gson.stream.JsonReader;
import net.minecraft.util.com.google.gson.stream.JsonWriter;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PegasusDataHelper
{
    public static void initialisePegasusData(final Pegasus plugin)
    {
        final File folder = plugin.getDataFolder();
        folder.mkdirs();
        final File file = new File(folder, plugin.getFilename());
        HashMap<UUID, PegasusProperties> data;
        if (file.exists() && file.length() > 0) {
            JsonReader jsonReader = null;
            try {
                jsonReader = new JsonReader(new FileReader(file));
            } catch (final FileNotFoundException e) {
                String stacktrace = e.toString();
                stacktrace += e.fillInStackTrace().toString();
                plugin.getLogger().log(Level.SEVERE, stacktrace);
            }

            final Type hashMapType = new TypeToken<HashMap<UUID, PegasusProperties>>() {
            }.getType();
            final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            data = gson.fromJson(jsonReader, hashMapType);
        } else {
            data = new HashMap<UUID, PegasusProperties>();
        }
        plugin.setPegasusData(data);
    }

    public static void savePegasusData(final Pegasus plugin)
    {
        final File folder = plugin.getDataFolder();
        folder.mkdirs();
        final File file = new File(folder, plugin.getFilename());
        final Type hashMapType = new TypeToken<HashMap<UUID, PegasusProperties>>() {
        }.getType();
        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        JsonWriter jsonWriter = null;

        try {
            jsonWriter = new JsonWriter(new FileWriter(file));
            gson.toJson(plugin.getPegasusData(), hashMapType, jsonWriter);
            jsonWriter.setSerializeNulls(true);
            jsonWriter.close();
        } catch (final IOException e) {
            String stacktrace = e.toString();
            stacktrace += e.fillInStackTrace().toString();
            plugin.getLogger().log(Level.SEVERE, stacktrace);
        }
    }

    public static void setPegasusProperties(final Horse monture, final Player player, final PegasusProperties properties)
    {
        monture.setOwner(player);
        monture.setCarryingChest(true);
        monture.setCanPickupItems(true);
        monture.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        monture.setRemoveWhenFarAway(false);
        monture.setTamed(true);
        monture.setJumpStrength(1);
        monture.setBreed(false);
        if (properties.getName() != null) {
            monture.setCustomName(properties.getName());
            monture.setCustomNameVisible(true);
        }
        monture.setColor(properties.getColor());
        monture.setVariant(properties.getVariant());
        monture.setStyle(properties.getStyle());

        // monture.getInventory().clear();
        // monture.getInventory().setContents(properties.getInventoryContents());
        // monture.getInventory().setSaddle(new ItemStack(Material.SADDLE));
    }

    public static void setPegasusProperties(final Horse monture, final String args[], final Player player, final boolean isCreation)
    {
        boolean endIteration = false;
        if (isCreation) {
            monture.setOwner(player);
            monture.setCarryingChest(true);
            monture.setCanPickupItems(true);
            monture.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            monture.setRemoveWhenFarAway(false);
            monture.setTamed(true);
            monture.setJumpStrength(1);
            monture.setBreed(false);
        }
        if (args != null && args.length > 0) {
            for (final String argument : args) {
                endIteration = false;
                if (argument.length() != 0) {
                    if (argument.equalsIgnoreCase("WHITE_C")) {
                        monture.setColor(Horse.Color.WHITE);
                        continue;
                    } else {
                        for (final Horse.Color color : Horse.Color.values()) {
                            if (color.name().equalsIgnoreCase(argument)) {
                                monture.setColor(color);
                                endIteration = true;
                                break;
                            }
                        }
                        if (endIteration) {
                            continue;
                        }
                    }
                    if (argument.equalsIgnoreCase("WHITE_S")) {
                        monture.setStyle(Horse.Style.WHITE);
                        continue;
                    } else {
                        for (final Horse.Style style : Horse.Style.values()) {
                            if (style.name().equalsIgnoreCase(argument)) {
                                monture.setStyle(style);
                                endIteration = true;
                                break;
                            }
                        }
                        if (endIteration) {
                            continue;
                        }
                    }
                    for (final Horse.Variant variant : Horse.Variant.values()) {
                        if (variant.name().equalsIgnoreCase(argument)) {
                            monture.setVariant(variant);
                            endIteration = true;
                            break;
                        }
                    }
                    if (endIteration) {
                        continue;
                    }
                    monture.setCustomName(argument);
                    monture.setCustomNameVisible(true);
                }
            }
        }

    }
}