package io.github.hartorn.Pegasus;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pegasus extends JavaPlugin {
	HashMap<UUID, PegasusProperties> pegasusData;
	private final String filename = "dataPegasus.json";
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(new PegasusListener(this), this);
		CustomEntityHelper.registerEntities();
		PegasusDataHelper.initialisePegasusData(this, this.pegasusData);
		getLogger().info("Pegasus Plugin has been enabled.");
	}

	public void onDisable(){
		PegasusDataHelper.savePegasusData(this, this.pegasusData);
		getLogger().info("Pegasus Plugin has been disabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args)
	{
		if ((sender instanceof Player))
		{
			Player player = (Player)sender;
			if (cmd.getName().equalsIgnoreCase("pegasus-create") && player.hasPermission("pegasus.create")) {	
				UUID UUIDPlayer  = player.getUniqueId();
				PegasusProperties properties = this.pegasusData.get(UUIDPlayer);
				if(properties==null || properties.getId() == null)
				{
					Horse monture = PegasusEntity.spawn(player);
					PegasusDataHelper.setPegasusProperties(monture, args, player, true);
					this.pegasusData.put(UUIDPlayer, new PegasusProperties(monture));
					player.sendMessage("Your pegasus has spawned.");
				}
				else
				{
					player.sendMessage("You already have a pegasus, use /pegasus-respawn to get it back.");
				}
			}
			else if (cmd.getName().equalsIgnoreCase("pegasus-customise") && player.hasPermission("pegasus.customise")) {	
				UUID UUIDPlayer  = player.getUniqueId();
				PegasusProperties properties = this.pegasusData.get(UUIDPlayer);
				if(properties!=null && properties.getId() != null)
				{
					if(player.getVehicle()!=null && player.getVehicle().getUniqueId().compareTo(properties.getId())==0)
					{
						Horse monture = Horse.class.cast(player.getVehicle());
						PegasusDataHelper.setPegasusProperties(monture, args, player, false);
						this.pegasusData.put(UUIDPlayer, new PegasusProperties(monture));
						player.sendMessage("Your pegasus has been customised.");
					}
					else
					{
						player.sendMessage("You have to be mounting your pegasus to customise it.");
					}
				}
				else
				{
					player.sendMessage("You don't have any pegasus. Either respawn it, if yours is dead, or create one using /pegasus-create");
				}
			}
			else if(cmd.getName().equalsIgnoreCase("pegasus-respawn") && player.hasPermission("pegasus.respawn"))
			{
				UUID UUIDPlayer  = player.getUniqueId();
				PegasusProperties properties = this.pegasusData.get(UUIDPlayer);
				if(properties==null)
				{
					player.sendMessage("You don't have any pegasus. You can create one using /pegasus-create");

				}
				else if( properties.getId()==null)
				{
					Horse monture = PegasusEntity.spawn(player);
					PegasusDataHelper.setPegasusProperties(monture, player, properties);
					this.pegasusData.put(UUIDPlayer, new PegasusProperties(monture));
					player.sendMessage("Your pegasus has respawned.");
				}
				else
				{
					Collection<Horse> collection = player.getWorld().getEntitiesByClass(Horse.class);
					for (Horse horse : collection)
					{
						if(horse.getUniqueId().compareTo(properties.getId())==0)
						{
							horse.teleport(player, TeleportCause.PLUGIN);
							player.sendMessage("Your pegasus has been teleported to your location.");
							return false;
						}
					}
					UUID idWorld = player.getWorld().getUID();
					for(World world :Bukkit.getWorlds())
					{
						if(idWorld.compareTo(world.getUID())!=0)
						{
							collection = world.getEntitiesByClass(Horse.class);
							for (Horse horse : collection)
							{
								if(horse.getUniqueId().compareTo(properties.getId())==0)
								{
									horse.teleport(player, TeleportCause.PLUGIN);
									player.sendMessage("Your pegasus has been teleported to your location.");
									return false;
								}
							}
						}
					}
					player.sendMessage("Your pegasus has not been found. Please report this.");
				}
			}
		}
		return false;
	}

	public PegasusProperties getPegasusProperties(UUID playerID)
	{
		return this.pegasusData.get(playerID);
	}

	public void setPegasusPropertiesWithUUIDNull(UUID playerID, Horse monture)
	{ 
		PegasusProperties properties = this.pegasusData.get(playerID);
		//properties.setInventoryContents(monture.getInventory().getContents());
		properties.setId(null);
	}

	public String getFilename() {
		return filename;
	}

	//	private static class InventorySerializer implements JsonSerializer<ItemStack[]> {
	//		@Override
	//		public JsonElement serialize(ItemStack[] inventory, Type type, JsonSerializationContext context) {
	//			int i;
	//			JsonArray jsonInventory = new JsonArray();
	//			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	//
	//			for(i=0; i<inventory.length; i++)
	//			{
	//				jsonInventory.add(gson.toJson(inventory[i], ItemStack.class));
	//			}
	//
	//			return null;
	//		}
	//	}
}