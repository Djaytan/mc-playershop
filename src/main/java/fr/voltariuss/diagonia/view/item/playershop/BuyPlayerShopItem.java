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
import fr.voltariuss.diagonia.controller.api.PlayerShopListController;
import fr.voltariuss.diagonia.model.config.data.PluginConfig;
import fr.voltariuss.diagonia.view.EconomyFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

@Singleton
public class BuyPlayerShopItem {

  private static final Material BUY_ITEM_MATERIAL = Material.EMERALD;

  private final EconomyFormatter economyFormatter;
  private final MiniMessage miniMessage;
  private final PlayerShopListController playerShopListController;
  private final PluginConfig pluginConfig;
  private final ResourceBundle resourceBundle;

  @Inject
  public BuyPlayerShopItem(
      @NotNull EconomyFormatter economyFormatter,
      @NotNull MiniMessage miniMessage,
      @NotNull PlayerShopListController playerShopListController,
      @NotNull PluginConfig pluginConfig,
      @NotNull ResourceBundle resourceBundle) {
    this.economyFormatter = economyFormatter;
    this.miniMessage = miniMessage;
    this.playerShopListController = playerShopListController;
    this.pluginConfig = pluginConfig;
    this.resourceBundle = resourceBundle;
  }

  public @NotNull GuiItem createItem() {
    Component itemName = getName();
    List<Component> itemLore = getLore();
    return ItemBuilder.from(BUY_ITEM_MATERIAL).name(itemName).lore(itemLore).asGuiItem(onClick());
  }

  private @NotNull GuiAction<InventoryClickEvent> onClick() {
    return event -> {
      Player player = (Player) event.getWhoClicked();
      playerShopListController.buyPlayerShop(player);
    };
  }

  private @NotNull Component getName() {
    return miniMessage
        .deserialize(resourceBundle.getString("diagonia.playershop.buy.item.name"))
        .decoration(TextDecoration.ITALIC, false);
  }

  private @NotNull List<Component> getLore() {
    return Arrays.asList(
        miniMessage
            .deserialize(
                resourceBundle.getString("diagonia.playershop.buy.item.description"),
                TagResolver.resolver(
                    Placeholder.unparsed(
                        "diag_buy_price",
                        economyFormatter.format(pluginConfig.getPlayerShop().getBuyCost()))))
            .decoration(TextDecoration.ITALIC, false),
        Component.empty(),
        miniMessage
            .deserialize(
                resourceBundle.getString("diagonia.playershop.buy.item.description.action"))
            .decoration(TextDecoration.ITALIC, false));
  }
}
