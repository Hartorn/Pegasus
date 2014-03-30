package io.github.hartorn.Pegasus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_7_R2.BiomeBase;
import net.minecraft.server.v1_7_R2.BiomeMeta;
import net.minecraft.server.v1_7_R2.EntityTypes;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pegasus extends JavaPlugin {
	Player joueur;

	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(new PegasusListener(this), this);
		registerEntities();

		getLogger().info("Pegasus Plugin has been enabled.");
	}

	public void onDisable(){
		getLogger().info("egasus Plugin has been enabled.");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerEntities(){
		for (CustomEntityType entity : CustomEntityType.values()){
			try {
				Field c = EntityTypes.class.getDeclaredField("c");
				Field d = EntityTypes.class.getDeclaredField("d");
				Field e = EntityTypes.class.getDeclaredField("e");
				Field f = EntityTypes.class.getDeclaredField("f");
				Field g = EntityTypes.class.getDeclaredField("g");

				c.setAccessible(true);
				d.setAccessible(true);
				e.setAccessible(true);
				f.setAccessible(true);
				g.setAccessible(true);

				Map cMap = (Map) c.get(null);
				Map dMap = (Map) d.get(null);
				Map eMap = (Map) e.get(null);
				Map fMap = (Map) f.get(null);
				Map gMap = (Map) g.get(null);

				cMap.put(entity.getName(), entity.getCustomClass());
				dMap.put(entity.getCustomClass(), entity.getName());
				eMap.put(entity.getID(), entity.getCustomClass());
				fMap.put(entity.getCustomClass(), entity.getID());
				gMap.put(entity.getName(), entity.getID());

				c.set(null, cMap);
				d.set(null, dMap);
				e.set(null, eMap);
				f.set(null, fMap);
				g.set(null, gMap);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		for (BiomeBase biomeBase : BiomeBase.n()){
			if (biomeBase == null){
				break;
			}

			for (String field : new String[]{"as", "at", "au", "av"}){
				try{
					Field list = BiomeBase.class.getDeclaredField(field);
					list.setAccessible(true);
					List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);

					for (BiomeMeta meta : mobList){
						for (CustomEntityType entity : CustomEntityType.values()){
							if (entity.getNMSClass().equals(meta.b)){
								meta.b = entity.getCustomClass();
							}
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args)
	{
		if ((sender instanceof Player))
		{
			Player player = (Player)sender;
			this.joueur = player;
			if (cmd.getName().equalsIgnoreCase("pegasus")) {
				Horse monture = PegasusEntity.spawn(player);
				monture.setOwner(player);
				monture.setCarryingChest(true);
				monture.setCanPickupItems(true);
				monture.getInventory().setSaddle(new ItemStack(Material.SADDLE));
				monture.setRemoveWhenFarAway(false);
				monture.setTamed(true);
				monture.setJumpStrength(1);
			}
		}
		return false;
	}

}