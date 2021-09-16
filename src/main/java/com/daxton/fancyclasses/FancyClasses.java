package com.daxton.fancyclasses;

import com.daxton.fancyclasses.command.MainCommand;
import com.daxton.fancyclasses.command.TabCommand;
import com.daxton.fancyclasses.listener.FancyMobListener;
import com.daxton.fancyclasses.listener.PlayerListener;
import com.daxton.fancyclasses.task.Start;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FancyClasses extends JavaPlugin {

    public static FancyClasses fancyClasses;

    @Override
    public void onEnable() {
        fancyClasses = this;
        //前置插件
        if(!DependPlugins.depend()){
            fancyClasses.setEnabled(false);
            return;
        }
        //指令
        Objects.requireNonNull(Bukkit.getPluginCommand("fancyclasses")).setExecutor(new MainCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("fancyclasses")).setTabCompleter(new TabCommand());
        //監聽
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), fancyClasses);
        Bukkit.getPluginManager().registerEvents(new FancyMobListener(), fancyClasses);
        //執行任務
        Start.execute();
    }

    @Override
    public void onDisable() {
        FancyClasses.fancyClasses.getLogger().info("§4FancyClasses uninstall.");
    }
}
