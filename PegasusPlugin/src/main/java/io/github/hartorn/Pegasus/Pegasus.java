package io.github.hartorn.Pegasus;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Horse;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pegasus extends JavaPlugin
{
    private static Pegasus instance;

    public static Pegasus getInstance()
    {
        return Pegasus.instance;
    }

    private final String                     filename = "dataPegasus.json";
    private HashMap<UUID, PegasusProperties> pegasusData;

    public String getFilename()
    {
        return this.filename;
    }

    public HashMap<UUID, PegasusProperties> getPegasusData()
    {
        return this.pegasusData;
    }

    public PegasusProperties getPegasusProperties(final UUID playerID)
    {
        return getPegasusData().get(playerID);
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

        // initialise pegasusData Hashmap
        PegasusDataHelper.initialisePegasusData(this);
        getLogger().info("Pegasus Plugin has been enabled.");
    }

    public void setPegasusData(final HashMap<UUID, PegasusProperties> pegasusData)
    {
        this.pegasusData = pegasusData;
    }

    public void setPegasusPropertiesWithUUIDNull(final UUID playerID, final Horse monture)
    {
        final PegasusProperties properties = getPegasusData().get(playerID);
        // properties.setInventoryContents(monture.getInventory().getContents());
        properties.setId(null);
    }
}