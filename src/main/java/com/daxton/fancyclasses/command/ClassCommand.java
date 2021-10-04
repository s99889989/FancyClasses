package com.daxton.fancyclasses.command;

import com.daxton.fancyclasses.api.dataplayer.CommandClass;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancycore.api.config.SearchConfigMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ClassCommand {

	public static void execute(String[] args){
		if(args.length == 4){
			Player player = Bukkit.getPlayer(args[3]);
			List<String> classList = SearchConfigMap.fileNameList(FileConfig.config_Map, "class/", true);
			if(player != null && classList.contains(args[2])){
				if(args[1].equalsIgnoreCase("change")){
					CommandClass.change(player, args[2]);
				}
				if(args[1].equalsIgnoreCase("rebirth")){
					CommandClass.rebirth(player, args[2]);
				}

			}
		}
	}

}
