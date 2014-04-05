package io.github.hartorn.Pegasus;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.entity.Horse;

public class PegasusProperties implements Serializable {

	private static final long serialVersionUID = 2539166267530593666L;
	
	private UUID id;

	private Horse.Color color;
	private Horse.Style style;
	private Horse.Variant variant;
	private String name;

	//private ItemStack[] inventoryContents;

	public PegasusProperties()
	{}

	public PegasusProperties(Horse pegasus)
	{
		this.id = pegasus.getUniqueId();
		this.color = pegasus.getColor();
		this.style = pegasus.getStyle();
		this.variant = pegasus.getVariant();
		this.name = pegasus.getCustomName();
		//this.inventoryContents = pegasus.getInventory().getContents();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Horse.Color getColor() {
		return color;
	}

	public void setColor(Horse.Color color) {
		this.color = color;
	}

	public Horse.Style getStyle() {
		return style;
	}

	public void setStyle(Horse.Style style) {
		this.style = style;
	}

	public Horse.Variant getVariant() {
		return variant;
	}

	public void setVariant(Horse.Variant variant) {
		this.variant = variant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public ItemStack[] getInventoryContents() {
//		return inventoryContents;
//	}
//
//	public void setInventoryContents(ItemStack[] inventoryContents) {
//		this.inventoryContents = inventoryContents;
//	}


}