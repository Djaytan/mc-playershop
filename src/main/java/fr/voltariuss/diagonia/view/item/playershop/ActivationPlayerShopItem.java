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

import com.google.common.base.Preconditions;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import fr.voltariuss.diagonia.controller.api.PlayerShopConfigController;
import fr.voltariuss.diagonia.model.entity.PlayerShop;
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
