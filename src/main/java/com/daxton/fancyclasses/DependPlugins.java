package com.daxton.fancyclasses;

import com.daxton.fancyclasses.config.FileConfig;
import org.bukkit.Bukkit;
import static com.daxton.fancyclasses.config.FileConfig.languageConfig;

public class DependPlugins {

    public static boolean depend(){

        FancyClasses fancyClasses = FancyClasses.fancyClasses;

        if (Bukkit.getServer().getPluginManager().getPlugin("FancyCore") != null && Bukkit.getPluginManager().isPluginEnabled("FancyCore")){
            //設定檔
            FileConfig.execute();
            fancyClasses.getLogger().info(languageConfig.getString("LogMessage.LoadFancyCore"));
        }else {
            fancyClasses.getLogger().info("*** FancyCore is not installed or not enabled. ***");
            fancyClasses.getLogger().info("*** FancyClasses will be disabled. ***");
            return false;
        }

        if (Bukkit.getServer().getPluginManager().getPlugin("FancyMobs") != null && Bukkit.getPluginManager().isPluginEnabled("FancyMobs")){
            fancyClasses.getLogger().info(languageConfig.getString("LogMessage.LoadFancyMobs"));
        }else {
            languageConfig.getStringList("LogMessage.UnLoadFancyMobs").forEach(info->{
                fancyClasses.getLogger().info(info);
            });
            return false;
        }

        if (Bukkit.getServer().getPluginManager().getPlugin("FancyAction") != null && Bukkit.getPluginManager().isPluginEnabled("FancyAction")){
            fancyClasses.getLogger().info(languageConfig.getString("LogMessage.LoadFancyAction"));
        }else {
            languageConfig.getStringList("LogMessage.UnLoadFancyAction").forEach(info->{
                fancyClasses.getLogger().info(info);
            });
            return false;
        }

        return true;
    }

}
