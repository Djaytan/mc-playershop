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

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import fr.djaytan.minecraft.playershop.model.config.data.PluginConfig;
import fr.djaytan.minecraft.playershop.view.EconomyFormatter;
import fr.djaytan.minecraft.playershop.controller.api.PlayerShopListController;
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
