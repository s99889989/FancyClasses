package com.daxton.fancyclasses.gui.skill;

import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.config.FileConfig;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.character.conversion.StringConversion;
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
				//使用技能等級/目前技能等級/最高技能等級/被動技能/吟唱時間/技能延遲/冷却時間/消耗SP
				Map<String, String> loreMap = new HashMap<>();
				int useSkill = playerClassData.getSkillUse(skillID);
				int newSkill = playerClassData.getSkillLevel(skillID);
				int maxSkill = playerClassData.getMaxSkillLevel(skillID);
				boolean passiveSkill = skillConfig.getBoolean("Skills."+skillID+".PassiveSkill");
				double distance = StringConversion.getDouble(player, null, 0, skillConfig.getString("Skills."+skillID+".TargetDistance"));
				int castTime = StringConversion.getInt(player, null, 0, skillConfig.getString("Skills."+skillID+".CastTime"));
				int castDelay = StringConversion.getInt(player, null, 0, skillConfig.getString("Skills."+skillID+".CastDelay"));
				int coolDown = StringConversion.getInt(player, null, 0, skillConfig.getString("Skills."+skillID+".CoolDown"));
				double mana = StringConversion.getDouble(player, null, 0, skillConfig.getString("Skills."+skillID+".Mana"));
				loreMap.put("{use}", String.valueOf(useSkill));
				loreMap.put("{now}", String.valueOf(newSkill));
				loreMap.put("{max}", String.valueOf(maxSkill));
				loreMap.put("{passive}", String.valueOf(passiveSkill));
				loreMap.put("{distance}", String.valueOf(distance));
				loreMap.put("{cast_time}", String.valueOf(castTime));
				loreMap.put("{cast_delay}", String.valueOf(castDelay));
				loreMap.put("{cool_down}", String.valueOf(coolDown));
				loreMap.put("{mana}", String.valueOf(mana));
				GuiEditItem.replaceLore(skillItem, loreMap);
				//Lore
				List<String> skillLore = skillConfig.getStringList("Skills."+skillID+".Lore");
				GuiEditItem.loreInsert(player, skillItem, "{lore}", skillLore);

				GuiButton skillButton = GuiButton.ButtonBuilder.getInstance().
					setItemStack(skillItem).
					setGuiAction(new ChangeSkill(player, gui, skillType, skillID, place)).
					build();

				gui.addButton(skillButton, 20, 44, ignore);

			});

		}

	}

}
