package io.github.hartorn.Pegasus;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.entity.Horse;

public class PegasusProperties implements Serializable
{

    private static final long serialVersionUID = 2539166267530593666L;

    private Horse.Color color;

    private UUID id;
    private String name;
    private Horse.Style style;
    private Horse.Variant variant;

    // private ItemStack[] inventoryContents;

    public PegasusProperties()
    {
    }

    public PegasusProperties(final Horse pegasus)
    {
        this.id = pegasus.getUniqueId();
        this.color = pegasus.getColor();
        this.style = pegasus.getStyle();
        this.variant = pegasus.getVariant();
        this.name = pegasus.getCustomName();
        // this.inventoryContents = pegasus.getInventory().getContents();
    }

    public Horse.Color getColor()
    {
        return this.color;
    }

    public UUID getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public Horse.Style getStyle()
    {
        return this.style;
    }

    public Horse.Variant getVariant()
    {
        return this.variant;
    }

    public void setColor(final Horse.Color color)
    {
        this.color = color;
    }

    public void setId(final UUID id)
    {
        this.id = id;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public void setStyle(final Horse.Style style)
    {
        this.style = style;
    }

    public void setVariant(final Horse.Variant variant)
    {
        this.variant = variant;
    }

    // public ItemStack[] getInventoryContents() {
    // return inventoryContents;
    // }
    //
    // public void setInventoryContents(ItemStack[] inventoryContents) {
    // this.inventoryContents = inventoryContents;
    // }

}