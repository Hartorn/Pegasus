package io.github.hartorn.Pegasus;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PegasusPlayerCommandExecutor implements CommandExecutor
{
    private final Pegasus pegasusInstance;

    public PegasusPlayerCommandExecutor()
    {
        this.pegasusInstance = Pegasus.getInstance();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String CommandLabel, final String[] args)
    {
        if ((sender instanceof Player)) {
            final Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("pegasus-create") && player.hasPermission("pegasus.create")) {
                final UUID UUIDPlayer = player.getUniqueId();
                final PegasusProperties properties = this.pegasusInstance.getPegasusData().get(UUIDPlayer);
                if (properties == null || properties.getId() == null) {
                    final Horse monture = PegasusEntity.spawn(player);
                    PegasusDataHelper.setPegasusProperties(monture, args, player, true, this.pegasusInstance);
                    this.pegasusInstance.getPegasusData().put(UUIDPlayer, new PegasusProperties(monture));
                    this.pegasusInstance.getPegasusMap().put(monture.getUniqueId(), UUIDPlayer);
                    player.sendMessage("Your pegasus has spawned.");
                } else {
                    player.sendMessage("You already have a pegasus, use /pegasus-respawn to get it back.");
                }
            } else if (cmd.getName().equalsIgnoreCase("pegasus-customise") && player.hasPermission("pegasus.customise")) {
                final UUID UUIDPlayer = player.getUniqueId();
                final PegasusProperties properties = this.pegasusInstance.getPegasusData().get(UUIDPlayer);
                if (properties != null && properties.getId() != null) {
                    if (player.getVehicle() != null && player.getVehicle().getUniqueId().compareTo(properties.getId()) == 0) {
                        final Horse monture = Horse.class.cast(player.getVehicle());
                        PegasusDataHelper.setPegasusProperties(monture, args, player, false, this.pegasusInstance);
                        this.pegasusInstance.getPegasusData().put(UUIDPlayer, new PegasusProperties(monture));
                        player.sendMessage("Your pegasus has been customised.");
                    } else {
                        player.sendMessage("You have to be mounting your pegasus to customise it.");
                    }
                } else {
                    player.sendMessage("You don't have any pegasus. Either respawn it, if yours is dead, or create one using /pegasus-create");
                }
            } else if (cmd.getName().equalsIgnoreCase("pegasus-respawn") && player.hasPermission("pegasus.respawn")) {
                final UUID UUIDPlayer = player.getUniqueId();
                final PegasusProperties properties = this.pegasusInstance.getPegasusData().get(UUIDPlayer);
                if (properties == null) {
                    player.sendMessage("You don't have any pegasus. You can create one using /pegasus-create");
                    return false;
                } else if (properties.getId() == null) {
                    final Horse monture = PegasusEntity.spawn(player);
                    PegasusDataHelper.setPegasusProperties(monture, player, properties, this.pegasusInstance);
                    this.pegasusInstance.getPegasusData().put(UUIDPlayer, new PegasusProperties(monture));
                    this.pegasusInstance.getPegasusMap().put(monture.getUniqueId(), UUIDPlayer);
                    player.sendMessage("Your pegasus has respawned.");
                } else {
                    final Horse horse = PegasusDataHelper.getEntityByUUIDAndByClass(properties.getId(), player.getWorld().getUID(), Horse.class);
                    if (horse != null) {
                        horse.teleport(player, TeleportCause.PLUGIN);
                        player.sendMessage("Your pegasus has been teleported to your location.");
                        return false;
                    }
                    player.sendMessage("Your pegasus has not been found. Please report this.");
                }
            }
        }
        return false;
    }
}