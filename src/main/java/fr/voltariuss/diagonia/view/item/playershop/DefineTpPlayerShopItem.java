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

package fr.voltariuss.diagonia.view.item.playershop;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import fr.voltariuss.diagonia.controller.api.PlayerShopConfigController;
import fr.voltariuss.diagonia.model.entity.PlayerShop;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefineTpPlayerShopItem {

  private static final Material DEFINE_TP_ITEM_MATERIAL = Material.ENDER_PEARL;

  private final MiniMessage miniMessage;
  private final PlayerShopConfigController playerShopConfigController;
  private final ResourceBundle resourceBundle;

  @Inject
  public DefineTpPlayerShopItem(
      @NotNull MiniMessage miniMessage,
      @NotNull PlayerShopConfigController playerShopConfigController,
      @NotNull ResourceBundle resourceBundle) {
    this.miniMessage = miniMessage;
    this.playerShopConfigController = playerShopConfigController;
    this.resourceBundle = resourceBundle;
  }

  public @NotNull GuiItem createItem(@NotNull PlayerShop playerShop) {
    Component itemName = getName();
    List<Component> itemLore = getLore();
    return ItemBuilder.from(DEFINE_TP_ITEM_MATERIAL)
        .name(itemName)
        .lore(itemLore)
        .asGuiItem(onClick(playerShop));
  }

  private @NotNull GuiAction<InventoryClickEvent> onClick(@NotNull PlayerShop playerShop) {
    return event -> {
      Player player = (Player) event.getWhoClicked();
      playerShopConfigController.defineTeleportPoint(player, playerShop, player.getLocation());
    };
  }

  private @NotNull Component getName() {
    return miniMessage
        .deserialize(resourceBundle.getString("diagonia.playershop.config.teleportation.item.name"))
        .decoration(TextDecoration.ITALIC, false);
  }

  private @NotNull List<Component> getLore() {
    return Arrays.asList(
        miniMessage
            .deserialize(
                resourceBundle.getString(
                    "diagonia.playershop.config.teleportation.item.description.1"))
            .decoration(TextDecoration.ITALIC, false),
        miniMessage
            .deserialize(
                resourceBundle.getString(
                    "diagonia.playershop.config.teleportation.item.description.2"))
            .decoration(TextDecoration.ITALIC, false));
  }
}
