package com.daxton.fancyclasses.gui.skill;

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

public class SkillList implements GuiAction {

	final Player player;
	final GUI gui;
	final String skillType;

	public SkillList(Player player, GUI gui, String skillType){
		this.player = player;
		this.gui = gui;
		this.skillType = skillType;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){

		int place = slot+1;

		if(clickType == ClickType.LEFT){
			UUID uuid = player.getUniqueId();
			FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillType+".yml");
			PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
			Integer[] ignore = {18, 19, 27, 28, 36, 37};
			gui.clearButtonFrom(20, 44);


			skillConfig.getConfigurationSection("Skills").getKeys(false).forEach(skillID->{

				ItemStack skillItem = GuiItem.valueOf(player, languageConfig, "Main.Skill.Skill");
				//材質
				String itemMaterial = skillConfig.getString("Skills."+skillID+".Material");
				if(itemMaterial != null){
					skillItem = GuiEditItem.setMaterial(skillItem, itemMaterial);
				}
				//CMD
				int cmd = skillConfig.getInt("Skills."+skillID+".CustomModelData");
				if(cmd > 0){
					GuiEditItem.setCustomModelData(skillItem, cmd);
				}
				//名稱
				String skillName = skillConfig.getString("Skills."+skillID+".DisplayName");
				Map<String, String> nameMap = new HashMap<>();
				nameMap.put("{name}", skillName);
				GuiEditItem.replaceName(skillItem, nameMap);
				//使用技能等級/目前技能等級/最高技能等級
				int useSkill = playerClassData.getSkillUse(skillID);
				int newSkill = playerClassData.getSkillLevel(skillID);
				int maxSkill = playerClassData.getMaxSkillLevel(skillID);
				Map<String, String> loreMap = new HashMap<>();
				loreMap.put("{use}", String.valueOf(useSkill));
				loreMap.put("{now}", String.valueOf(newSkill));
				loreMap.put("{max}", String.valueOf(maxSkill));
				GuiEditItem.replaceLore(skillItem, loreMap);

				GuiButton skillButton = GuiButton.ButtonBuilder.getInstance().
					setItemStack(skillItem).
					setGuiAction(new ChangeSkill(player, gui, skillType, skillID, place)).
					build();

				gui.addButton(skillButton, 20, 44, ignore);

			});

		}

	}

}
