package com.daxton.fancyclasses.gui.attributes;

import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.gui.GUI;
import com.daxton.fancycore.api.gui.button.GuiAction;
import com.daxton.fancycore.api.gui.button.GuiButton;
import com.daxton.fancycore.api.gui.item.GuiEditItem;
import com.daxton.fancycore.api.gui.item.GuiItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.daxton.fancyclasses.config.FileConfig.languageConfig;

public class ChangeAttr implements GuiAction {

	final Player player;
	final GUI gui;
	final String attrName;

	public ChangeAttr(Player player, GUI gui, String attrName){
		this.player = player;
		this.gui = gui;
		this.attrName = attrName;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		UUID uuid = player.getUniqueId();
		PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
		FileConfiguration attrConfig = FileConfig.config_Map.get("character-attributes.yml");
		int place = slot+1;
		if(clickType == ClickType.LEFT){

			playerClassData.usePoint("base", playerClassData.addAttr(attrName, 1, true)*-1);

			changeDisplay(playerClassData, attrConfig, place);

		}
		if(clickType == ClickType.RIGHT){
			playerClassData.usePoint("base", playerClassData.addAttr(attrName, -1, true)*-1);

			changeDisplay(playerClassData, attrConfig, place);
		}
	}

	public void changeDisplay(PlayerClassData playerClassData, FileConfiguration attrConfig, int place){
		ItemStack itemStack = GuiItem.valueOf(player, languageConfig, "Main.Attributes.ChangeAttr");

		String display_name = attrConfig.getString("Point."+attrName+".display-name");
		Map<String, String> nameMap = new HashMap<>();
		nameMap.put("{name}", display_name);
		GuiEditItem.replaceName(itemStack, nameMap);

		Map<String, String> loreMap = new HashMap<>();
		loreMap.put("{now}", playerClassData.getAttr(attrName)+"");
		loreMap.put("{max}", playerClassData.getMaxAttr(attrName)+""); //
		GuiEditItem.replaceLore(itemStack, loreMap);

		GuiButton attributesButton = GuiButton.ButtonBuilder.getInstance().
			setItemStack(itemStack).
			setGuiAction(this).
			build();

		gui.setButton(attributesButton, place);

		GuiButton attributesMainButton = GuiButton.ButtonBuilder.getInstance().
			setItemStack(GuiItem.valueOf(player, languageConfig, "Main.Attributes.Main")).
			setGuiAction(new AttrShow(player, gui)).
			build();
		gui.setButton( attributesMainButton, 1, 3);
	}

}
