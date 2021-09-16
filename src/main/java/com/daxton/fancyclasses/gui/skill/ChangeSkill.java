package com.daxton.fancyclasses.gui.skill;

import com.daxton.fancyclasses.FancyClasses;
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

public class ChangeSkill implements GuiAction {

	final Player player;
	final GUI gui;
	final String skillType;
	final String skillID;
	final int typePlace;

	public ChangeSkill(Player player, GUI gui, String skillType, String skillID, int typePlace){
		this.player = player;
		this.gui = gui;
		this.skillType = skillType;
		this.skillID = skillID;
		this.typePlace = typePlace;
	}

	public void execute(ClickType clickType, InventoryAction action, int slot){
		UUID uuid = player.getUniqueId();
		PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
		FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillType+".yml");
		String pointName = skillConfig.getString("Level");

		int place = slot+1;
		if(clickType == ClickType.LEFT){
			playerClassData.addUseSkill(skillID, 1);
			changeSkillDisplay(playerClassData, place);
		}
		if(clickType == ClickType.RIGHT){
			playerClassData.addUseSkill(skillID, -1);
			changeSkillDisplay(playerClassData, place);
		}
		//升級技能
		if(clickType == ClickType.SHIFT_LEFT){
			playerClassData.addSkill(skillID, pointName, 1, true);
			changeSkillTypeDisplay(playerClassData);
			changeSkillDisplay(playerClassData, place);
		}
		//降級技能
		if(clickType == ClickType.SHIFT_RIGHT){
			playerClassData.addSkill(skillID, pointName, -1, true);
			changeSkillTypeDisplay(playerClassData);
			changeSkillDisplay(playerClassData, place);
		}
		//選擇技能
		if(clickType == ClickType.MIDDLE){
			playerClassData.selectSkill = skillType+"."+skillID;
		}
	}

	public void changeSkillTypeDisplay(PlayerClassData playerClassData){

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
			return;
		}
		//目前點數/最高點數
		int nowPoint = playerClassData.getPoint(needPoint);
		int maxPoint = playerClassData.getMaxPoint(needPoint);
		Map<String, String> loreMap = new HashMap<>();
		loreMap.put("{need}", needPoint);
		loreMap.put("{now}", String.valueOf(nowPoint));
		loreMap.put("{max}", String.valueOf(maxPoint));
		GuiEditItem.replaceLore(skillTypeItem, loreMap);

		GuiButton skillTypeButton = GuiButton.ButtonBuilder.getInstance().
			setItemStack(skillTypeItem).
			setGuiAction(new SkillList(player, gui, skillType)).
			build();

		gui.setButton(skillTypeButton, typePlace);
	}

	public void changeSkillDisplay(PlayerClassData playerClassData, int place){
		FileConfiguration skillConfig = FileConfig.config_Map.get("skill/"+skillType+".yml");

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
			setGuiAction(this).
			build();

		gui.setButton(skillButton, place);
	}

}
