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
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import fr.djaytan.minecraft.playershop.model.entity.PlayerShop;
import fr.djaytan.minecraft.playershop.view.item.GoToMainMenuItem;
import fr.djaytan.minecraft.playershop.view.item.PaginatedItem;
import fr.djaytan.minecraft.playershop.view.item.playershop.BuyPlayerShopItem;
import fr.djaytan.minecraft.playershop.view.item.playershop.ConfigPlayerShopItem;
import fr.djaytan.minecraft.playershop.view.item.playershop.ConsultPlayerShopItem;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerShopListGui {

  private static final int NB_COLUMNS_PER_LINE = 9;
  private static final int NB_ROW_PER_PAGE = 4;
  private static final Material DECORATION_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;

  private final BuyPlayerShopItem buyPlayerShopItem;
  private final ConfigPlayerShopItem configPlayerShopItem;
  private final ConsultPlayerShopItem consultPlayerShopItem;
  private final GoToMainMenuItem goToMainMenuItem;
  private final MiniMessage miniMessage;
  private final PaginatedItem paginatedItem;
  private final ResourceBundle resourceBundle;
  private final Server server;

  @Inject
  public PlayerShopListGui(
      @NotNull BuyPlayerShopItem buyPlayerShopItem,
      @NotNull ConfigPlayerShopItem configPlayerShopItem,
      @NotNull ConsultPlayerShopItem consultPlayerShopItem,
      @NotNull GoToMainMenuItem goToMainMenuItem,
      @NotNull MiniMessage miniMessage,
      @NotNull PaginatedItem paginatedItem,
      @NotNull ResourceBundle resourceBundle,
      @NotNull Server server) {
    this.buyPlayerShopItem = buyPlayerShopItem;
    this.configPlayerShopItem = configPlayerShopItem;
    this.consultPlayerShopItem = consultPlayerShopItem;
    this.goToMainMenuItem = goToMainMenuItem;
    this.miniMessage = miniMessage;
    this.paginatedItem = paginatedItem;
    this.resourceBundle = resourceBundle;
    this.server = server;
  }

  public void open(
      @NotNull Player whoOpen, @NotNull List<PlayerShop> playerShopList, boolean hasPlayerShop) {
    int pageSize = NB_ROW_PER_PAGE * NB_COLUMNS_PER_LINE;
    PaginatedGui gui =
        Gui.paginated()
            .title(
                miniMessage.deserialize(
                    resourceBundle.getString("diagonia.playershop.list.gui.title")))
            .rows(6)
            .pageSize(pageSize)
            .create();

    GuiItem decorationItem =
        ItemBuilder.from(DECORATION_MATERIAL).name(Component.empty()).asGuiItem();

    for (int i = 1; i <= NB_COLUMNS_PER_LINE; i++) {
      gui.setItem(5, i, decorationItem);
    }

    gui.addItem(
        playerShopList.stream()
            .map(
                playerShop -> {
                  OfflinePlayer ownerPlayer = server.getOfflinePlayer(playerShop.getOwnerUuid());
                  return consultPlayerShopItem.createItem(ownerPlayer, playerShop);
                })
            .filter(Objects::nonNull)
            .toArray(GuiItem[]::new));

    GuiItem configItem;
    if (!hasPlayerShop) {
      configItem = buyPlayerShopItem.createItem();
    } else {
      configItem = configPlayerShopItem.createItem();
    }
    gui.setItem(6, 5, configItem);

    if (playerShopList.size() > pageSize) {
      gui.setItem(5, 3, paginatedItem.createPreviousPageItem(gui));
      gui.setItem(5, 7, paginatedItem.createNextPageItem(gui));
    }

    gui.setItem(6, 1, goToMainMenuItem.createItem());

    gui.setDefaultClickAction(event -> event.setCancelled(true));

    gui.open(whoOpen);
  }
}
