package io.github.hartorn.Pegasus;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;

public class PegasusListener implements Listener
{
    private final Pegasus pegasusInstance;

    public PegasusListener()
    {
        this.pegasusInstance = Pegasus.getInstance();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void SpawnPegasusOnPlayerJoining(final PlayerRespawnEvent event)
    {
        PegasusDataHelper.respawnPegasusByOwnerId(event.getPlayer(), this.pegasusInstance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void UnloadPegasusOnPlayerQuiting(final PlayerQuitEvent event)
    {
        PegasusDataHelper.unloadPegasusByOwnerId(event.getPlayer().getUniqueId(), this.pegasusInstance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void UnloadPegasusOnPlayerKicked(final PlayerKickEvent event)
    {
        PegasusDataHelper.unloadPegasusByOwnerId(event.getPlayer().getUniqueId(), this.pegasusInstance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void UnloadPegasusOnWorldUnloading(final WorldUnloadEvent event)
    {
        PegasusDataHelper.unloadPegasusInList(event.getWorld().getEntities(), this.pegasusInstance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void UnloadPegasusOnChunkUnload(final ChunkUnloadEvent event)
    {
        if (event.getChunk() != null) {
            PegasusDataHelper.unloadPegasusInArray(event.getChunk().getEntities(), this.pegasusInstance);
        }
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
                this.pegasusInstance.removePegasusByOwnerUUID(player.getUniqueId());
                if (event.getEntity().getType().equals(EntityType.HORSE)) {
                    final Horse monture = Horse.class.cast(event.getEntity());
                    if (monture.getInventory() != null && ConfigHelper.hasInventory(this.pegasusInstance.getConfig()) && ConfigHelper.isKeepingInventoryOnDeath(this.pegasusInstance.getConfig())) {
                        this.pegasusInstance.getPegasusProperties(player.getUniqueId()).setInventoryContents(InventorySerializer.getSerializedInventory(monture.getInventory()));
                        monture.getInventory().clear();
                        monture.setCarryingChest(false);
                        event.getDrops().clear();
                    }
                    event.setDroppedExp(0);
                    monture.getInventory().setSaddle(null);
                    event.getDrops().remove(new ItemStack(Material.SADDLE));
                }
                player.sendMessage("Your pegasus died... You can get it back using /pegasus-respawn.");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void PegasusDamageHandler(final EntityDamageEvent event)
    {
        // Cancel fall damage on the target, if it is registered as a Pegasus
        if (event.getCause().compareTo(DamageCause.FALL) == 0 && this.pegasusInstance.getPegasusOwnerUUID(event.getEntity().getUniqueId()) != null) {
            event.setDamage(0);
            event.setCancelled(true);
            return;
        }
        // Cancel fall damage for the player, if he is mounting a Pegasus
        else if (event.getCause().compareTo(DamageCause.FALL) == 0 && event.getEntityType().compareTo(EntityType.PLAYER) == 0) {
            final Player player = Player.class.cast(event.getEntity());
            final Entity monture = player.getVehicle();
            if (monture != null) {
                final UUID playerId = this.pegasusInstance.getPegasusOwnerUUID(monture.getUniqueId());
                if (playerId != null && playerId.compareTo(player.getUniqueId()) == 0) {
                    event.setDamage(0);
                    event.setCancelled(true);
                    return;
                }
            }
        }
        // Cancel damage coming from different sources, given the configuration
        if (event.getEntity() != null && this.pegasusInstance.getPegasusOwnerUUID(event.getEntity().getUniqueId()) != null) {
            final DamageCause cause = event.getCause();
            boolean toCancel = false;
            if (!ConfigHelper.isTakingDamage(this.pegasusInstance.getConfig())) {
                toCancel = true;
            } else {
                switch (cause) {
                    case LAVA:
                    case FIRE_TICK:
                    case FIRE:
                        if (!ConfigHelper.isTakingFireDamage(this.pegasusInstance.getConfig())) {
                            toCancel = true;
                        }
                        break;
                    case DROWNING:
                        if (!ConfigHelper.isTakingDrowningDamage(this.pegasusInstance.getConfig())) {
                            toCancel = true;
                        }
                        break;
                    default:
                        break;
                }
            }
            event.setCancelled(toCancel);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void PegasusDamageHandler(final EntityDamageByEntityEvent event)
    {
        if (event.getEntity() != null && this.pegasusInstance.getPegasusOwnerUUID(event.getEntity().getUniqueId()) != null) {
            boolean toCancel = false;

            if (!ConfigHelper.isTakingDamage(this.pegasusInstance.getConfig())) {
                toCancel = true;
            } else {
                final EntityType type = event.getDamager().getType();
                switch (type) {
                    case PLAYER:
                        if (!ConfigHelper.isTakingPlayerDamage(this.pegasusInstance.getConfig())) {
                            toCancel = true;
                        }
                        break;
                    case BAT:
                    case BLAZE:
                    case CAVE_SPIDER:
                    case CREEPER:
                    case ENDERMAN:
                    case ENDER_DRAGON:
                    case GHAST:
                    case GIANT:
                    case IRON_GOLEM:
                    case MAGMA_CUBE:
                    case MUSHROOM_COW:
                    case OCELOT:
                    case PIG_ZOMBIE:
                    case SILVERFISH:
                    case SKELETON:
                    case SLIME:
                    case SPIDER:
                    case WITCH:
                    case WITHER:
                    case WITHER_SKULL:
                    case WOLF:
                    case ZOMBIE:
                        if (!ConfigHelper.isTakingMonstersDamage(this.pegasusInstance.getConfig())) {
                            toCancel = true;
                        }
                        break;
                    default:
                        break;
                }
            }
            event.setCancelled(toCancel);
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