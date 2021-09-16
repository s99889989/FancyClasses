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

import java.util.*;

import static com.daxton.fancyclasses.config.FileConfig.languageConfig;

public class AttrShow implements GuiAction {

	final Player player;
	final GUI gui;

	public AttrShow(Player player, GUI gui){
		this.player = player;
		this.gui = gui;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		if(clickType == ClickType.LEFT){
			Integer[] ignore = {18, 19, 27, 28, 36, 37};
			gui.clearButtonFrom(10, 54);
			FileConfiguration attrConfig = FileConfig.config_Map.get("character-attributes.yml");

			UUID uuid = player.getUniqueId();
			PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);

			attrConfig.getConfigurationSection("Point").getKeys(false).forEach(key -> {
				ItemStack itemStack = GuiItem.valueOf(player, languageConfig, "Main.Attributes.ChangeAttr");

				String display_name = attrConfig.getString("Point."+key+".display-name");
				Map<String, String> nameMap = new HashMap<>();
				nameMap.put("{name}", display_name);
				GuiEditItem.replaceName(itemStack, nameMap);

				Map<String, String> loreMap = new HashMap<>();
				loreMap.put("{now}", playerClassData.getAttr(key)+"");
				loreMap.put("{max}", playerClassData.getMaxAttr(key)+""); //
				GuiEditItem.replaceLore(itemStack, loreMap);

				GuiButton attributesButton = GuiButton.ButtonBuilder.getInstance().
					setItemStack(itemStack).
					setGuiAction(new ChangeAttr(player, gui, key)).
					build();

				gui.addButton(attributesButton, 11, 44, ignore);

			});

		}
	}

}
