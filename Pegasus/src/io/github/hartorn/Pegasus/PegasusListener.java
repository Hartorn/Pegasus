package io.github.hartorn.Pegasus;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PegasusListener
implements Listener
{
	private Pegasus plugin;

	public PegasusListener(Pegasus instance)
	{
		this.plugin = instance;
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void HorseJumpEventHandler(HorseJumpEvent event) {
		if(((CraftEntity)event.getEntity()).getHandle() instanceof PegasusEntity )
		{
			event.setPower(1);
//			Bukkit.getLogger().log(Level.INFO, "JUMP EVENT");
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void HorseFallEventHandler(EntityDamageEvent event) {

		if(event.getCause().compareTo(DamageCause.FALL) == 0 && event.getEntity()!=null && event.getEntityType().compareTo(EntityType.HORSE)==0 && ((CraftEntity)event.getEntity()).getHandle() instanceof PegasusEntity)
		{
//			Bukkit.getLogger().log(Level.INFO, "FALL EVENT");
			event.setDamage(0);
			event.setCancelled(true);
		}

		if (event.getEntityType().compareTo(EntityType.PLAYER)==0 && (CraftEntity)Player.class.cast(event.getEntity()).getVehicle()!=null && ((CraftEntity)Player.class.cast(event.getEntity()).getVehicle()).getHandle() instanceof PegasusEntity)
		{
//			Bukkit.getLogger().log(Level.INFO, "PASSENGER FALL EVENT");
			event.setDamage(0);
			event.setCancelled(true);
		}
	}
}