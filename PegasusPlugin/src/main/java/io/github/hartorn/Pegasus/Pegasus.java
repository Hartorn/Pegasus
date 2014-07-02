package io.github.hartorn.Pegasus;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pegasus extends JavaPlugin
{
    private static Pegasus instance;

    public static Pegasus getInstance()
    {
        return Pegasus.instance;
    }

    private final String filenameDataMap = "dataPegasus.json";
    private final String filenameMap = "mapPegasus.json";

    private HashMap<UUID, PegasusProperties> pegasusData;
    private HashMap<UUID, UUID> pegasusMap;

    public HashMap<UUID, PegasusProperties> getPegasusData()
    {
        return this.pegasusData;
    }

    public PegasusProperties getPegasusProperties(final UUID playerID)
    {
        return this.getPegasusData().get(playerID);
    }

    public PegasusProperties getPegasusPropertiesByPegasusUUID(final UUID pegasusID)
    {
        if (pegasusID == null) {
            return null;
        }
        return this.getPegasusProperties(this.getPegasusOwnerUUID(pegasusID));
    }

    public UUID getPegasusOwnerUUID(final UUID pegasusID)
    {
        return this.getPegasusMap().get(pegasusID);
    }

    public Player getPegasusOwner(final UUID pegasusID)
    {
        if (pegasusID == null) {
            return null;
        }

        return PegasusDataHelper.getPlayerByUUID(this.getPegasusOwnerUUID(pegasusID));
    }

    @Override
    public void onDisable()
    {
        PegasusDataHelper.savePegasusData(this);
        this.getLogger().info("Pegasus Plugin has been disabled.");
    }

    @Override
    public void onEnable()
    {
        Pegasus.instance = this;
        // register custom entities
        CustomEntityHelper.registerEntities();

        // register listeners
        this.getServer().getPluginManager().registerEvents(new PegasusListener(), this);

        // register command executors
        final PegasusPlayerCommandExecutor pegasusPlayerCommandExecutor = new PegasusPlayerCommandExecutor();
        this.getCommand("pegasus-create").setExecutor(pegasusPlayerCommandExecutor);
        this.getCommand("pegasus-customise").setExecutor(pegasusPlayerCommandExecutor);
        this.getCommand("pegasus-respawn").setExecutor(pegasusPlayerCommandExecutor);

        final PegasusAdministratorCommandExecutor pegasusAdministratorCommandExecutor = new PegasusAdministratorCommandExecutor();
        this.getCommand("pegasus-clear").setExecutor(pegasusAdministratorCommandExecutor);
        this.getCommand("pegasus-clean").setExecutor(pegasusAdministratorCommandExecutor);
        this.getCommand("pegasus-forceclear").setExecutor(pegasusAdministratorCommandExecutor);

        // initialise pegasusData Hashmap
        PegasusDataHelper.initialisePegasusData(this);

        // initialise configuration
        ConfigHelper.initialiseConfig(this);
        this.getLogger().info("Pegasus Plugin has been enabled.");
    }

    public void setPegasusData(final HashMap<UUID, PegasusProperties> pegasusData)
    {
        this.pegasusData = pegasusData;
    }

    public void removePegasusByUUID(final UUID pegasusID)
    {
        final UUID playerID = this.getPegasusOwnerUUID(pegasusID);
        this.getPegasusMap().remove(playerID);
        this.setPegasusPropertiesWithUUIDNull(playerID);
    }

    public void removePegasusByOwnerUUID(final UUID ownerID)
    {
        this.getPegasusMap().remove(ownerID);
        this.setPegasusPropertiesWithUUIDNull(ownerID);
    }

    public void setPegasusPropertiesWithUUIDNull(final UUID playerID)
    {
        if (playerID != null) {
            final PegasusProperties properties = this.getPegasusData().get(playerID);
            if (properties != null) {
                properties.setId(null);
            }
        }
    }

    public String getFilenameDataMap()
    {
        return this.filenameDataMap;
    }

    public String getFilenameMap()
    {
        return this.filenameMap;
    }

    public HashMap<UUID, UUID> getPegasusMap()
    {
        return this.pegasusMap;
    }

    public void setPegasusMap(final HashMap<UUID, UUID> pegasusMap)
    {
        this.pegasusMap = pegasusMap;
    }
}