package com.daxton.fancyclasses.skill;

import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.config.FileConfig;

import com.daxton.fancycore.api.character.conversion.StringTransform;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;


public class SkillBar extends SkillUse{


	public SkillBar(Player player, PlayerClassData playerClassData){
		super(player, playerClassData);
		setDefaultBar();
	}
	public void setDefaultBar(){
		BarColor barColor1 = StringTransform.getBarColor(config.getString("SkillBar.top.bar-color"), BarColor.WHITE);
		BarColor barColor2 = StringTransform.getBarColor(config.getString("SkillBar.down.bar-color"), BarColor.WHITE);
		BarStyle barStyle1 =  StringTransform.getBarStyle(config.getString("SkillBar.top.bar-style"), BarStyle.SOLID);
		BarStyle barStyle2 =  StringTransform.getBarStyle(config.getString("SkillBar.top.bar-style"), BarStyle.SOLID);
		bossBar1 = Bukkit.createBossBar("", barColor1, barStyle1);
		bossBar2 = Bukkit.createBossBar("",  barColor2, barStyle2);
	}
	//開啟
	public void open(int mainKey){
		String[] bindArray = playerClassData.bind;
		int bind = 0;
		String blank = config.getString("SkillBar.top.blank");
		String mainHand = config.getString("SkillBar.top.main-hand");
		String airDisplay = config.getString("SkillBar.top.air");
		for(int i = 0 ; i < 9 ; i++){
			if(i == mainKey){
				strings1[i] = mainHand;
			}else {
				String bindString = bindArray[bind];
				if(bindString != null){
					String[] strings = bindString.split("\\.");
					FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+strings[0]+".yml");
					String barDisplay = skillConfig.getString("Skills."+strings[1]+".BarDisplayName");

					if(barDisplay != null && playerClassData.getSkillUse(strings[1]) > 0){
						strings1[i] = barDisplay;
					}else {
						strings1[i] = airDisplay;
					}
				}else {
					strings1[i] = airDisplay;
				}
				bind++;
			}
		}
		bossBar1.setTitle(strings1[0]+blank+strings1[1]+blank+strings1[2]+blank+strings1[3]+blank+strings1[4]+blank+strings1[5]+blank+strings1[6]+blank+strings1[7]+blank+strings1[8]);
		bossBar1.addPlayer(player);
		bossBar2.addPlayer(player);
	}
	//關閉
	public void close(){
		bossBar1.removePlayer(player);
		bossBar2.removePlayer(player);
	}


}
