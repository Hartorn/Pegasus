package io.github.hartorn.Pegasus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Horse;

public class PegasusProperties implements Serializable
{

    private static final long serialVersionUID = -1429007520327836350L;

    private Horse.Color color;

    private UUID id;
    private String name;
    private Horse.Style style;
    private Horse.Variant variant;
    private ArrayList<Map<String, Object>> inventoryContents;

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
        this.inventoryContents = InventorySerializer.getSerializedInventory(pegasus.getInventory());
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

    public ArrayList<Map<String, Object>> getInventoryContents()
    {
        return this.inventoryContents;
    }

    public void setInventoryContents(final ArrayList<Map<String, Object>> inventoryContents)
    {
        this.inventoryContents = inventoryContents;
    }
}