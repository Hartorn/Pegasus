package io.github.hartorn.Pegasus;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_7_R1.BiomeBase;
import net.minecraft.server.v1_7_R1.BiomeMeta;
import net.minecraft.server.v1_7_R1.EntityTypes;


public class CustomEntityHelper {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerEntities(){
		for (CustomEntityType entity : CustomEntityType.values()){
			try {
				Field c = EntityTypes.class.getDeclaredField("c");
				Field d = EntityTypes.class.getDeclaredField("d");
				Field e = EntityTypes.class.getDeclaredField("e");
				Field f = EntityTypes.class.getDeclaredField("f");
				Field g = EntityTypes.class.getDeclaredField("g");

				c.setAccessible(true);
				d.setAccessible(true);
				e.setAccessible(true);
				f.setAccessible(true);
				g.setAccessible(true);

				Map cMap = (Map) c.get(null);
				Map dMap = (Map) d.get(null);
				Map eMap = (Map) e.get(null);
				Map fMap = (Map) f.get(null);
				Map gMap = (Map) g.get(null);

				cMap.put(entity.getName(), entity.getCustomClass());
				dMap.put(entity.getCustomClass(), entity.getName());
				eMap.put(entity.getID(), entity.getCustomClass());
				fMap.put(entity.getCustomClass(), entity.getID());
				gMap.put(entity.getName(), entity.getID());

				c.set(null, cMap);
				d.set(null, dMap);
				e.set(null, eMap);
				f.set(null, fMap);
				g.set(null, gMap);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		for (BiomeBase biomeBase : BiomeBase.n()){
			if (biomeBase == null){
				break;
			}

			for (String field : new String[]{"as", "at", "au", "av"}){
				try{
					Field list = BiomeBase.class.getDeclaredField(field);
					list.setAccessible(true);
					List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);

					for (BiomeMeta meta : mobList){
						for (CustomEntityType entity : CustomEntityType.values()){
							if (entity.getNMSClass().equals(meta.b)){
								meta.b = entity.getCustomClass();
							}
						}
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}