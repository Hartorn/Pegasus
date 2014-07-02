package io.github.hartorn.Pegasus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import net.minecraft.util.com.google.gson.reflect.TypeToken;
import net.minecraft.util.com.google.gson.stream.JsonReader;
import net.minecraft.util.com.google.gson.stream.JsonWriter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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

    public static void setPegasusProperties(final Horse monture, final Player player, final PegasusProperties properties, final Pegasus plugin)
    {
        monture.setCarryingChest(ConfigHelper.hasInventory(plugin.getConfig()));
        monture.setCanPickupItems(ConfigHelper.canPickUpItems(plugin.getConfig()));
        monture.setJumpStrength(ConfigHelper.getJumpStrength(plugin.getConfig()));
        monture.setMaxHealth(ConfigHelper.getMaxHealth(plugin.getConfig()));
        monture.getInventory().setArmor(ConfigHelper.getArmor(plugin.getConfig()));

        monture.setOwner(player);
        monture.setRemoveWhenFarAway(false);
        monture.setTamed(true);
        monture.setBreed(false);
        if (properties.getName() != null) {
            monture.setCustomName(properties.getName());
            monture.setCustomNameVisible(true);
        }
        monture.setColor(properties.getColor());
        monture.setVariant(properties.getVariant());
        monture.setStyle(properties.getStyle());

        InventorySerializer.setInventoryContent(monture.getInventory(), properties.getInventoryContents());
    }

    public static void setPegasusProperties(final Horse monture, final String args[], final Player player, final boolean isCreation, final Pegasus plugin)
    {
        boolean endIteration = false;
        if (isCreation) {
            monture.setCarryingChest(ConfigHelper.hasInventory(plugin.getConfig()));
            monture.setCanPickupItems(ConfigHelper.canPickUpItems(plugin.getConfig()));
            monture.setJumpStrength(ConfigHelper.getJumpStrength(plugin.getConfig()));
            monture.setMaxHealth(ConfigHelper.getMaxHealth(plugin.getConfig()));
            monture.getInventory().setArmor(ConfigHelper.getArmor(plugin.getConfig()));

            monture.setOwner(player);
            monture.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            monture.setRemoveWhenFarAway(false);
            monture.setTamed(true);
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
        return Bukkit.getServer().getPlayer(playerID);
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

    public static void respawnPegasusByOwnerId(final Player player, final Pegasus plugin)
    {
        final UUID UUIDPlayer = player.getUniqueId();
        final PegasusProperties properties = plugin.getPegasusData().get(UUIDPlayer);
        if (properties != null && properties.getId() == null) {
            PegasusDataHelper.respawnPegasus(player, properties, plugin);
        }
    }

    public static void spawnPegasus(final Player player, final String[] args, final Pegasus plugin)
    {
        final Horse monture = PegasusEntity.spawn(player);
        PegasusDataHelper.setPegasusProperties(monture, args, player, true, plugin);
        plugin.getPegasusData().put(player.getUniqueId(), new PegasusProperties(monture));
        plugin.getPegasusMap().put(monture.getUniqueId(), player.getUniqueId());
        player.sendMessage("Your pegasus has spawned.");
    }

    public static void respawnPegasus(final Player player, final PegasusProperties properties, final Pegasus plugin)
    {
        final Horse monture = PegasusEntity.spawn(player);
        PegasusDataHelper.setPegasusProperties(monture, player, properties, plugin);
        plugin.getPegasusData().put(player.getUniqueId(), new PegasusProperties(monture));
        plugin.getPegasusMap().put(monture.getUniqueId(), player.getUniqueId());
        player.sendMessage("Your pegasus has respawned.");
    }

    public static void unloadPegasusInList(final List<Entity> unloadedEntities, final Pegasus plugin)
    {
        if (unloadedEntities != null) {
            for (final Entity entity : unloadedEntities) {
                PegasusDataHelper.unloadPegasusByEntity(entity, plugin);
            }
        }
    }

    public static void unloadPegasusInArray(final Entity[] unloadedEntities, final Pegasus plugin)
    {
        if (unloadedEntities != null) {
            for (final Entity entity : unloadedEntities) {
                if (entity != null) {
                    PegasusDataHelper.unloadPegasusByEntity(entity, plugin);
                }
            }
        }
    }

    public static void unloadPegasusByOwnerId(final UUID ownerId, final Pegasus plugin)
    {
        if (plugin.getPegasusProperties(ownerId) != null) {
            final UUID pegasusId = plugin.getPegasusProperties(ownerId).getId();
            if (pegasusId != null) {
                PegasusDataHelper.unloadPegasusByEntity(PegasusDataHelper.getEntityByUUIDAndByClass(pegasusId, Horse.class), plugin);
            }
        }
    }

    public static void unloadPegasusByEntity(final Entity entity, final Pegasus plugin)
    {
        if (entity != null && EntityType.HORSE.compareTo(entity.getType()) == 0) {
            final UUID ownerId = plugin.getPegasusOwnerUUID(entity.getUniqueId());
            if (ownerId != null) {
                final Horse monture = Horse.class.cast(entity);
                plugin.getPegasusProperties(ownerId).setInventoryContents(InventorySerializer.getSerializedInventory(monture.getInventory()));
                plugin.removePegasusByOwnerUUID(ownerId);
                entity.remove();
                final Player player = PegasusDataHelper.getPlayerByUUID(ownerId);
                if (player != null && player.isOnline()) {
                    player.sendMessage("Your Pegasus has been unloaded, you can respawn it using /pegasus-respawn");
                }
            }
        }
    }

    public static int killPegasus(final Pegasus plugin)
    {
        return PegasusDataHelper.killPegasus(plugin, false);
    }

    public static int killRegisteredPegasus(final Pegasus plugin)
    {
        int i = 0;
        for (final World world : Bukkit.getWorlds()) {
            final Collection<Horse> collection = world.getEntitiesByClass(Horse.class);
            for (final Horse horse : collection) {
                if (plugin.getPegasusOwnerUUID(horse.getUniqueId()) != null) {
                    final UUID playerUUID = plugin.getPegasusOwnerUUID(horse.getUniqueId());
                    plugin.getPegasusMap().remove(horse.getUniqueId());
                    plugin.setPegasusPropertiesWithUUIDNull(playerUUID);
                    horse.remove();
                    i++;
                }
            }
        }
        return i;
    }

    public static int killPegasus(final Pegasus plugin, final boolean deleteAll)
    {
        int i = 0;
        for (final World world : Bukkit.getWorlds()) {
            final Collection<Horse> collection = world.getEntitiesByClass(Horse.class);
            for (final Horse horse : collection) {
                if (deleteAll || (((CraftEntity) horse).getHandle() instanceof PegasusEntity && plugin.getPegasusOwnerUUID(horse.getUniqueId()) == null)) {
                    horse.remove();
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