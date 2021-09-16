package com.daxton.fancyclasses.api.dataplayer;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancycore.api.judgment.NumberJudgment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassConfig {

	public static FileConfiguration createConfig(PlayerClassData playerClassData, String uuid){
		File file = new File(FancyClasses.fancyClasses.getDataFolder(), "playerdata/"+uuid+".yml");

		if(!file.exists()){

			try {
				if(file.createNewFile()){

					FileConfiguration deConfig = FileConfig.config_Map.get("class/Default.yml");

					FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);

					//職業模板
					playerConfig.set("Class_Type", "Default");
					//設置職業等級名稱
					String className = deConfig.getString("Class_Name");
					playerConfig.set("Class_Name", className);
					//設置預設頂級設定
					setBaseLevel(playerConfig);
					//職業等級設定
					for(String levelName : getLevelList(deConfig.getStringList("Skills"))){
						setLevel(playerConfig, levelName);
					}
					//屬性點設置
					AttributeConfig.set(playerConfig);
					//綁定設定
					SkillConfig.setBindConfig(playerConfig);

					//設置技能設定
					deConfig.getStringList("Skills").forEach(skillFileName -> {
						SkillConfig.SetSkillConfig(playerConfig, skillFileName);
					});


					FileConfig.config_Map.put("playerdata/"+uuid+".yml", playerConfig);
					playerConfig.save(file);
				}
			}catch (IOException exception){
				//
			}
		}

		if(file.exists()){
			FileConfiguration playerConfig = FileConfig.config_Map.get("playerdata/"+uuid+".yml");

			playerClassData.className = playerConfig.getString("Class_Name");

			for(String levelKey : playerConfig.getConfigurationSection("level").getKeys(false)){
				int now_level = playerConfig.getInt("level."+levelKey+".level");
				int now_exp = playerConfig.getInt("level."+levelKey+".exp");
				int now_point = playerConfig.getInt("level."+levelKey+".point");
				int max_point = playerConfig.getInt("level."+levelKey+".point_max");

				playerClassData.level_Map.put(levelKey, now_level);
				playerClassData.exp_Map.put(levelKey, now_exp);
				playerClassData.point_Map.put(levelKey, now_point);
				playerClassData.point_Map.put(levelKey+"_max", max_point);
			}

			for(String attributeKey : playerConfig.getConfigurationSection("attribute").getKeys(false)){
				int attValue = playerConfig.getInt("attribute."+attributeKey);
				playerClassData.attribute_Map.put(attributeKey, attValue);
			}

			for(String bindKey : playerConfig.getConfigurationSection("bind").getKeys(false)){
				String bindName = playerConfig.getString("bind."+bindKey);
				if(!bindName.equalsIgnoreCase("null")){
					int bindNumber = Integer.parseInt(bindKey)-1;
					playerClassData.bind[bindNumber] = bindName;
				}
			}
			for(String skillName : playerConfig.getConfigurationSection("skill").getKeys(false)){
				int skill_level = playerConfig.getInt("skill."+skillName+".level");
				int skill_use = playerConfig.getInt("skill."+skillName+".use");

				playerClassData.skill_Map.put(skillName, skill_level);
				playerClassData.use_Map.put(skillName, skill_use);
			}
		}

		return FileConfig.config_Map.get("playerdata/"+uuid+".yml");
	}
	//獲取當前等級可獲的點數
	public static int getGivePoint(String levelName, int nowLevel){
		FileConfiguration levelConfig = FileConfig.config_Map.get("level/"+levelName+".yml");
		if(levelConfig != null){
			int givePoint = levelConfig.getInt("level."+nowLevel+".points");
			return givePoint;
		}
		return 0;
	}

	//獲取目前等級最高擁有點數
	public static int getMaxPoint(String levelName, int nowLevel){
		FileConfiguration levelConfig = FileConfig.config_Map.get("level/"+levelName+".yml");
		if(levelConfig != null){
			int point = levelConfig.getInt("level."+nowLevel+".points");
			return point;
		}
		return 0;
	}

	//獲取最高等級
	public static int getMaxLevel(String levelName){
		FileConfiguration levelConfig = FileConfig.config_Map.get("level/"+levelName+".yml");
		if(levelConfig != null){
			List<String> levelList = new ArrayList<>(levelConfig.getConfigurationSection("level").getKeys(false));
			String max = levelList.get(levelList.size()-1);
			if(NumberJudgment.isNumber(max)){
				return Integer.parseInt(max);
			}
		}
		return 0;
	}
	//獲取指定等級最高經驗
	public static int getMaxExp(String levelName, int nowLevel){
		FileConfiguration levelConfig = FileConfig.config_Map.get("level/"+levelName+".yml");
		if(levelConfig != null){
			int exp = levelConfig.getInt("level."+nowLevel+".exp");
			return exp;
		}
		return 0;
	}

	//設置技能設定
	public static void SetSkillConfig(FileConfiguration playerConfig, String skillFillName){
		FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillFillName+".yml");
		if(skillConfig == null){
			return;
		}

		for(String skillName : skillConfig.getConfigurationSection("Skills").getKeys(false)){
			int base = skillConfig.getInt("Skills."+skillName+".Base");
			playerConfig.set("skill."+skillName+".level", base);
			playerConfig.set("skill."+skillName+".use", 0);
		}
	}

	//給予技能名稱列表  回傳等級名稱列表
	public static List<String> getLevelList(List<String> skillList){
		List<String> levelList = new ArrayList<>();
		if(skillList != null && skillList.size() > 0){
			for(String skillName : skillList){
				FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillName+".yml");
				if(skillConfig != null && skillConfig.contains("Level")){
					String levelName = skillConfig.getString("Level");
					FileConfiguration levelConfig = FileConfig.config_Map.get("level/"+levelName+".yml");
					if(levelConfig != null){
						levelList.add(levelName);
					}
				}
			}
		}
		return levelList;
	}

	//設置等級初始設定
	public static void setBaseLevel(FileConfiguration playerConfig){
		if(playerConfig == null){
			return;
		}
		FileConfiguration baseLevelConfig = FileConfig.config_Map.get("level/base.yml");

		List<String> levelList = new ArrayList<>(baseLevelConfig.getConfigurationSection("level").getKeys(false));

		String min = levelList.get(0);
		if(NumberJudgment.isNumber(min)){
			playerConfig.set("level.base.level", Integer.parseInt(min));
		}else {
			playerConfig.set("level.base.level", 0);
		}

		playerConfig.set("level.base.exp", 0);
		playerConfig.set("level.base.point", 0);
		playerConfig.set("level.base.point_max", 0);
	}
	//設置等級初始設定
	public static void setLevel(FileConfiguration playerConfig, String levelName){
		if(playerConfig == null || levelName == null){
			return;
		}
		FileConfiguration baseLevelConfig = FileConfig.config_Map.get("level/"+levelName+".yml");

		List<String> levelList = new ArrayList<>(baseLevelConfig.getConfigurationSection("level").getKeys(false));

		String min = levelList.get(0);

		playerConfig.set("level."+levelName+".level", Integer.parseInt(min));

		playerConfig.set("level."+levelName+".exp", 0);
		playerConfig.set("level."+levelName+".point", 0);
		playerConfig.set("level."+levelName+".point_max", 0);
	}

}
