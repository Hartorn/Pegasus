package io.github.hartorn.Pegasus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_7_R2.BiomeBase;
import net.minecraft.server.v1_7_R2.BiomeMeta;
import net.minecraft.server.v1_7_R2.EntityTypes;

public class CustomEntityHelper
{
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void registerEntities()
    {
        for (final CustomEntityType entity : CustomEntityType.values()) {
            try {
                final Field c = EntityTypes.class.getDeclaredField("c");
                final Field d = EntityTypes.class.getDeclaredField("d");
                // final Field e = EntityTypes.class.getDeclaredField("e");
                final Field f = EntityTypes.class.getDeclaredField("f");
                final Field g = EntityTypes.class.getDeclaredField("g");

                c.setAccessible(true);
                d.setAccessible(true);
                // e.setAccessible(true);
                f.setAccessible(true);
                g.setAccessible(true);

                final Map cMap = (Map) c.get(null);
                final Map dMap = (Map) d.get(null);
                // final Map eMap = (Map) e.get(null);
                final Map fMap = (Map) f.get(null);
                final Map gMap = (Map) g.get(null);

                cMap.put(entity.getName(), entity.getCustomClass());
                dMap.put(entity.getCustomClass(), entity.getName());
                // eMap.put(entity.getID(), entity.getCustomClass());
                fMap.put(entity.getCustomClass(), entity.getID());
                gMap.put(entity.getName(), entity.getID());

                c.set(null, cMap);
                d.set(null, dMap);
                // e.set(null, eMap);
                f.set(null, fMap);
                g.set(null, gMap);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        for (final BiomeBase biomeBase : BiomeBase.n()) {
            if (biomeBase == null) {
                break;
            }

            for (final String field : new String[] { "as", "at", "au", "av" }) {
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
