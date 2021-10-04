package com.daxton.fancyclasses.other;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.character.conversion.StringConversion;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ManaReg {

	public static BukkitRunnable bukkitRunnable;
	public static BukkitRunnable bukkitRunnable2;

	public static void execute(){
		FileConfiguration coreConfig = com.daxton.fancycore.config.FileConfig.config_Map.get("Other/CustomCore.yml");
		String manaRegString = coreConfig.getString("Mana_Regeneration.formula");
		int periodTime = coreConfig.getInt("Mana_Regeneration.period");
		if(bukkitRunnable != null && !bukkitRunnable.isCancelled()){
			bukkitRunnable.cancel();
		}
		if(bukkitRunnable2 != null && !bukkitRunnable2.isCancelled()){
			bukkitRunnable2.cancel();
		}
		bukkitRunnable = new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(player -> {
					UUID uuid = player.getUniqueId();
					PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
					if(playerClassData != null){
						if(playerClassData.nowMana < playerClassData.maxMana){
							double manaRegValue = StringConversion.getDouble(player, null, 0, manaRegString);
							playerClassData.nowMana += manaRegValue;
							if(playerClassData.nowMana > playerClassData.maxMana){
								playerClassData.nowMana = playerClassData.maxMana;
							}
						}
					}
				});
			}
		};
		bukkitRunnable.runTaskTimer(FancyClasses.fancyClasses, 0, periodTime * 20L);

		boolean healthBoolean = coreConfig.getBoolean("Health_Regeneration.enable");
		if(healthBoolean){
			int healthPeriodTime = coreConfig.getInt("Health_Regeneration.period");
			String healthRegString = coreConfig.getString("Health_Regeneration.formula");
			bukkitRunnable2 = new BukkitRunnable() {
				@Override
				public void run() {
					Bukkit.getOnlinePlayers().forEach(player -> {
						double manaRegValue = StringConversion.getDouble(player, null, 0, healthRegString);

						double giveHealth = player.getHealth()+manaRegValue;
						double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

						if(giveHealth > maxHealth){
							giveHealth = giveHealth - (giveHealth - maxHealth);
						}

						player.setHealth(giveHealth);

					});
				}
			};
			bukkitRunnable2.runTaskTimer(FancyClasses.fancyClasses, 0, healthPeriodTime * 20L);
		}

	}

}
