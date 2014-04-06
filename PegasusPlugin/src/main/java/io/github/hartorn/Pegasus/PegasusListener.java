package io.github.hartorn.Pegasus;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class PegasusListener implements Listener
{
    private final Pegasus pegasusInstance;

    public PegasusListener()
    {
        this.pegasusInstance = Pegasus.getInstance();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void MountPegasusEventHandler(final VehicleEnterEvent event)
    {
        if (event.getEntered().getType().compareTo(EntityType.PLAYER) == 0 && event.getVehicle().getType().compareTo(EntityType.HORSE) == 0) {
            final Player player = Player.class.cast(event.getEntered());
            final UUID playerId = this.pegasusInstance.getPegasusOwnerUUID(event.getVehicle().getUniqueId());
            if (playerId != null && player.getUniqueId().compareTo(playerId) != 0) {
                player.sendMessage("This is not your Pegasus...");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OpenPegasusInventoryEventHandler(final InventoryOpenEvent event)
    {
        if (event.getInventory().getHolder() instanceof Horse) {
            final Horse monture = Horse.class.cast(event.getInventory().getHolder());
            final UUID playerId = this.pegasusInstance.getPegasusOwnerUUID(monture.getUniqueId());
            if (playerId != null && event.getPlayer().getUniqueId().compareTo(playerId) != 0) {
                Player.class.cast(event.getPlayer()).sendMessage("This is not your Pegasus...");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PegasusDeathEvent(final EntityDeathEvent event)
    {
        if (event.getEntity() != null && event.getEntity().getType().compareTo(EntityType.HORSE) == 0) {
            final UUID montureId = event.getEntity().getUniqueId();
            final Player player = this.pegasusInstance.getPegasusOwner(montureId);

            if (player != null && montureId != null) {
                this.pegasusInstance.setPegasusPropertiesWithUUIDNull(player.getUniqueId());
                this.pegasusInstance.getPegasusMap().remove(montureId);
                // monture.getInventory().clear();
                player.sendMessage("Your pegasus died... You can get it back using /pegasus-respawn.");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void PegasusFallEventHandler(final EntityDamageEvent event)
    {

        if (event.getCause().compareTo(DamageCause.FALL) == 0 && this.pegasusInstance.getPegasusOwnerUUID(event.getEntity().getUniqueId()) != null) {
            event.setDamage(0);
            event.setCancelled(true);
            return;
        }

        if (event.getCause().compareTo(DamageCause.FALL) == 0 && event.getEntityType().compareTo(EntityType.PLAYER) == 0) {
            final Player player = Player.class.cast(event.getEntity());
            final Entity monture = player.getVehicle();
            if (monture != null) {
                final UUID playerId = this.pegasusInstance.getPegasusOwnerUUID(monture.getUniqueId());
                if (playerId != null && playerId.compareTo(player.getUniqueId()) == 0) {
                    event.setDamage(0);
                    event.setCancelled(true);
                }
            }

        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void PegasusJumpEventHandler(final HorseJumpEvent event)
    {
        if (this.pegasusInstance.getPegasusMap().get(event.getEntity().getUniqueId()) != null) {
            event.setPower(1);
        }
    }
}