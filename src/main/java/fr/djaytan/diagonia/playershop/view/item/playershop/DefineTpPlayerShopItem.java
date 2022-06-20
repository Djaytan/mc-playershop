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

package fr.djaytan.diagonia.playershop.view.item.playershop;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopConfigController;
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
