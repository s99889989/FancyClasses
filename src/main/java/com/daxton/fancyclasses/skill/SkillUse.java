package com.daxton.fancyclasses.skill;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancycore.api.aims.entity.one.LookTarget;
import com.daxton.fancycore.api.aims.location.one.Look;
import com.daxton.fancycore.api.character.conversion.StringConversion;
import com.daxton.fancycore.api.gui.item.GuiItem;
import com.daxton.fancycore.other.task.guise.GuiseEntity;
import com.daxton.fancycore.other.taskaction.StringToMap;
import com.daxton.fancycore.task.TaskAction;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
		int useKey = getUseKey(key, oKey);
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
		if(useMana(strings[1])){
			castTime(key, strings[1]);
		}

	}
	//使用魔力
	public boolean useMana(String skillName){
		double useMana = StringConversion.getDouble(player, null, 0, skillConfig.getString("Skills."+skillName+".Mana"));
		if(useMana <= playerClassData.nowMana && playerClassData.castTime == null){
			playerClassData.nowMana -= useMana;
			return true;
		}
		return false;
	}

	//CD
	public boolean coolDown(int key, String skillName){
		if(playerClassData.castDown.containsKey(skillName)){
			return false;
		}
		int coolDownTime = StringConversion.getInt(player, null, 0, skillConfig.getString("Skills."+skillName+".CoolDown"));
		int p = key+1;
		playerClassData.castDownTime.put(skillName, 10);
		String s10 = config.getString("SkillBar.down.cd.10");
		setBossBar2(p, s10);
		BukkitRunnable bukkitRunnable = new BukkitRunnable() {
			int tickCount = 10;
			@Override
			public void run() {
				tickCount -= 1;
				String s9 = config.getString("SkillBar.down.cd."+tickCount);
				setBossBar2(p, s9);
				if(tickCount <= 0){
					cancel();
					playerClassData.castDown.remove(skillName);
					playerClassData.castDownTime.remove(skillName);
					//FancyClasses.fancyClasses.getLogger().info("刪除: "+skillName);
					return;
				}
				playerClassData.castDownTime.put(skillName, tickCount);
			}
		};
		bukkitRunnable.runTaskTimer(FancyClasses.fancyClasses, 0, 2L * coolDownTime);
		playerClassData.castDown.put(skillName, bukkitRunnable);
		return true;
	}
	//CD2
	public void coolDown2(int p, FileConfiguration skillConfig, String skillName){

		if(playerClassData.castDown.containsKey(skillName)){
			int coolDownTime = StringConversion.getInt(player, null, 0, skillConfig.getString("Skills."+skillName+".CoolDown"));
			playerClassData.castDown.get(skillName).cancel();
			playerClassData.castDown.remove(skillName);

			BukkitRunnable bukkitRunnable = new BukkitRunnable() {
				int tickCount = playerClassData.castDownTime.get(skillName)+1;
				@Override
				public void run() {
					tickCount -= 1;
					if(tickCount < 0){
						cancel();
						playerClassData.castDown.remove(skillName);
						playerClassData.castDownTime.remove(skillName);
						return;
					}
					playerClassData.castDownTime.put(skillName, tickCount);
					String s9 = config.getString("SkillBar.down.cd."+tickCount);
					setBossBar2(p, s9);
				}
			};
			bukkitRunnable.runTaskTimer(FancyClasses.fancyClasses, 0, 2L * coolDownTime);
			playerClassData.castDown.put(skillName, bukkitRunnable);
		}
	}

	//設置BossBar下
	public void setBossBar2(int place, String content){
		place = place - 1;
		strings2[place] = content;
		String blank = config.getString("SkillBar.down.blank");
		bossBar2.setTitle(strings2[0]+blank+strings2[1]+blank+strings2[2]+blank+strings2[3]+blank+strings2[4]+blank+strings2[5]+blank+strings2[6]+blank+strings2[7]+blank+strings2[8]);
	}

	//技能使用時間
	public boolean castTime(int key, String skillName){

		if(playerClassData.castTime == null){

			double targetDistance = StringConversion.getDouble(player, null, 0, skillConfig.getString("Skills."+skillName+".TargetDistance"));
			boolean soundEnable = config.getBoolean("SkillAim.sound.enable");
			String sound = config.getString("SkillAim.sound.sound");
			boolean hologramEnable = config.getBoolean("SkillAim.hologram.enable");


			int castTime = StringConversion.getInt(player, null, 0, skillConfig.getString("Skills."+skillName+".CastTime"));
			int castDelay = StringConversion.getInt(player, null, 0, skillConfig.getString("Skills."+skillName+".CastDelay"));


			if(castTime > 0){
				if(soundEnable){
					player.getLocation().getWorld().playSound(player.getLocation(), sound, SoundCategory.PLAYERS, 1, 1);
				}
				if(hologramEnable){
					hd(targetDistance, castTime);
				}
				playerClassData.castTime = new BukkitRunnable() {

					double costCount = -0.1;
					@Override
					public void run() {
						costCount += 0.1;

						bossBar2.setProgress(costCount);

						if(costCount >= 0.9999999999999999){
							cancel();
							if(coolDown(key, skillName)){
								cast(player);
							}

						if(castDelay > 0){
							castDelay(castDelay);
						}else {
							bossBar2.setProgress(0);
							playerClassData.castTime = null;
						}

						}

					}
				};
				playerClassData.castTime.runTaskTimer(FancyClasses.fancyClasses, 0, 2L * castTime);
			}else {
				if(castDelay > 0){
					if(coolDown(key, skillName) && castDelay(castDelay)){
						cast(player);
					}
				}else {
					if(coolDown(key, skillName)){
						cast(player);
					}
				}

			}

			return true;
		}

		return false;

	}

	public void hd(double targetDistance, int castTime){
		double height = config.getDouble("SkillAim.hologram.height");
			ItemStack itemStack = GuiItem.valueOf(player, config, "SkillAim.hologram.item");
			Location location = Look.getLook(player, targetDistance).add(0,height,0);
			GuiseEntity guiseEntity = new GuiseEntity(location, "DROPPED_ITEM", itemStack, false, false, false);

			new BukkitRunnable() {
				double costCount = 0;
				final int endTime = castTime * 20;
				@Override
				public void run() {

					costCount += 2;

					LivingEntity livingEntity = LookTarget.getLivingTarget(player, targetDistance);
					if(livingEntity != null){
						Location location = livingEntity.getLocation().add(0,height,0);
						guiseEntity.teleport(location, false, false);
					}else {
						Location location = Look.getLook(player, targetDistance).add(0,height,0);
						guiseEntity.teleport(location, false, false);
					}
					if(costCount >= endTime){
						guiseEntity.delete();
					}

				}
			}.runTaskTimer(FancyClasses.fancyClasses, 0, 2);

	}

	//技能使用完延遲時間
	public boolean castDelay(int castDelay){

		if(playerClassData.castDelay == null){
			playerClassData.castDelay = new BukkitRunnable() {
				double costCount = 1.1;
				@Override
				public void run() {
					costCount -= 0.1;

					bossBar2.setProgress(costCount);

					if(costCount <= 0.01){
						cancel();
						playerClassData.castDelay = null;
						playerClassData.castTime = null;
					}

				}
			};
			playerClassData.castDelay.runTaskTimer(FancyClasses.fancyClasses, 0, 2L * castDelay);
			return true;
		}

		return false;
	}


	public void cast(Player player){

		boolean needTarget = skillConfig.getBoolean("Skills."+skillName+".NeedTarget");

		double targetDistance = StringConversion.getDouble(player, null, 0, skillConfig.getString("Skills."+skillName+".TargetDistance"));

		LivingEntity livingEntity = LookTarget.getLivingTarget(player, targetDistance);

		if(needTarget){
			if(livingEntity == null){
				return;
			}
		}

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
