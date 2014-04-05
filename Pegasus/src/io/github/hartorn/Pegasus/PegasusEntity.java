package io.github.hartorn.Pegasus;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import net.minecraft.server.v1_7_R1.EntityHorse;
import net.minecraft.server.v1_7_R1.EntityLiving;
import net.minecraft.server.v1_7_R1.GenericAttributes;
import net.minecraft.server.v1_7_R1.MathHelper;
import net.minecraft.server.v1_7_R1.MobEffectList;
import net.minecraft.server.v1_7_R1.World;

import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;


public class PegasusEntity extends EntityHorse
{

	public static Horse spawn(Player player){
		Location location = player.getLocation();
		World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
		PegasusEntity customEntity = new PegasusEntity(mcWorld);
		customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
		return (CraftHorse) customEntity.getBukkitEntity();
	}

	public PegasusEntity(World world) {
		super(world);
	}
	
	//Version for craftbukkit 1.7.2-R0.3
	@Override
	 public void e(float f, float f1) {
		    if ((this.passenger != null) && ((this.passenger instanceof EntityLiving)) && (cs())) {
		      this.lastYaw = (this.yaw = this.passenger.yaw);
		      this.pitch = (this.passenger.pitch * 0.5F);
		      b(this.yaw, this.pitch);
		      this.aP = (this.aN = this.yaw);
		      f = ((EntityLiving)this.passenger).be * 0.5F;
		      f1 = ((EntityLiving)this.passenger).bf;
		      if (f1 <= 0.0F) {
		        f1 *= 0.25F;
//		        this.bP = 0;
		      }

//		      if ((this.onGround) && (this.bt == 0.0F) && (cl()) && (!this.bI)) {
		      if ((this.bt == 0.0F) && (cl())) {
		        f = 0.0F;
		        f1 = 0.0F;
		      }

//		      if ((this.bt > 0.0F) && (!ch()) && (this.onGround)) {
		      if ((this.bt > 0.0F) && (!ch())) {
		        this.motY = (getJumpStrength() * this.bt);
		        if (hasEffect(MobEffectList.JUMP)) {
		          this.motY += (getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F;
		        }

		        j(true);
		        this.am = true;
		        if (f1 > 0.0F) {
		          float f2 = MathHelper.sin(this.yaw * 3.141593F / 180.0F);
		          float f3 = MathHelper.cos(this.yaw * 3.141593F / 180.0F);

		          this.motX += -0.4F * f2 * this.bt;
		          this.motZ += 0.4F * f3 * this.bt;
		          makeSound("mob.horse.jump", 0.4F, 1.0F);
		        }

		        this.bt = 0.0F;
		      }

		      this.X = 1.0F;
		      this.aR = (bl() * 0.1F);
		      if (!this.world.isStatic) {
		        i((float)getAttributeInstance(GenericAttributes.d).getValue());
		        super.e(f, f1);
		      }

//		      if (this.onGround) {
		        this.bt = 0.0F;
		        j(false);
//		      }

		      this.aF = this.aG;
		      double d0 = this.locX - this.lastX;
		      double d1 = this.locZ - this.lastZ;
		      float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

		      if (f4 > 1.0F) {
		        f4 = 1.0F;
		      }

		      this.aG += (f4 - this.aG) * 0.4F;
		      this.aH += this.aG;
		    } else {
		      this.X = 0.5F;
		      this.aR = 0.02F;
		      super.e(f, f1);
		    }
		  }
}