package io.github.hartorn.Pegasus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_6_R3.BiomeBase;
import net.minecraft.server.v1_6_R3.BiomeMeta;
import net.minecraft.server.v1_6_R3.EntityTypes;

public class CustomEntityHelper
{
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void registerEntities()
    {
        for (final CustomEntityType entity : CustomEntityType.values()) {
            try {
                final Field b = EntityTypes.class.getDeclaredField("b");
                final Field c = EntityTypes.class.getDeclaredField("c");
                // final Field d = EntityTypes.class.getDeclaredField("d");
                final Field e = EntityTypes.class.getDeclaredField("e");
                final Field f = EntityTypes.class.getDeclaredField("f");

                b.setAccessible(true);
                c.setAccessible(true);
                // d.setAccessible(true);
                e.setAccessible(true);
                f.setAccessible(true);

                final Map bMap = (Map) b.get(null);
                final Map cMap = (Map) c.get(null);
                // final Map dMap = (Map) d.get(null);
                final Map eMap = (Map) e.get(null);
                final Map fMap = (Map) f.get(null);

                bMap.put(entity.getName(), entity.getCustomClass());
                cMap.put(entity.getCustomClass(), entity.getName());
                // dMap.put(entity.getID(), entity.getCustomClass());
                eMap.put(entity.getCustomClass(), entity.getID());
                fMap.put(entity.getName(), entity.getID());

                b.set(null, bMap);
                c.set(null, cMap);
                // d.set(null, dMap);
                e.set(null, eMap);
                f.set(null, fMap);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        for (final BiomeBase biomeBase : BiomeBase.biomes) {
            if (biomeBase == null) {
                break;
            }

            for (final String field : new String[] { "J", "K", "L", "M" }) {
                try {
                    final Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    final List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);

                    for (final BiomeMeta meta : mobList) {
                        for (final CustomEntityType entity : CustomEntityType.values()) {
                            if (entity.getNMSClass().equals(meta.b)) {
                                meta.b = entity.getCustomClass();
                            }
                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}