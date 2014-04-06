package io.github.hartorn.Pegasus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PegasusAdministratorCommandExecutor implements CommandExecutor
{
    private final Pegasus pegasusInstance;

    public PegasusAdministratorCommandExecutor()
    {
        this.pegasusInstance = Pegasus.getInstance();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String CommandLabel, final String[] args)
    {
        final boolean isPlayer = (sender instanceof Player);
        Player player = null;
        if (isPlayer) {
            player = (Player) sender;
        }
        int i;
        if (cmd.getName().equalsIgnoreCase("pegasus-clear") && (!isPlayer || player.hasPermission("pegasus.clear"))) {
            i = PegasusDataHelper.KillPegasus(this.pegasusInstance);
            if (isPlayer) {
                player.sendMessage("Cleaning done. Number of killed Pegasus : " + i);
            }
            this.pegasusInstance.getLogger().info("Cleaning done. Number of killed Pegasus : " + i);
        } else if (cmd.getName().equalsIgnoreCase("pegasus-forceclear") && (!isPlayer || player.hasPermission("pegasus.forceclear"))) {
            i = PegasusDataHelper.KillPegasus(this.pegasusInstance, true);
            if (isPlayer) {
                player.sendMessage("Cleaning done. Number of killed horse : " + i);
            }
            this.pegasusInstance.getLogger().info("Cleaning done. Number of killed horse : " + i);
        }
        return false;
    }
}
