package com.daxton.fancyclasses.command;



import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.gui.MainGui;
import com.daxton.fancyclasses.task.Reload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.daxton.fancyclasses.config.FileConfig.languageConfig;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args){

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("gui")){
                    MainGui.open(player);
                }
            }

        }

        if(sender instanceof Player && !sender.isOp()){
            return true;
        }
        //轉職指令
        if(args.length > 1) {
            if(args[0].equalsIgnoreCase("class")){
                ClassCommand.execute(args);
            }
        }
        //經驗指令
        if(sender instanceof Player){
            if(args.length > 1) {
                if(args[0].equalsIgnoreCase("give")){
                    ExpCommand.exp(args);
                }
            }

        }
        //重新讀取設定
        if(args.length == 1) {


            if(args[0].equalsIgnoreCase("reload")){
                //重新讀取的一些程序
                Reload.execute();

                if(sender instanceof Player){
                    Player player = (Player) sender;
                    player.sendMessage(languageConfig.getString("OpMessage.Reload")+"");
                }
                FancyClasses.fancyClasses.getLogger().info(languageConfig.getString("LogMessage.Reload"));
            }

        }

        return true;
    }

}
