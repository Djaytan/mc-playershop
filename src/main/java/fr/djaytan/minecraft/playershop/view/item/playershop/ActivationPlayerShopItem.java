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

package fr.djaytan.minecraft.playershop.view.item.playershop;

import com.google.common.base.Preconditions;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import fr.djaytan.minecraft.playershop.model.entity.PlayerShop;
import fr.djaytan.minecraft.playershop.controller.api.PlayerShopConfigController;
import java.util.Collections;
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
public class ActivationPlayerShopItem {

  private static final Material ACTIVATED_PLAYER_SHOP_MATERIAL = Material.LIME_DYE;
  private static final Material DEACTIVATED_PLAYER_SHOP_MATERIAL = Material.GRAY_DYE;

  private final MiniMessage miniMessage;
  private final PlayerShopConfigController playerShopConfigController;
  private final ResourceBundle resourceBundle;

  @Inject
  public ActivationPlayerShopItem(
      @NotNull MiniMessage miniMessage,
      @NotNull PlayerShopConfigController playerShopConfigController,
      @NotNull ResourceBundle resourceBundle) {
    this.miniMessage = miniMessage;
    this.playerShopConfigController = playerShopConfigController;
    this.resourceBundle = resourceBundle;
  }

  public @NotNull GuiItem createItem(@NotNull PlayerShop playerShop) {
    Preconditions.checkNotNull(playerShop);

    Component itemName = getName(playerShop.isActive());
    List<Component> itemLore = getLore(playerShop.isActive());

    return ItemBuilder.from(
            playerShop.isActive()
                ? ACTIVATED_PLAYER_SHOP_MATERIAL
                : DEACTIVATED_PLAYER_SHOP_MATERIAL)
        .name(itemName)
        .lore(itemLore)
        .asGuiItem(onClick(playerShop));
  }

  private @NotNull GuiAction<InventoryClickEvent> onClick(@NotNull PlayerShop playerShop) {
    return event -> {
      Player player = (Player) event.getWhoClicked();
      playerShopConfigController.togglePlayerShop(player, playerShop);
    };
  }

  private @NotNull Component getName(boolean isPlayerShopActive) {
    return miniMessage
        .deserialize(
            resourceBundle.getString(
                isPlayerShopActive
                    ? "diagonia.playershop.config.activation.item.disabling.name"
                    : "diagonia.playershop.config.activation.item.enabling.name"))
        .decoration(TextDecoration.ITALIC, false);
  }

  private @NotNull List<Component> getLore(boolean isPlayerShopActive) {
    return Collections.singletonList(
        miniMessage
            .deserialize(
                resourceBundle.getString(
                    isPlayerShopActive
                        ? "diagonia.playershop.config.activation.item.disabling.description"
                        : "diagonia.playershop.config.activation.item.enabling.description"))
            .decoration(TextDecoration.ITALIC, false));
  }
}
