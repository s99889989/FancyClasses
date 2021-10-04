package com.daxton.fancyclasses.gui.main;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancycore.api.gui.GUI;
import com.daxton.fancycore.api.gui.button.GuiAction;
import com.daxton.fancycore.api.gui.button.GuiButton;
import com.daxton.fancycore.api.gui.item.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

import static com.daxton.fancyclasses.config.FileConfig.languageConfig;

public class InfoShow implements GuiAction {

	final Player player;
	final GUI gui;

	public InfoShow(Player player, GUI gui){
		this.player = player;
		this.gui = gui;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		if(clickType == ClickType.LEFT){
			show();
		}
	}

	public void show(){
		Integer[] ignore = {18, 19, 27, 28, 36, 37};
		gui.clearButtonFrom(10, 54);
		languageConfig.getConfigurationSection("Main.PlayerInfo.Stats").getKeys(false).forEach(s -> {
			GuiButton playerInfoButton = GuiButton.ButtonBuilder.getInstance().
				setItemStack(GuiItem.valueOf(player, languageConfig, "Main.PlayerInfo.Stats."+s)).
				build();
			gui.addButton(playerInfoButton, 11, 44, ignore);
		});
	}

}
