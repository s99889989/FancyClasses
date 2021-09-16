package com.daxton.fancyclasses.config;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.api.mobdata.MobClassData;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.config.ConfigLoad;
import com.daxton.fancycore.api.config.SearchConfigMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MythicMobConfig {

	public static void execute(){
		if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null){
			Map<String, FileConfiguration> mob_Config_Map = ConfigLoad.execute("plugins\\MythicMobs\\Mobs");
			mob_Config_Map.forEach(MythicMobConfig::create);
		}
	}

	//建立怪物設定檔
	public static void create(String fileName, FileConfiguration loadConfig){
		File saveFile = new File(FancyClasses.fancyClasses.getDataFolder(), "mobs/"+fileName);
		if(!saveFile.exists()){
			try {
				saveFile.createNewFile();
			}catch (IOException exception){
				exception.printStackTrace();
			}
		}
		FileConfiguration saveConfig = YamlConfiguration.loadConfiguration(saveFile);

		List<String> levelList = SearchConfigMap.fileNameList(FileConfig.config_Map, "level/", true);

		loadConfig.getConfigurationSection("").getKeys(false).forEach(mobID->{
			String type = loadConfig.getString(mobID+".Type");
			saveConfig.set(mobID +".Type", type);
			String display = loadConfig.getString(mobID+".Display");
			saveConfig.set(mobID +".Display", display);

			//經驗值
			Map<String, Integer> exp_Map = new HashMap<>();

			for(String levelName : levelList){
				if(!saveConfig.contains(mobID+".exp."+levelName)){
					saveConfig.set(mobID+".exp."+levelName, 1);
				}
				int exp = saveConfig.getInt(mobID+".exp."+levelName);
				exp_Map.put(levelName, exp);
			}

			MobClassData mobClassData = new MobClassData();
			mobClassData.setExp_Map(exp_Map);
			//FancyClasses.fancyClasses.getLogger().info(mobID+" 0");
			ClassesManager.mob_ClassData_Map.put(mobID, mobClassData);

			try {
				saveConfig.save(saveFile);
			}catch (IOException exception){
				exception.printStackTrace();
			}

		});


	}

}
