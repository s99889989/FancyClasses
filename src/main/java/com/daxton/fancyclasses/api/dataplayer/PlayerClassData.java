package com.daxton.fancyclasses.api.dataplayer;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancyclasses.skill.SkillBar;
import com.daxton.fancycore.api.gui.GUI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerClassData{

	final UUID uuid;

	public String className = "";
	//等級
	Map<String, Integer> level_Map = new HashMap<>();
	//經驗
	Map<String, Integer> exp_Map = new HashMap<>();
	//點數
	Map<String, Integer> point_Map = new HashMap<>();
	//屬性
	Map<String, Integer> attribute_Map = new HashMap<>();
	//技能目前等級
	Map<String, Integer> skill_Map = new HashMap<>();
	//技能使用等級
	Map<String, Integer> use_Map = new HashMap<>();
	//綁定名稱
	public String[] bind = new String[8];
	public String selectSkill;
	public SkillBar skillBar;
	public BukkitRunnable castTime;
	public BukkitRunnable castDelay;

	//Gui
	public GUI gui;

	public boolean player_F;
	//建立初始玩家設定
	public PlayerClassData(Player player){
		this.uuid = player.getUniqueId();
		createConfig();
		setDefaultCustomValue();
		skillBar = new SkillBar(player, this);
	}
	//登入啟動全部自訂值
	public void setDefaultCustomValue(){
		AttributeConfig.setDefaultValue(uuid, attribute_Map);
	}
	//設置單個自訂值
	public void setCustomValue(String key){
		AttributeConfig.setCustomValue(uuid, attribute_Map, key);
	}

	//屬性
	//---------------------------------------------------------------------------------------------------//

	//增加屬性
	public int addAttr(String attrName, int add, boolean needBase){
		if(add == 0){
			return 0;
		}

		if(attribute_Map.containsKey(attrName)){

			if(add > 0){
				int needPoint = AttributeConfig.getNeed(getAttr(attrName));
				int basePoint = getPoint("base");
				//FancyClasses.fancyClasses.getLogger().info(basePoint+" : "+needPoint);
				if(needBase && basePoint < needPoint){
					return 0;
				}
				int oV = attribute_Map.get(attrName);
				oV += add;

				attribute_Map.put(attrName, oV);
				setCustomValue(attrName);
				return needPoint;

			}else {
				int needPoint = AttributeConfig.getNeed(getAttr(attrName)-1);

				int oV = attribute_Map.get(attrName);
				oV += add;

				if(oV < 0){
					return 0;
				}

				attribute_Map.put(attrName, oV);
				setCustomValue(attrName);
				return needPoint*-1;

			}

		}
		return 0;
	}


	//獲取目前屬性
	public int getAttr(String attrName){
		if(attribute_Map.containsKey(attrName)){
			return attribute_Map.get(attrName);
		}
		return 0;
	}
	//獲取屬性上限
	public int getMaxAttr(String attrName){
		return AttributeConfig.getMax();
	}

	//點數
	//---------------------------------------------------------------------------------------------------//

	//消耗點數
	public void usePoint(String pointName, int add){
		if(point_Map.containsKey(pointName)){
			int oV = point_Map.get(pointName);
			oV += add;

			if(oV < 0){
				return;
			}
			point_Map.put(pointName, oV);
		}
	}
	//增加點數
	public void addPoint(String pointName, int add){
		if(point_Map.containsKey(pointName)){
			int pMin = point_Map.get(pointName);
			pMin += add;

			point_Map.put(pointName, pMin);
		}
		if(point_Map.containsKey(pointName+"_max")){
			int pMax = point_Map.get(pointName+"_max");
			pMax += add;
			point_Map.put(pointName+"_max", pMax);
		}
	}
	//獲得剩餘可使用點數
	public int getPoint(String pointName){
		if(point_Map.containsKey(pointName)){
			return point_Map.get(pointName);
		}
		return 0;
	}
	//獲得目前總點數
	public int getMaxPoint(String pointName){
		if(point_Map.containsKey(pointName+"_max")){
			return point_Map.get(pointName+"_max");
		}
		return 0;
	}
	//技能
	//---------------------------------------------------------------------------------------------------//
	//改變技能使用等級
	public void addUseSkill(String skillName, int level){
		if(level == 0){
			return;
		}
		if(use_Map.containsKey(skillName)){
			int nowLevel = use_Map.get(skillName);
			if(level > 0){
				nowLevel += level;
				if(nowLevel <= getSkillLevel(skillName)){
					use_Map.put(skillName, nowLevel);
				}
			}else {
				nowLevel += level;
				if(nowLevel >= 0){
					use_Map.put(skillName, nowLevel);
				}
			}

		}


	}

	//改變技能等級
	public void addSkill(String skillName, String pointName, int level, boolean needPoint){
		if(level == 0){
			return;
		}
		if(skill_Map.containsKey(skillName)){
			int nowLevel = skill_Map.get(skillName);
			nowLevel += level;
			if(nowLevel < 0 || nowLevel > getMaxSkillLevel(skillName)){
				return;
			}
			if(needPoint){
				if(pointName == null){
					return;
				}
				if(level > 0){
					if(level <= getPoint(pointName)){
						usePoint(pointName, -1);
						skill_Map.put(skillName, nowLevel);
					}
				}else {
					if(getSkillLevel(skillName) > 0){
						usePoint(pointName, 1);
						skill_Map.put(skillName, nowLevel);

					}
				}
			}else {
				skill_Map.put(skillName, nowLevel);

			}
		}
	}

	//獲取技能最高等級
	public int getMaxSkillLevel(String skillName){
		return SkillConfig.getMaxSkill(skillName);
	}
	//獲取技能等級
	public int getSkillLevel(String skillName){
		if(skill_Map.containsKey(skillName)){
			return skill_Map.get(skillName);
		}
		return 0;
	}
	//獲取技能使用等級
	public int getSkillUse(String skillName){
		if(use_Map.containsKey(skillName)){
			return use_Map.get(skillName);
		}
		return 0;
	}

	//綁定
	//---------------------------------------------------------------------------------------------------//

	//獲取綁定技能名稱
	public String getBind(int key){
		int useKey = key-1;
		if(useKey >= 0 && useKey < 8){
			if(bind[useKey] != null){
				return bind[useKey];
			}
		}
		return "";
	}

	public boolean setBind(String skillName, int place){
		int usePlace = place-47;
		if(usePlace >= 0 && usePlace < 8){
			for(String b : bind){
				if(b != null && b.equals(skillName)){
					return false;
				}
			}
			bind[usePlace] = skillName;
		}
		return true;
	}

	//經驗
	//---------------------------------------------------------------------------------------------------//

	//增加經驗
	public void addExp(String expName, int add){
		if(exp_Map.containsKey(expName)){
			int nowExp = getExp(expName);
			int maxExp = ClassesManager.need_Exp_Map.get(expName+getLevel(expName));
			nowExp += add;
			if(nowExp < 0){
				nowExp = 0;
			}
			while (nowExp >= maxExp){
				//FancyClasses.fancyClasses.getLogger().info("前: "+nowExp+":"+maxExp);
				nowExp -= maxExp;

				addLevel(expName, 1);
				addPoint(expName, ClassConfig.getGivePoint(expName, (getLevel(expName)-1)));

				maxExp = ClassesManager.need_Exp_Map.get(expName+getLevel(expName));
				//FancyClasses.fancyClasses.getLogger().info("後: "+nowExp+":"+maxExp);

			}
			exp_Map.put(expName, nowExp);
		}
	}
	//獲取目前經驗
	public int getExp(String expName){
		if(exp_Map.containsKey(expName)){
			return exp_Map.get(expName);
		}
		return 0;
	}
	//獲取最高經驗
	public int getMaxExp(String expName){
		if(exp_Map.containsKey(expName)){
			return ClassesManager.need_Exp_Map.get(expName+getLevel(expName));
		}
		return 0;
	}

	//等級
	//---------------------------------------------------------------------------------------------------//

	//設置等級
	public void setLevel(String levelName, int set){
		if(level_Map.containsKey(levelName)){
			int maxLevel = getMaxLevel(levelName);
			if(set > 0 && set <= maxLevel){
				level_Map.put(levelName, set);
			}
		}
	}

	//增加等級
	public void addLevel(String levelName, int add){
		if(level_Map.containsKey(levelName)){
			int nowLevel = level_Map.get(levelName);
			int maxLevel = getMaxLevel(levelName);
			nowLevel++;
			if(nowLevel <= 0){
				nowLevel = 0;
			}
			if(nowLevel > maxLevel){
				nowLevel = maxLevel;
			}
			level_Map.put(levelName, nowLevel);
		}
	}
	//獲取最高等級
	public int getMaxLevel(String levelName){
		if(level_Map.containsKey(levelName)){
			if(ClassesManager.max_Level_Map.containsKey(levelName)){
				return ClassesManager.max_Level_Map.get(levelName);
			}
		}
		return 0;
	}
	//獲取目前等級
	public int getLevel(String levelName){
		if(level_Map.containsKey(levelName)){
			return level_Map.get(levelName);
		}
		return 0;
	}

	//---------------------------------------------------------------------------------------------------//
	//建立檔案 和 設置等級
	public void createConfig(){
		ClassConfig.createConfig(this, uuid.toString());
	}

	//儲存等級、經驗
	public void saveConfig(){
		File file = new File(FancyClasses.fancyClasses.getDataFolder(), "playerdata/"+uuid+".yml");
		FileConfiguration playerConfig = FileConfig.config_Map.get("playerdata/"+uuid+".yml");
		level_Map.forEach((level, integer) -> {
			playerConfig.set("level."+level+".level", integer);
		});
		exp_Map.forEach((exp, integer) -> {
			playerConfig.set("level."+exp+".exp", integer);
		});
		point_Map.forEach((point, integer) -> {
			if(point.endsWith("_max")){
				point = point.replace("_max", "");
				playerConfig.set("level."+point+".point_max", integer);
			}else {
				playerConfig.set("level."+point+".point", integer);
			}

		});

		skill_Map.forEach((skill, integer) -> {
			playerConfig.set("skill."+skill+".level", integer);
		});

		use_Map.forEach((use, integer) -> {
			playerConfig.set("skill."+use+".use", integer);
		});

		for(int i = 0 ; i < 8 ; i++){
			String bindName = bind[i];
			if(bindName != null){
				playerConfig.set("bind."+(i+1), bindName);
			}
		}
		try {
			playerConfig.save(file);
		}catch (IOException exception){
			//
		}
	}

}
