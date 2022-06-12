/*
 * Copyright (c) 2022 - Loïc DUBOIS-TERMOZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.voltariuss.diagonia.view.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import fr.voltariuss.diagonia.controller.api.PlayerShopController;
import fr.voltariuss.diagonia.model.entity.PlayerShop;
import fr.voltariuss.diagonia.view.item.playershop.ActivationPlayerShopItem;
import fr.voltariuss.diagonia.view.item.playershop.DefineTpPlayerShopItem;
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
