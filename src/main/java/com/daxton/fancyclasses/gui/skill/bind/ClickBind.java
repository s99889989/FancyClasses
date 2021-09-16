package com.daxton.fancyclasses.gui.skill.bind;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.gui.GUI;
import com.daxton.fancycore.api.gui.button.GuiAction;
import com.daxton.fancycore.api.gui.button.GuiButton;
import com.daxton.fancycore.api.gui.item.GuiItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.daxton.fancyclasses.config.FileConfig.languageConfig;

public class ClickBind implements GuiAction {

	final Player player;
	final GUI gui;

	public ClickBind(Player player, GUI gui){
		this.player = player;
		this.gui = gui;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		int place = slot+1;
		int number = slot-45;
		UUID uuid = player.getUniqueId();
		PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
		if(clickType == ClickType.LEFT){

			if(playerClassData.selectSkill == null){
				return;
			}
			if(playerClassData.setBind(playerClassData.selectSkill, place)){
				String[] strings = playerClassData.selectSkill.split("\\.");
				String skillType = strings[0];
				String skillID = strings[1];
				FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillType+".yml");
				ItemStack bindItem = GuiItem.valueOf(player, skillConfig, "Skills."+skillID);
				GuiButton skillTypeButton = GuiButton.ButtonBuilder.getInstance().
					setItemStack(bindItem).
					setGuiAction(this).
					build();
				gui.setButton(skillTypeButton, place);
			}




		}
		if(clickType == ClickType.RIGHT){
			ItemStack bindItem = GuiItem.valueOf(player, languageConfig, "Main.Skill.SkillBind."+number);
			GuiButton skillTypeButton = GuiButton.ButtonBuilder.getInstance().
				setItemStack(bindItem).
				setGuiAction(this).
				build();
			gui.setButton(skillTypeButton, place);
			playerClassData.setBind(null, place);
		}
	}

}
