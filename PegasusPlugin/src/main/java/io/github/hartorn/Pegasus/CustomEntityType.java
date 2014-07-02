package io.github.hartorn.Pegasus;

import net.minecraft.server.v1_7_R3.EntityHorse;
import net.minecraft.server.v1_7_R3.EntityInsentient;

import org.bukkit.entity.EntityType;

public enum CustomEntityType
{

    PEGASUS("Horse", 100, EntityType.HORSE, EntityHorse.class, PegasusEntity.class);

    private Class<? extends EntityInsentient> customClass;
    private EntityType entityType;
    private int id;
    private String name;
    private Class<? extends EntityInsentient> nmsClass;

    private CustomEntityType(final String name, final int id, final EntityType entityType, final Class<? extends EntityInsentient> nmsClass, final Class<? extends EntityInsentient> customClass)
    {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public Class<? extends EntityInsentient> getCustomClass()
    {
        return this.customClass;
    }

    public EntityType getEntityType()
    {
        return this.entityType;
    }

    public int getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public Class<? extends EntityInsentient> getNMSClass()
    {
        return this.nmsClass;
    }
}