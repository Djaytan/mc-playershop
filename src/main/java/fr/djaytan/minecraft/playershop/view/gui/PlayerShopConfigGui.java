/*
 * PlayerShop plugin for Minecraft (Bukkit servers)
 * Copyright (C) 2022 - Loïc DUBOIS-TERMOZ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.djaytan.minecraft.playershop.view.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import fr.djaytan.minecraft.playershop.model.entity.PlayerShop;
import fr.djaytan.minecraft.playershop.view.item.playershop.ActivationPlayerShopItem;
import fr.djaytan.minecraft.playershop.view.item.playershop.DefineTpPlayerShopItem;
import fr.djaytan.minecraft.playershop.controller.api.PlayerShopController;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerShopConfigGui {

  private static final Material PREVIOUS_GUI_MATERIAL = Material.ARROW;

  private final ActivationPlayerShopItem activationPlayerShopItem;
  private final DefineTpPlayerShopItem defineTpPlayerShopItem;
  private final MiniMessage miniMessage;
  private final PlayerShopController playerShopController;
  private final ResourceBundle resourceBundle;

  @Inject
  public PlayerShopConfigGui(
      @NotNull ActivationPlayerShopItem activationPlayerShopItem,
      @NotNull DefineTpPlayerShopItem defineTpPlayerShopItem,
      @NotNull MiniMessage miniMessage,
      @NotNull PlayerShopController playerShopController,
      @NotNull ResourceBundle resourceBundle) {
    this.activationPlayerShopItem = activationPlayerShopItem;
    this.defineTpPlayerShopItem = defineTpPlayerShopItem;
    this.playerShopController = playerShopController;
    this.miniMessage = miniMessage;
    this.resourceBundle = resourceBundle;
  }

  public void open(@NotNull Player whoOpen, @NotNull PlayerShop playerShop) {
    Gui gui =
        Gui.gui()
            .rows(3)
            .title(
                miniMessage.deserialize(
                    resourceBundle.getString("diagonia.playershop.config.gui.title")))
            .create();

    gui.setItem(2, 4, activationPlayerShopItem.createItem(playerShop));
    gui.setItem(2, 6, defineTpPlayerShopItem.createItem(playerShop));

    gui.setItem(
        3,
        1,
        ItemBuilder.from(PREVIOUS_GUI_MATERIAL)
            .name(
                miniMessage
                    .deserialize(resourceBundle.getString("diagonia.gui.go_to_previous_menu"))
                    .decoration(TextDecoration.ITALIC, false))
            .asGuiItem(
                event ->
                    playerShopController.openPlayerShopListGui((Player) event.getWhoClicked())));

    gui.setDefaultClickAction(event -> event.setCancelled(true));

    gui.open(whoOpen);
  }
}
