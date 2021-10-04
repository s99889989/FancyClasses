package com.daxton.fancyclasses.api.dataplayer;

import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancycore.api.config.SearchConfigMap;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SkillConfig {

	//技能最高技能等級
	public static int getMaxSkill(String skillName){
		for(FileConfiguration configuration : SearchConfigMap.configList(FileConfig.config_Map, "skill/")){
			if(configuration.contains("Skills."+skillName+".MaxLevel")){
				return configuration.getInt("Skills."+skillName+".MaxLevel");
			}
		}
		return 0;
	}

	//設置綁定設定
	public static void setBindConfig(FileConfiguration playerConfig){
		for(int i = 1 ; i <= 8 ; i++){
			playerConfig.set("bind."+i, "null");
		}
	}

	//設置技能設定
	public static void SetSkillConfig(FileConfiguration playerConfig, String skillFillName){
		FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillFillName+".yml");
		if(skillConfig == null){
			return;
		}

		for(String skillName : skillConfig.getConfigurationSection("Skills").getKeys(false)){
			int base = skillConfig.getInt("Skills."+skillName+".Base");
			if(!playerConfig.contains("skill."+skillName+".level")){
				playerConfig.set("skill."+skillName+".level", base);
			}
			if(!playerConfig.contains("skill."+skillName+".use")){
				playerConfig.set("skill."+skillName+".use", 0);
			}



		}
	}

}
