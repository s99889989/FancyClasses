package com.daxton.fancyclasses.api.dataplayer;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancycore.api.judgment.NumberJudgment;
import com.daxton.fancycore.manager.PlayerManagerCore;
import com.daxton.fancycore.other.playerdata.PlayerDataFancy;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AttributeConfig {
	//設置初始玩家屬性
	public static void set(FileConfiguration playerConfig){
		FileConfiguration attrConfig = FileConfig.config_Map.get("character-attributes.yml");

		attrConfig.getConfigurationSection("Point").getKeys(false).forEach(key -> {
			int baseValue = attrConfig.getInt("Point."+key+".base");
			playerConfig.set("attribute."+key, baseValue);


		});

	}
	//獲取最高屬性值
	public static int getMax(){
		FileConfiguration attrConfig = FileConfig.config_Map.get("character-attributes.yml");

		List<String> levelList = new ArrayList<>(attrConfig.getConfigurationSection("cost").getKeys(false));

		String max = levelList.get(levelList.size()-1);

		if(NumberJudgment.isNumber(max)){
			return Integer.parseInt(max);
		}
		return 0;
	}

	public static int getNeed(int attr){
		FileConfiguration attrConfig = FileConfig.config_Map.get("character-attributes.yml");
		if(attrConfig.contains("cost."+attr)){
			return attrConfig.getInt("cost."+attr);
		}
		return 0;
	}

	//增加屬性
	public static void setDefaultValue(UUID uuid, Map<String, Integer> attribute_Map){
		FileConfiguration attrConfig = FileConfig.config_Map.get("character-attributes.yml");

		PlayerDataFancy playerDataFancy = PlayerManagerCore.player_Data_Map.get(uuid);

		attribute_Map.forEach((key, integer) -> {
			attrConfig.getConfigurationSection("Point."+key+".custom-value-add").getKeys(false).forEach(custom_key -> {
				int custom_value = attrConfig.getInt("Point."+key+".custom-value-add."+custom_key);
				int addValue = integer * custom_value;
				//FancyClasses.fancyClasses.getLogger().info("職業: "+custom_key+" : "+addValue+" : "+key);
				playerDataFancy.addCustomValue(custom_key, String.valueOf(addValue), key);
			});
		});

	}
	//增加單獨屬性
	public static void setCustomValue(UUID uuid, Map<String, Integer> attribute_Map, String key){
		FileConfiguration attrConfig = FileConfig.config_Map.get("character-attributes.yml");

		PlayerDataFancy playerDataFancy = PlayerManagerCore.player_Data_Map.get(uuid);

		attrConfig.getConfigurationSection("Point."+key+".custom-value-add").getKeys(false).forEach(custom_key -> {
			int custom_value = attrConfig.getInt("Point."+key+".custom-value-add."+custom_key);
			int addValue = attribute_Map.get(key) * custom_value;
			//FancyClasses.fancyClasses.getLogger().info("職業: "+custom_key+" : "+addValue+" : "+key);
			playerDataFancy.addCustomValue(custom_key, String.valueOf(addValue), key);
		});

	}

}
