package io.github.hartorn.Pegasus;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class ConfigHelper
{
    public static FileConfiguration initialiseConfig(final Pegasus plugin)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append("Pegasus configuration file\n");
        builder.append("Jump strengh represents how high your pegasus will go each time you press the spacebar, even when flying\n");
        builder.append("Pegasus.caracteristics.armor accept this values :NONE, IRON, GOLD, DIAMOND.\n");
        builder.append("Pegasus.caracteristics.hasinventory defines if the pegasus got an inventory.\n");
        builder.append("Pegasus.caracteristics.canpickupitems defines if the pegasus can pick up items.\n");
        builder.append("Pegasus.caracteristics.maxhealth is in game between 15 and 30.\n");
        builder.append("Pegasus.caracteristics.must be between 0 and 2. At 0, you won't jump or fly anymore.\n");

        builder.append("Pegasus.damage defines from which sources the pegasus can take damage.\n");
        builder.append("Pegasus.damage.none makes Pegasus invulnerable.\n");

        final FileConfiguration config = plugin.getConfig();
        config.options().header(builder.toString());
        config.addDefault("pegasus.caracteristics.maxhealth", 20.0D);
        config.addDefault("pegasus.caracteristics.jumpstrength", 1.0D);
        config.addDefault("pegasus.caracteristics.armor", "NONE");
        config.addDefault("pegasus.caracteristics.hasinventory", true);
        config.addDefault("pegasus.caracteristics.canpickupitems", true);

        config.addDefault("pegasus.damage.none", false);
        config.addDefault("pegasus.damage.fire", true);
        config.addDefault("pegasus.damage.drowning", true);
        config.addDefault("pegasus.damage.player", true);
        config.addDefault("pegasus.damage.monsters", true);

        config.options().copyDefaults(true);
        config.options().copyHeader(true);
        plugin.saveConfig();
        return config;
    }

    public static double getMaxHealth(final FileConfiguration config)
    {
        return config.getDouble("pegasus.caracteristics.maxhealth");
    }

    public static ItemStack getArmor(final FileConfiguration config)
    {
        final String armorMaterialString = config.getString("pegasus.caracteristics.armor");
        Material armorMaterial;
        switch (armorMaterialString) {
            case ("IRON"):
                armorMaterial = Material.IRON_BARDING;
                break;
            case ("DIAMOND"):
                armorMaterial = Material.DIAMOND_BARDING;
                break;
            case ("GOLD"):
                armorMaterial = Material.GOLD_BARDING;
                break;
            case ("NONE"):
            default:
                armorMaterial = null;
                break;
        }
        if (armorMaterial == null) {
            return null;
        }
        return new ItemStack(armorMaterial);
    }

    public static double getJumpStrength(final FileConfiguration config)
    {
        return config.getDouble("pegasus.caracteristics.jumpstrength");
    }

    public static boolean hasInventory(final FileConfiguration config)
    {
        return config.getBoolean("pegasus.caracteristics.inventory");
    }

    public static boolean canPickUpItems(final FileConfiguration config)
    {
        return config.getBoolean("pegasus.caracteristics.canpickupitems");
    }

    public static boolean isTakingDamage(final FileConfiguration config)
    {
        return !config.getBoolean("pegasus.damage.none");
    }

    public static boolean isTakingFireDamage(final FileConfiguration config)
    {
        return ConfigHelper.isTakingDamage(config) && config.getBoolean("pegasus.damage.fire");
    }

    public static boolean isTakingDrowningDamage(final FileConfiguration config)
    {
        return ConfigHelper.isTakingDamage(config) && config.getBoolean("pegasus.damage.drowning");
    }

    public static boolean isTakingPlayerDamage(final FileConfiguration config)
    {
        return ConfigHelper.isTakingDamage(config) && config.getBoolean("pegasus.damage.players");
    }

    public static boolean isTakingMonstersDamage(final FileConfiguration config)
    {
        return ConfigHelper.isTakingDamage(config) && config.getBoolean("pegasus.damage.monsters");
    }
}
