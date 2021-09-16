package com.daxton.fancyclasses.other;

import com.daxton.fancyclasses.FancyClasses;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;


public class CreateFile {

	public static void execute(){

        //FileConfiguration bb = FileConfig.config_Map.get("bbb.yml");

        File file = new File(FancyClasses.fancyClasses.getDataFolder(), "/character-attributes.yml");
        try {
            file.createNewFile();
        }catch (IOException exception){
            //
        }
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        int j = 2;
        for(int i = 0 ; i <= 99 ; i++){
            int k = i+1;

            int p = 1;

            int s = i/10+2;
//            if(bb.getInt((i)+"") != 0){
//                int o = bb.getInt((i)+"");
//                int n = bb.getInt((i+1)+"");
//                p = n - o;
//            }
            //fileConfiguration.set(i+"", 1);
            fileConfiguration.set("cost."+i, s);
            //fileConfiguration.set("level."+i+".points", p);
        }
        try {
            fileConfiguration.save(file);
        }catch (IOException exception){
            //
        }
	}

}
