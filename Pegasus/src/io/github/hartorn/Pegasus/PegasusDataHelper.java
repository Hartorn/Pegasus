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

public class PegasusDataHelper {
	public static void setPegasusProperties(Horse monture, String args[], Player player, boolean isCreation)
	{
		boolean endIteration = false;
		if(isCreation)
		{
			monture.setOwner(player);
			monture.setCarryingChest(true);
			monture.setCanPickupItems(true);
			monture.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			monture.setRemoveWhenFarAway(false);
			monture.setTamed(true);
			monture.setJumpStrength(1);
			monture.setBreed(false);
		}
		if(args!=null && args.length >0)
		{
			for(String argument : args){
				endIteration = false;
				if(argument.length()!=0)
				{
					if (argument.equalsIgnoreCase("WHITE_C"))
					{
						monture.setColor(Horse.Color.WHITE);
						continue;
					}
					else{
						for(Horse.Color color: Horse.Color.values()){
							if(color.name().equalsIgnoreCase(argument))
							{
								monture.setColor(color);
								endIteration = true;
								break;
							}
						}
						if(endIteration)
						{
							continue;
						}
					}
					if (argument.equalsIgnoreCase("WHITE_S"))
					{
						monture.setStyle(Horse.Style.WHITE);
						continue;
					}
					else{
						for(Horse.Style style: Horse.Style.values()){
							if(style.name().equalsIgnoreCase(argument))
							{
								monture.setStyle(style);
								endIteration = true;
								break;
							}
						}
						if(endIteration)
						{
							continue;
						}
					}
					for(Horse.Variant variant: Horse.Variant.values()){
						if(variant.name().equalsIgnoreCase(argument))
						{
							monture.setVariant(variant);
							endIteration = true;
							break;
						}
					}
					if(endIteration)
					{
						continue;
					}
					monture.setCustomName(argument);
					monture.setCustomNameVisible(true);
				}
			}
		}

	}
	public static void setPegasusProperties(Horse monture, Player player, PegasusProperties properties)
	{
		monture.setOwner(player);
		monture.setCarryingChest(true);
		monture.setCanPickupItems(true);
		monture.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		monture.setRemoveWhenFarAway(false);
		monture.setTamed(true);
		monture.setJumpStrength(1);
		monture.setBreed(false);
		if (properties.getName() !=null)
		{
			monture.setCustomName(properties.getName());
			monture.setCustomNameVisible(true);
		}
		monture.setColor(properties.getColor());
		monture.setVariant(properties.getVariant());
		monture.setStyle(properties.getStyle());

		//		monture.getInventory().clear();
		//		monture.getInventory().setContents(properties.getInventoryContents());
		//		monture.getInventory().setSaddle(new ItemStack(Material.SADDLE));
	}

	public static void initialisePegasusData(Pegasus plugin, HashMap<UUID, PegasusProperties> data)
	{
		File folder = plugin.getDataFolder();
		folder.mkdirs();
		File file = new File(folder, plugin.getFilename());
		if (file.exists() && file.length()>0)
		{
			JsonReader jsonReader = null;
			try {
				jsonReader = new JsonReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				String stacktrace = e.toString();
				stacktrace += e.fillInStackTrace().toString();
				plugin.getLogger().log(Level.SEVERE, stacktrace);
			}

			Type hashMapType = new TypeToken<HashMap<UUID, PegasusProperties>>() {}.getType();
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
			data = gson.fromJson(jsonReader, hashMapType);
		}
		else
		{
			data = new HashMap<UUID, PegasusProperties>();
		}
	}
	public static void savePegasusData(Pegasus plugin, HashMap<UUID, PegasusProperties> data)
	{
		File folder = plugin.getDataFolder();
		folder.mkdirs();
		File file = new File(folder, plugin.getFilename());
		Type hashMapType = new TypeToken<HashMap<UUID, PegasusProperties>>() {}.getType();
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

		JsonWriter jsonWriter = null;

		try {
			jsonWriter = new JsonWriter(new FileWriter(file));
			gson.toJson(data, hashMapType, jsonWriter);
			jsonWriter.setSerializeNulls(true);
			jsonWriter.close();
		} catch (IOException e) {
			String stacktrace = e.toString();
			stacktrace += e.fillInStackTrace().toString();
			plugin.getLogger().log(Level.SEVERE, stacktrace);
		}
	}
}