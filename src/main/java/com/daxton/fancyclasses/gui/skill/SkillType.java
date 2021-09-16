package com.daxton.fancyclasses.gui.skill;

import com.daxton.fancyclasses.FancyClasses;
import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancyclasses.gui.skill.bind.ClickBind;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.daxton.fancyclasses.config.FileConfig.languageConfig;

public class SkillType implements GuiAction {

	final Player player;
	final GUI gui;

	public SkillType(Player player, GUI gui){
		this.player = player;
		this.gui = gui;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		if(clickType == ClickType.LEFT){
			UUID uuid = player.getUniqueId();
			Integer[] ignore = {18, 19, 27, 28, 36, 37};
			gui.clearButtonFrom(10, 54);
			FileConfiguration playerConfig = FileConfig.config_Map.get("playerdata/"+uuid+".yml");
			PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
			String classType = playerConfig.getString("Class_Type");

			FileConfiguration classConfig = FileConfig.config_Map.get("class/"+classType+".yml");
			List<String> skillList = classConfig.getStringList("Skills");
			for(String skillType : skillList){
				FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillType+".yml");

				ItemStack skillTypeItem = GuiItem.valueOf(player, languageConfig, "Main.Skill.Type");


				//材質
				String itemMaterial = skillConfig.getString("Material");
				if(itemMaterial != null){
					skillTypeItem = GuiEditItem.setMaterial(skillTypeItem, itemMaterial);
				}
				//CMD
				int cmd = skillConfig.getInt("CustomModelData");
				if(cmd > 0){
					GuiEditItem.setCustomModelData(skillTypeItem,cmd);
				}
				//名稱
				String displayName = skillType;
				if(skillConfig.getString("DisplayName") != null){
					displayName = skillConfig.getString("DisplayName");
				}
				Map<String, String> nameMap = new HashMap<>();
				nameMap.put("{name}", displayName);
				GuiEditItem.replaceName(skillTypeItem, nameMap);
				//需要點數
				String needPoint = skillConfig.getString("Level");
				if(needPoint == null){
					continue;
				}
				//目前點數/最高點數
				int newPoint = playerClassData.getPoint(needPoint);
				int maxPoint = playerClassData.getMaxPoint(needPoint);
				Map<String, String> loreMap = new HashMap<>();
				loreMap.put("{need}", needPoint);
				loreMap.put("{now}", String.valueOf(newPoint));
				loreMap.put("{max}", String.valueOf(maxPoint));
				GuiEditItem.replaceLore(skillTypeItem, loreMap);


				GuiButton skillTypeButton = GuiButton.ButtonBuilder.getInstance().
					setItemStack(skillTypeItem).
					setGuiAction(new SkillList(player, gui, skillType)).
					build();

				gui.addButton(skillTypeButton, 11, 17, ignore);
			}

			for(int i = 0 ; i < 8 ; i++){
				int key = i+1;

				ItemStack bindItem = GuiItem.valueOf(player, languageConfig, "Main.Skill.SkillBind."+key);
				if(playerClassData.getBind(key) != null && !playerClassData.getBind(key).isEmpty()){
					String[] strings = playerClassData.getBind(key).split("\\.");
					String skillType = strings[0];
					String skillID = strings[1];
					FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillType+".yml");
					bindItem = GuiItem.valueOf(player, skillConfig, "Skills."+skillID);
				}
				GuiButton skillTypeButton = GuiButton.ButtonBuilder.getInstance().
					setItemStack(bindItem).
					setGuiAction(new ClickBind(player, gui)).
					build();
				gui.addButton(skillTypeButton, 47, 54, ignore);
			}

		}
	}

}
