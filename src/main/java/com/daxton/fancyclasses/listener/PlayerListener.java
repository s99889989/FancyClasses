package com.daxton.fancyclasses.listener;


import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.manager.ClassesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler//當玩家登入
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ClassesManager.player_ClassData_Map.putIfAbsent(uuid, new PlayerClassData(player));

    }
    @EventHandler//當玩家登出
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
        playerClassData.saveConfig();
        ClassesManager.player_ClassData_Map.remove(uuid);

    }

    //當按下F鍵
    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);


        if(playerClassData.player_F){
            playerClassData.player_F = false;
            playerClassData.skillBar.close();
        }else {
            playerClassData.player_F = true;
            int mainKey = player.getInventory().getHeldItemSlot();
            playerClassData.skillBar.open(mainKey);
        }

    }


    //當按下切換1~9時
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
        int key = event.getNewSlot();
        int oldKey = event.getPreviousSlot();
        if(playerClassData.player_F){
            event.setCancelled(true);
            if(key != oldKey){
                playerClassData.skillBar.useSkill(key, oldKey);
            }
        }
    }

}
