package com.daxton.fancyclasses.api.placeholder;

import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.judgment.NumberJudgment;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class ClassPlaceholder {

	public static String valueOf(LivingEntity entity, String inputString){
		UUID uuid = entity.getUniqueId();
		PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
		if(playerClassData != null){
			if(inputString.toLowerCase().contains("<fc_class_name")){
				return playerClassData.className;
			}
			//等級
			if(inputString.toLowerCase().contains("<fc_class_level_now_")){
				String key = inputString.replace(" ","").replace("<fc_class_level_now_","");
				return String.valueOf(playerClassData.getLevel(key));
			}
			if(inputString.toLowerCase().contains("<fc_class_level_max_")){
				String key = inputString.replace(" ","").replace("<fc_class_level_max_","");
				return String.valueOf(playerClassData.getMaxLevel(key));
			}
			//屬性
			if(inputString.toLowerCase().contains("<fc_class_attr_now_")){
				String key = inputString.replace(" ","").replace("<fc_class_attr_now_","");
				return String.valueOf(playerClassData.getAttr(key));
			}
			if(inputString.toLowerCase().contains("<fc_class_attr_max_")){
				String key = inputString.replace(" ","").replace("<fc_class_attr_max_","");
				return String.valueOf(playerClassData.getMaxAttr(key));
			}
			//經驗
			if(inputString.toLowerCase().contains("<fc_class_exp_now_")){
				String key = inputString.replace(" ","").replace("<fc_class_exp_now_","");
				return String.valueOf(playerClassData.getExp(key));
			}
			if(inputString.toLowerCase().contains("<fc_class_exp_max_")){
				String key = inputString.replace(" ","").replace("<fc_class_exp_max_","");
				return String.valueOf(playerClassData.getMaxExp(key));
			}
			//點數
			if(inputString.toLowerCase().contains("<fc_class_point_now_")){
				String key = inputString.replace(" ","").replace("<fc_class_point_now_","");
				return String.valueOf(playerClassData.getPoint(key));
			}
			if(inputString.toLowerCase().contains("<fc_class_point_max_")){
				String key = inputString.replace(" ","").replace("<fc_class_point_max_","");
				return String.valueOf(playerClassData.getMaxPoint(key));
			}
			//技能
			if(inputString.toLowerCase().contains("<fc_class_skill_now_")){
				String key = inputString.replace(" ","").replace("<fc_class_skill_now_","");
				return String.valueOf(playerClassData.getSkillLevel(key));
			}
			if(inputString.toLowerCase().contains("<fc_class_skill_max_")){
				String key = inputString.replace(" ","").replace("<fc_class_skill_max_","");
				return String.valueOf(playerClassData.getMaxSkillLevel(key));
			}
			if(inputString.toLowerCase().contains("<fc_class_skill_use_")){
				String key = inputString.replace(" ","").replace("<fc_class_skill_use_","");
				return String.valueOf(playerClassData.getSkillUse(key));
			}
			//綁定名稱
			if(inputString.toLowerCase().contains("<fc_class_skill_bind_")){
				String key = inputString.replace(" ","").replace("<fc_class_skill_bind_","");
				if(NumberJudgment.isNumber(key)){
					return playerClassData.getBind(Integer.parseInt(key));
				}
			}
		}
		return "0";
	}

}
