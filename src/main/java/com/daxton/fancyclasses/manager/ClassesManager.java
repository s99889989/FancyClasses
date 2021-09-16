package com.daxton.fancyclasses.manager;

import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.api.mobdata.MobClassData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClassesManager {
	//玩家資訊
	public static Map<UUID, PlayerClassData> player_ClassData_Map = new HashMap<>();
	//魔物資訊
	public static Map<String, MobClassData> mob_ClassData_Map = new HashMap<>();

	//最高等級
	public static Map<String, Integer> max_Level_Map = new HashMap<>();
	//當下等級需要的經驗
	public static Map<String, Integer> need_Exp_Map = new HashMap<>();
	//技能基礎最高等級
	public static Map<String, Integer> max_Skill_Map = new HashMap<>();
	//技能設定檔的位置
	public static Map<String, String> skill_Config_Map = new HashMap<>();


}
