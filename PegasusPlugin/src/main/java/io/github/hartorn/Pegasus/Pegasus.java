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
        return getPegasusData().get(playerID);
    }

    public UUID getPegasusOwnerUUID(final UUID pegasusID)
    {
        return getPegasusMap().get(pegasusID);
    }

    public Player getPegasusOwner(final UUID pegasusID)
    {
        if (pegasusID == null) {
            return null;
        }

        return PegasusDataHelper.getPlayerByUUID(getPegasusOwnerUUID(pegasusID));
    }

    @Override
    public void onDisable()
    {
        PegasusDataHelper.savePegasusData(this);
        getLogger().info("Pegasus Plugin has been disabled.");
    }

    @Override
    public void onEnable()
    {
        Pegasus.instance = this;
        // register custom entities
        CustomEntityHelper.registerEntities();

        // register listeners
        getServer().getPluginManager().registerEvents(new PegasusListener(), this);

        // register command executors
        final PegasusPlayerCommandExecutor pegasusPlayerCommandExecutor = new PegasusPlayerCommandExecutor();
        getCommand("pegasus-create").setExecutor(pegasusPlayerCommandExecutor);
        getCommand("pegasus-customise").setExecutor(pegasusPlayerCommandExecutor);
        getCommand("pegasus-respawn").setExecutor(pegasusPlayerCommandExecutor);

        final PegasusAdministratorCommandExecutor pegasusAdministratorCommandExecutor = new PegasusAdministratorCommandExecutor();
        getCommand("pegasus-clear").setExecutor(pegasusAdministratorCommandExecutor);
        getCommand("pegasus-forceclear").setExecutor(pegasusAdministratorCommandExecutor);

        // initialise pegasusData Hashmap
        PegasusDataHelper.initialisePegasusData(this);

        // initialise configuration
        ConfigHelper.initialiseConfig(this);
        getLogger().info("Pegasus Plugin has been enabled.");
    }

    public void setPegasusData(final HashMap<UUID, PegasusProperties> pegasusData)
    {
        this.pegasusData = pegasusData;
    }

    public void setPegasusPropertiesWithUUIDNull(final UUID playerID)
    {
        if (playerID != null) {
            final PegasusProperties properties = getPegasusData().get(playerID);
            if (properties != null) {
                // properties.setInventoryContents(monture.getInventory().getContents());
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