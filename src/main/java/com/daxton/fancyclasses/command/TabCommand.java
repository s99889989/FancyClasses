package com.daxton.fancyclasses.command;


import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancycore.api.config.SearchConfigMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TabCommand implements TabCompleter {

    private final String[] subCommands = {"reload", "gui", "exp"};

    final String[] number = {"10", "100", "1000", "10000", "100000"};

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args){
        List<String> commandList = new ArrayList<>();

        if (args.length == 1){
            commandList = Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }

        if (args.length == 2){
            if(args[0].equals("exp")){
                String[] expArray = {"give"};
                commandList = Arrays.stream(expArray).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
            }
        }

        if (args.length == 3){
            if(args[0].equals("exp") && args[1].equals("give")){
                commandList = SearchConfigMap.fileNameList(FileConfig.config_Map, "level/", true);
            }
        }

        if (args.length == 4){
            if(args[0].equals("exp") && args[1].equals("give")){
                commandList = Arrays.stream(number).collect(Collectors.toList());
            }
        }

        if (args.length == 5){
            if(args[0].equals("exp") && args[1].equals("give")){
                commandList = new ArrayList<>(Bukkit.getOnlinePlayers()).stream().map(HumanEntity::getName).collect(Collectors.toList());

            }
        }

        return commandList;
    }

}

