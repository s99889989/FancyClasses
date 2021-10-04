package com.daxton.fancyclasses.api.dataplayer;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancyclasses.skill.SkillBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CommandClass {
	//轉職
	public static void change(Player player, String className){
		UUID uuid = player.getUniqueId();

		File file = new File(FancyClasses.fancyClasses.getDataFolder(), "playerdata/"+uuid+".yml");
		FileConfiguration deConfig = FileConfig.config_Map.get("class/"+className+".yml");


		PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);

		FileConfiguration playerConfig = FileConfig.config_Map.get("playerdata/"+uuid+".yml");
		//職業模板
		playerConfig.set("Class_Type", className);
		//設置職業等級名稱
		String classSetName = deConfig.getString("Class_Name");
		playerConfig.set("Class_Name", classSetName);
		//職業等級設定
		for(String levelName : ClassConfig.getLevelList(deConfig.getStringList("Skills"))){
			ClassConfig.setLevel(playerConfig, levelName);
		}
		//設置技能設定
		deConfig.getStringList("Skills").forEach(skillFileName -> {
			SkillConfig.SetSkillConfig(playerConfig, skillFileName);
		});
		try {
			playerConfig.save(file);
		}catch (IOException exception){
			//
		}


		ClassConfig.createConfig(playerClassData, uuid.toString(), className);

	}
	//重生
	public static void rebirth(Player player, String className){
		UUID uuid = player.getUniqueId();
		PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
		File file = new File(FancyClasses.fancyClasses.getDataFolder(), "playerdata/"+uuid+".yml");
		file.delete();

		ClassConfig.createConfig(playerClassData, uuid.toString(), className);
		playerClassData.setDefaultCustomValue();

		playerClassData.skillBar = new SkillBar(player, playerClassData);

	}

}
