package io.github.hartorn.Pegasus;

import net.minecraft.server.v1_7_R2.EntityHorse;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PegasusEntity extends EntityHorse
{

    public static Horse spawn(final Player player)
    {
        final Location location = player.getLocation();
        final World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        final PegasusEntity customEntity = new PegasusEntity(mcWorld);
        customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (CraftHorse) customEntity.getBukkitEntity();
    }

    public PegasusEntity(final World world)
    {
        super(world);
    }

    // Version for craftbukkit 1.7.5-R0.1
    @Override
    public void e(float f, float f1)
    {
        if ((this.passenger != null) && ((this.passenger instanceof EntityLiving)) && (cu())) {
            this.lastYaw = (this.yaw = this.passenger.yaw);
            this.pitch = (this.passenger.pitch * 0.5F);
            b(this.yaw, this.pitch);
            this.aO = (this.aM = this.yaw);
            f = ((EntityLiving) this.passenger).bd * 0.5F;
            f1 = ((EntityLiving) this.passenger).be;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
                // this.bP = 0;
            }

            // if ((this.onGround) && (this.bt == 0.0F) && (cn()) && (!this.bI))
            // {
            // if ((this.onGround) && (this.bt == 0.0F) && (cn())) {
            if ((this.bt == 0.0F) && (cn())) {
                f = 0.0F;
                f1 = 0.0F;
            }

            // if ((this.bt > 0.0F) && (!cj()) && (this.onGround)) {
            if ((this.bt > 0.0F) && (!cj())) {
                this.motY = (getJumpStrength() * this.bt);
                if (hasEffect(MobEffectList.JUMP)) {
                    this.motY += (getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F;
                }

                j(true);
                this.al = true;
                if (f1 > 0.0F) {
                    final float f2 = MathHelper.sin(this.yaw * 3.141593F / 180.0F);
                    final float f3 = MathHelper.cos(this.yaw * 3.141593F / 180.0F);

                    this.motX += -0.4F * f2 * this.bt;
                    this.motZ += 0.4F * f3 * this.bt;
                    makeSound("mob.horse.jump", 0.4F, 1.0F);
                }

                this.bt = 0.0F;
            }

            this.W = 1.0F;
            this.aQ = (bk() * 0.1F);
            if (!this.world.isStatic) {
                i((float) getAttributeInstance(GenericAttributes.d).getValue());
                super.e(f, f1);
            }

            // if (this.onGround) {
            this.bt = 0.0F;
            j(false);
            // }

            this.aE = this.aF;
            final double d0 = this.locX - this.lastX;
            final double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.aF += (f4 - this.aF) * 0.4F;
            this.aG += this.aF;
        } else {
            this.W = 0.5F;
            this.aQ = 0.02F;
            super.e(f, f1);
        }
    }
}