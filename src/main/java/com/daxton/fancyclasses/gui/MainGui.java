package com.daxton.fancyclasses.gui;

import com.daxton.fancyclasses.api.dataplayer.PlayerClassData;
import com.daxton.fancyclasses.gui.attributes.AttrShow;
import com.daxton.fancyclasses.gui.main.InfoShow;
import com.daxton.fancyclasses.gui.skill.SkillType;
import com.daxton.fancyclasses.manager.ClassesManager;
import com.daxton.fancycore.api.gui.GUI;
import com.daxton.fancycore.api.gui.button.GuiButton;
import com.daxton.fancycore.api.gui.item.GuiItem;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.daxton.fancyclasses.config.FileConfig.languageConfig;

public class MainGui {

	public static void open(Player player){
		UUID uuid = player.getUniqueId();
		PlayerClassData playerClassData = ClassesManager.player_ClassData_Map.get(uuid);
		GUI gui = playerClassData.gui;

		if(gui == null){
			gui = GUI.GUIBuilder.getInstance().
				setPlayer(player).
				setSize(54).
				setTitle(languageConfig.getString("Main.Title")).
				build();
			InfoShow infoShow = new InfoShow(player, gui);
			infoShow.show();
			GuiButton playerInfoButton = GuiButton.ButtonBuilder.getInstance().
				setItemStack(GuiItem.valueOf(player, languageConfig, "Main.PlayerInfo.Main")).
				setGuiAction(infoShow).
				build();

			gui.setButton(playerInfoButton, 1, 1);

			GuiButton attributesButton = GuiButton.ButtonBuilder.getInstance().
				setItemStack(GuiItem.valueOf(player, languageConfig, "Main.Attributes.Main")).
				setGuiAction(new AttrShow(player, gui)).
				build();
			gui.setButton( attributesButton, 1, 3);

			GuiButton skillButton = GuiButton.ButtonBuilder.getInstance().
				setItemStack(GuiItem.valueOf(player, languageConfig, "Main.Skill.Main")).
				setGuiAction(new SkillType(player, gui)).
				build();
			gui.setButton(skillButton, 1, 5);

			GuiButton closeButton = GuiButton.ButtonBuilder.getInstance().
				setItemStack(GuiItem.valueOf(player, languageConfig, "Main.Close")).
				setGuiAction(new CloseGui(gui)).
				build();
			gui.setButton(closeButton, 1, 9);
		}

		gui.open(gui);



	}

}
