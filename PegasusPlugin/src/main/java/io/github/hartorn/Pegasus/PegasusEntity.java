package io.github.hartorn.Pegasus;

import net.minecraft.server.v1_6_R3.EntityHorse;
import net.minecraft.server.v1_6_R3.EntityLiving;
import net.minecraft.server.v1_6_R3.GenericAttributes;
import net.minecraft.server.v1_6_R3.MathHelper;
import net.minecraft.server.v1_6_R3.MobEffectList;
import net.minecraft.server.v1_6_R3.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftHorse;
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

    // Version for craftbukkit 1.6.4-R2.0
    @Override
    public void e(float f, float f1)
    {
        if ((this.passenger != null) && (co())) {
            this.lastYaw = (this.yaw = this.passenger.yaw);
            this.pitch = (this.passenger.pitch * 0.5F);
            b(this.yaw, this.pitch);
            this.aP = (this.aN = this.yaw);
            f = ((EntityLiving) this.passenger).be * 0.5F;
            f1 = ((EntityLiving) this.passenger).bf;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
                // this.bP = 0;
            }

            // if ((this.onGround) && (this.bt == 0.0F) && (ch()) && (!this.bI))
            // {
            if ((this.bt == 0.0F) && (ch())) {
                f = 0.0F;
                f1 = 0.0F;
            }

            // if ((this.bt > 0.0F) && (!cd()) && (this.onGround)) {
            if ((this.bt > 0.0F) && (!cd())) {
                this.motY = (getJumpStrength() * this.bt);
                if (hasEffect(MobEffectList.JUMP)) {
                    this.motY += (getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F;
                }

                j(true);
                this.an = true;
                if (f1 > 0.0F) {
                    final float f2 = MathHelper.sin(this.yaw * 3.141593F / 180.0F);
                    final float f3 = MathHelper.cos(this.yaw * 3.141593F / 180.0F);

                    this.motX += -0.4F * f2 * this.bt;
                    this.motZ += 0.4F * f3 * this.bt;
                    makeSound("mob.horse.jump", 0.4F, 1.0F);
                }

                this.bt = 0.0F;
            }

            this.Y = 1.0F;
            this.aR = (bg() * 0.1F);
            if (!this.world.isStatic) {
                i((float) getAttributeInstance(GenericAttributes.d).getValue());
                super.e(f, f1);
            }

            // if (this.onGround) {
            this.bt = 0.0F;
            j(false);
            // }

            this.aF = this.aG;
            final double d0 = this.locX - this.lastX;
            final double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.aG += (f4 - this.aG) * 0.4F;
            this.aH += this.aG;
        } else {
            this.Y = 0.5F;
            this.aR = 0.02F;
            super.e(f, f1);
        }
    }

}