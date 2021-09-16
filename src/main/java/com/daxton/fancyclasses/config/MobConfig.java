package com.daxton.fancyclasses.config;


import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.api.mobdata.MobClassData;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.config.SearchConfigMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MobConfig {

	public static void execute(){
		ClassesManager.mob_ClassData_Map.clear();
		FileConfiguration mobConfig = FileConfig.config_Map.get("mobs/DefaultMobs.yml");

		List<String> levelList = SearchConfigMap.fileNameList(FileConfig.config_Map, "level/", true);

		Arrays.stream(EntityType.values()).forEach(entityType -> {

			World world = Bukkit.getWorld("world");
			Location location = new Location(world, 0,0,0);
			if(world != null){
				try {
					Entity entity = world.spawnEntity(location, entityType);
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.setSilent(true);
					String mobName = livingEntity.getName();
					livingEntity.remove();
					//經驗值
					Map<String, Integer> exp_Map = new HashMap<>();

					mobConfig.set(mobName+".Type", mobName);
					mobConfig.set(mobName+".Display", mobName);
					for(String levelName : levelList){
						if(!mobConfig.contains(mobName+".exp."+levelName)){
							mobConfig.set(mobName+".exp."+levelName, 1);
						}
						int exp = mobConfig.getInt(mobName+".exp."+levelName);
						exp_Map.put(levelName, exp);
					}
					MobClassData mobClassData = new MobClassData();
					mobClassData.setExp_Map(exp_Map);
					//FancyClasses.fancyClasses.getLogger().info(mobName+" 1");
					ClassesManager.mob_ClassData_Map.put(mobName, mobClassData);

				}catch (ClassCastException | IllegalArgumentException exception){
					//exception.printStackTrace();
				}
				File file = new File(FancyClasses.fancyClasses.getDataFolder(), "mobs/DefaultMobs.yml");
				try {
					mobConfig.save(file);
				}catch (IOException exception){
					//
				}

			}
		});

	}

}
