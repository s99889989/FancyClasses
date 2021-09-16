package com.daxton.fancyclasses.command;

import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.judgment.NumberJudgment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ExpCommand {

	public static void exp(String[] args){

		if(args.length == 5){
			if(args[0].equals("exp") && args[1].equals("give")){
				Player player = Bukkit.getPlayer(args[4]);
				if(player != null){
					UUID uuid = player.getUniqueId();
					PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
					if(NumberJudgment.isNumber(args[3])){
						playerClassData.addExp(args[2], Integer.parseInt(args[3]));
					}
				}

			}
		}

	}

}
