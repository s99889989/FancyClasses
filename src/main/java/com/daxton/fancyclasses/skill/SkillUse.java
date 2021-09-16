package com.daxton.fancyclasses.skill;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancycore.api.aims.entity.one.LookTarget;
import com.daxton.fancycore.other.taskaction.StringToMap;
import com.daxton.fancycore.task.TaskAction;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SkillUse {

	BossBar bossBar1;
	BossBar bossBar2;

	String[] strings1 = new String[]{"", "", "", "", "", "", "", "", ""};
	String[] strings2 = new String[]{"", "", "", "", "", "", "", "", ""};

	Player player;
	PlayerClassData playerClassData;
	FileConfiguration config;
	FileConfiguration skillConfig;
	String skillName;

	public SkillUse(Player player, PlayerClassData playerClassData){
		this.player = player;
		this.playerClassData = playerClassData;
		this.config = FileConfig.config_Map.get("config.yml");

	}


	//使用技能
	public void useSkill(int key, int oKey){
		int useKey =  getUseKey(key, oKey);
		String bindString = playerClassData.getBind(useKey);
		if(bindString.isEmpty()){
			return;
		}
		String[] strings = bindString.split("\\.");
		if(playerClassData.getSkillUse(strings[1]) <= 0){
			return;
		}
		skillConfig = FileConfig.config_Map.get("skill/"+strings[0]+".yml");
		skillName = strings[1];

		castTime();
	}

	//技能使用時間
	public void castTime(){
		int castTime = skillConfig.getInt("Skills."+skillName+".CastTime");
		int castDelay = skillConfig.getInt("Skills."+skillName+".CastDelay");
		castTime = castTime*20/10;
		if(playerClassData.castTime == null){
			playerClassData.castTime = new BukkitRunnable() {
				double costCount = -0.1;
				@Override
				public void run() {
					costCount += 0.1;
					bossBar2.setProgress(castDelay);
					if(costCount >= 1){

						bossBar2.setProgress(0);
						cancel();
						playerClassData.castTime = null;

//					if(castDelay > 0){
//						castDelay(castDelay);
//					}
					}

				}
			};
			playerClassData.castTime.runTaskTimer(FancyClasses.fancyClasses, 0, castTime);
		}



	}
	//技能使用完延遲時間
	public void castDelay(int castDelay){
		playerClassData.castDelay = new BukkitRunnable() {
			@Override
			public void run() {

			}
		};
		playerClassData.castDelay.runTaskTimer(FancyClasses.fancyClasses, 0, castDelay);
	}


	public static void cast(Player player, FileConfiguration skillConfig, String skillName){
		LivingEntity livingEntity = LookTarget.getLivingTarget(player, 10);
		List<String> actionString = skillConfig.getStringList("Skills."+skillName+".Action");
		StringToMap.toActiomMapList(actionString).forEach(stringStringMap -> {
			TaskAction.execute(player, livingEntity, stringStringMap, null, String.valueOf((int)(Math.random()*100000)));
		});
	}

	//綁案件轉為綁定順序
	public static int getUseKey(int key, int oKey){
		int useKey = 1;
		if(key > oKey){
			useKey = key;
		}
		if(key < oKey){
			useKey = key + 1;
		}
		return useKey;
	}

}
