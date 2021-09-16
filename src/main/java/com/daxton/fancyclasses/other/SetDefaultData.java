package com.daxton.fancyclasses.other;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.config.SearchConfigMap;
import com.daxton.fancycore.api.judgment.NumberJudgment;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SetDefaultData {
	//設置等級基礎值
	public static void level(){
		ClassesManager.max_Level_Map.clear();
		SearchConfigMap.fileNameList(FileConfig.config_Map, "level/", true).forEach(levelConfigName -> {
			FileConfiguration levelConfig = FileConfig.config_Map.get("level/"+levelConfigName+".yml");
			List<String> levelList = new ArrayList<>(levelConfig.getConfigurationSection("level").getKeys(false));
			String max = levelList.get(levelList.size()-1);
			if(NumberJudgment.isNumber(max)){
				ClassesManager.max_Level_Map.put(levelConfigName, Integer.parseInt(max));
			}
			levelList.forEach(levelKey -> {
				if(NumberJudgment.isNumber(levelKey)){
					int needExp = levelConfig.getInt("level."+levelKey+".exp");
					ClassesManager.need_Exp_Map.put(levelConfigName+levelKey, needExp);
				}
			});
		});
	}
	//設置技能基礎值
	public static void skill(){
		ClassesManager.max_Skill_Map.clear();
		ClassesManager.skill_Config_Map.clear();

		SearchConfigMap.filePathList(FileConfig.config_Map, "skill/", false).forEach(skillConfigName -> {
			FileConfiguration skillConfig = FileConfig.config_Map.get(skillConfigName);

			skillConfig.getConfigurationSection("Skills").getKeys(false).forEach(skillName -> {
				int baseLeve = skillConfig.getInt("Skills."+skillName+".Base");
				int maxLeve = skillConfig.getInt("Skills."+skillName+".MaxLevel");
				ClassesManager.max_Skill_Map.put(skillName, maxLeve);
				ClassesManager.skill_Config_Map.put(skillName, skillConfigName);
			});

		});
	}

}
