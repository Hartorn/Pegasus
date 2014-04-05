package io.github.hartorn.Pegasus;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
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
import org.bukkit.inventory.HorseInventory;

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
        if (event.getEntered() != null && event.getVehicle() != null && event.getEntered().getType().compareTo(EntityType.PLAYER) == 0 && event.getVehicle().getType().compareTo(EntityType.HORSE) == 0 && ((CraftEntity) event.getVehicle()).getHandle() instanceof PegasusEntity) {
            final Player player = Player.class.cast(event.getEntered());
            final Horse monture = Horse.class.cast(event.getVehicle());
            if (!monture.getOwner().equals(player)) {
                player.sendMessage("This is not your Pegasus...");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OpenPegasusInventoryEventHandler(final InventoryOpenEvent event)
    {
        if (event.getPlayer() != null && event.getInventory() != null && event.getInventory() instanceof HorseInventory && ((CraftEntity) event.getInventory().getHolder()).getHandle() instanceof PegasusEntity) {
            final Horse monture = Horse.class.cast(event.getInventory().getHolder());
            if (!monture.getOwner().equals(event.getPlayer())) {
                Player.class.cast(event.getPlayer()).sendMessage("This is not your Pegasus...");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PegasusDeathEvent(final EntityDeathEvent event)
    {
        if (event.getEntity() != null && event.getEntity().getType().compareTo(EntityType.HORSE) == 0) {
            final Horse monture = Horse.class.cast(event.getEntity());
            if (monture.getOwner() != null && monture.getOwner() instanceof Player) {
                final Player player = Player.class.cast(monture.getOwner());
                final PegasusProperties properties = this.pegasusInstance.getPegasusProperties(player.getUniqueId());
                if (properties != null && monture.getUniqueId().compareTo(properties.getId()) == 0) {
                    this.pegasusInstance.setPegasusPropertiesWithUUIDNull(player.getUniqueId(), monture);
                    // monture.getInventory().clear();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void PegasusFallEventHandler(final EntityDamageEvent event)
    {

        if (event.getCause().compareTo(DamageCause.FALL) == 0 && event.getEntity() != null && event.getEntityType().compareTo(EntityType.HORSE) == 0 && ((CraftEntity) event.getEntity()).getHandle() instanceof PegasusEntity) {
            event.setDamage(0);
            event.setCancelled(true);
        }

        if (event.getCause().compareTo(DamageCause.FALL) == 0 && event.getEntityType().compareTo(EntityType.PLAYER) == 0 && (CraftEntity) Player.class.cast(event.getEntity()).getVehicle() != null && ((CraftEntity) Player.class.cast(event.getEntity()).getVehicle()).getHandle() instanceof PegasusEntity) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void PegasusJumpEventHandler(final HorseJumpEvent event)
    {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof PegasusEntity) {
            event.setPower(1);
        }
    }
}