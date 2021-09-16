package com.daxton.fancyclasses.listener;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.api.mobdata.MobClassData;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancymobs.api.FancyMob;
import com.daxton.fancymobs.api.event.FancyMobDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Locale;
import java.util.UUID;

public class FancyMobListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)//當FancyMob的怪物死亡時
	public void onFancyMobDeath(FancyMobDeathEvent event){
		FancyMob fancyMob = event.getFancyMob();
		String mobID = fancyMob.getMobID();
		MobClassData mobClassData = ClassesManager.mob_ClassData_Map.get(mobID);
		if(mobClassData != null){
			Player player = event.getKiller();
			UUID uuid = player.getUniqueId();
			PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
			if(playerClassData != null){
				mobClassData.getExp_Map().forEach((s, integer) -> {
					//FancyClasses.fancyClasses.getLogger().info(mobID+" : "+s+" : "+integer);
					playerClassData.addExp(s, integer);
				});
			}

		}



	}

}
