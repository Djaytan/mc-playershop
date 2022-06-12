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
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import fr.voltariuss.diagonia.model.entity.PlayerShop;
import fr.voltariuss.diagonia.view.item.GoToMainMenuItem;
import fr.voltariuss.diagonia.view.item.PaginatedItem;
import fr.voltariuss.diagonia.view.item.playershop.BuyPlayerShopItem;
import fr.voltariuss.diagonia.view.item.playershop.ConfigPlayerShopItem;
import fr.voltariuss.diagonia.view.item.playershop.ConsultPlayerShopItem;
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
