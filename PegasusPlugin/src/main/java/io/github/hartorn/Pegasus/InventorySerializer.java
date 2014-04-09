package io.github.hartorn.Pegasus;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventorySerializer
{
    public static ArrayList<Map<String, Object>> getSerializedInventory(final Inventory inventory)
    {
        if (inventory == null || inventory.getContents() == null || inventory.getContents().length <= 0) {
            return null;
        }
        final ArrayList<Map<String, Object>> serializedInventory = new ArrayList<Map<String, Object>>();
        for (final ItemStack item : inventory.getContents()) {
            if (item != null) {
                serializedInventory.add(item.serialize());
            }
        }
        return serializedInventory;
    }

    public static void setInventoryContent(final HorseInventory inventory, final ArrayList<Map<String, Object>> serializedInventory)
    {
        if (inventory == null || serializedInventory == null || serializedInventory.size() <= 0) {
            return;
        }
        inventory.clear();
        for (final Map<String, Object> serializedItem : serializedInventory) {
            if (serializedItem != null && !serializedItem.isEmpty()) {
                final ItemStack tmp = ItemStack.deserialize(serializedItem);
                if (tmp != null && InventorySerializer.isHorseArmor(tmp) && inventory.getArmor() == null) {
                    inventory.setArmor(tmp);
                } else if (tmp != null && Material.SADDLE.equals(tmp.getType()) && inventory.getSaddle() == null) {
                    inventory.setSaddle(tmp);
                } else if (tmp != null) {
                    inventory.addItem(tmp);
                }
            }
        }
    }

    public static boolean isHorseArmor(final ItemStack item)
    {
        return Material.GOLD_BARDING.equals(item.getType()) || Material.DIAMOND_BARDING.equals(item.getType()) || Material.IRON_BARDING.equals(item.getType());
    }
}
