package io.github.hartorn.Pegasus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import net.minecraft.util.com.google.gson.reflect.TypeToken;
import net.minecraft.util.com.google.gson.stream.JsonReader;
import net.minecraft.util.com.google.gson.stream.JsonWriter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PegasusDataHelper
{
    public static void initialisePegasusData(final Pegasus plugin)
    {
        final File folder = plugin.getDataFolder();
        folder.mkdirs();
        final File fileDataMap = new File(folder, plugin.getFilenameDataMap());
        final File fileMap = new File(folder, plugin.getFilenameMap());

        HashMap<UUID, PegasusProperties> data;
        HashMap<UUID, UUID> map;

        if (fileDataMap.exists() && fileDataMap.length() > 0 && fileMap.exists() && fileMap.length() > 0) {
            JsonReader jsonReaderDataMap = null;
            JsonReader jsonReaderMap = null;

            try {
                jsonReaderDataMap = new JsonReader(new FileReader(fileDataMap));
                jsonReaderMap = new JsonReader(new FileReader(fileMap));

            } catch (final FileNotFoundException e) {
                String stacktrace = e.toString();
                stacktrace += e.fillInStackTrace().toString();
                plugin.getLogger().log(Level.SEVERE, stacktrace);
            }

            final Type hashMapType = new TypeToken<HashMap<UUID, PegasusProperties>>() {
            }.getType();
            final Type hashMap2Type = new TypeToken<HashMap<UUID, UUID>>() {
            }.getType();

            final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            data = gson.fromJson(jsonReaderDataMap, hashMapType);
            map = gson.fromJson(jsonReaderMap, hashMap2Type);
        } else {
            data = new HashMap<UUID, PegasusProperties>();
            map = new HashMap<UUID, UUID>();
        }
        plugin.setPegasusData(data);
        plugin.setPegasusMap(map);
    }

    public static void savePegasusData(final Pegasus plugin)
    {
        final File folder = plugin.getDataFolder();
        folder.mkdirs();
        final File fileData = new File(folder, plugin.getFilenameDataMap());
        final File fileMap = new File(folder, plugin.getFilenameMap());

        final Type hashMapType = new TypeToken<HashMap<UUID, PegasusProperties>>() {
        }.getType();
        final Type hashMap2Type = new TypeToken<HashMap<UUID, UUID>>() {
        }.getType();
        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        JsonWriter jsonWriterData = null;
        JsonWriter jsonWriterMap = null;

        try {
            jsonWriterData = new JsonWriter(new FileWriter(fileData));
            jsonWriterMap = new JsonWriter(new FileWriter(fileMap));

            gson.toJson(plugin.getPegasusData(), hashMapType, jsonWriterData);
            jsonWriterData.close();

            gson.toJson(plugin.getPegasusMap(), hashMap2Type, jsonWriterMap);
            jsonWriterMap.close();
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

    public static Player getPlayerByUUID(final UUID playerID)
    {
        if (playerID == null) {
            return null;
        }
        final Player[] playerList = Bukkit.getOnlinePlayers();
        for (final Player player : playerList) {
            if (player.getUniqueId().compareTo(playerID) == 0) {
                return player;
            }
        }
        final OfflinePlayer[] offlinePlayerList = Bukkit.getOfflinePlayers();
        for (final OfflinePlayer offlinePlayer : offlinePlayerList) {
            if (offlinePlayer.getPlayer().getUniqueId().compareTo(playerID) == 0) {
                return offlinePlayer.getPlayer();
            }
        }
        return null;
    }

    public static <ParamType extends Entity> ParamType getEntityByUUIDAndByClass(final UUID entityId, final Class<ParamType> typeClass)
    {
        if (entityId != null) {
            for (final World world : Bukkit.getWorlds()) {
                final Collection<ParamType> collection = world.getEntitiesByClass(typeClass);
                for (final ParamType entity : collection) {
                    if (entity.getUniqueId().compareTo(entityId) == 0) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }

    public static <ParamType extends Entity> ParamType getEntityByUUIDAndByClass(final UUID entityId, final UUID worldId, final Class<ParamType> typeClass)
    {
        if (entityId != null && worldId != null) {
            final Collection<ParamType> collection = Bukkit.getWorld(worldId).getEntitiesByClass(typeClass);

            for (final ParamType entity : collection) {
                if (entity.getUniqueId().compareTo(entityId) == 0) {
                    return entity;
                }
            }
        } else if (entityId != null) {
            for (final World world : Bukkit.getWorlds()) {
                if (worldId == null || world.getUID().compareTo(worldId) != 0) {
                    final Collection<ParamType> collection = world.getEntitiesByClass(typeClass);
                    for (final ParamType entity : collection) {
                        if (entity.getUniqueId().compareTo(entityId) == 0) {
                            return entity;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static <ParamType extends Entity> ParamType getEntityByUUIDByClassAndByWorld(final UUID entityId, final UUID worldUUID, final Class<ParamType> typeClass)
    {
        if (entityId != null && worldUUID != null) {
            final Collection<ParamType> collection = Bukkit.getWorld(worldUUID).getEntitiesByClass(typeClass);
            for (final ParamType entity : collection) {
                if (entity.getUniqueId().compareTo(entityId) == 0) {
                    return entity;
                }
            }
        }
        return null;
    }

    public static int KillPegasus(final Pegasus plugin)
    {
        return PegasusDataHelper.KillPegasus(plugin, false);
    }

    public static int KillPegasus(final Pegasus plugin, final boolean deleteAll)
    {
        int i = 0;
        for (final World world : Bukkit.getWorlds()) {
            final Collection<Horse> collection = world.getEntitiesByClass(Horse.class);
            for (final Horse horse : collection) {
                if (deleteAll || (((CraftEntity) horse).getHandle() instanceof PegasusEntity && plugin.getPegasusOwnerUUID(horse.getUniqueId()) == null)) {
                    horse.setHealth(0);
                    i++;
                }
            }
        }
        if (deleteAll) {
            plugin.getPegasusMap().clear();
            for (final PegasusProperties properties : plugin.getPegasusData().values()) {
                properties.setId(null);
            }
        }
        return i;
    }
}