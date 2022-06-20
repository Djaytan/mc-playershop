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
import fr.djaytan.minecraft.playershop.controller.api.PlayerController;
import fr.djaytan.minecraft.playershop.model.entity.PlayerShop;
import fr.djaytan.minecraft.playershop.controller.api.PlayerShopListController;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class ConsultPlayerShopItem {

  private final MiniMessage miniMessage;
  private final PlayerController playerController;
  private final PlayerShopListController playerShopListController;
  private final ResourceBundle resourceBundle;

  @Inject
  public ConsultPlayerShopItem(
      @NotNull MiniMessage miniMessage,
      @NotNull PlayerController playerController,
      @NotNull PlayerShopListController playerShopListController,
      @NotNull ResourceBundle resourceBundle) {
    this.miniMessage = miniMessage;
    this.playerController = playerController;
    this.playerShopListController = playerShopListController;
    this.resourceBundle = resourceBundle;
  }

  public @Nullable GuiItem createItem(
      @NotNull OfflinePlayer ownerPlayer, @NotNull PlayerShop playerShop) {
    Preconditions.checkState(
        ownerPlayer.getUniqueId().equals(playerShop.getOwnerUuid()),
        "The owner player specified isn't the same than the one of the playershop.");

    if (!playerShop.isActive()) {
      return null;
    }

    String ownerName = playerController.getOfflinePlayerName(ownerPlayer);

    Component itemName = getName(ownerName);

    GuiItem psItem;

    if (playerShop.hasItemIcon()) {
      psItem = ItemBuilder.from(playerShop.getItemIcon()).name(itemName).asGuiItem();
    } else {
      psItem = ItemBuilder.skull().owner(ownerPlayer).name(itemName).asGuiItem(onClick(playerShop));
    }

    return psItem;
  }

  private @NotNull GuiAction<InventoryClickEvent> onClick(@NotNull PlayerShop playerShop) {
    return event -> {
      Player player = (Player) event.getWhoClicked();
      playerShopListController.teleportToPlayerShop(player, playerShop);
    };
  }

  private @NotNull Component getName(@NotNull String ownerName) {
    return miniMessage
        .deserialize(
            resourceBundle.getString("playershop.playershop.list.shop.name"),
            TagResolver.resolver(Placeholder.unparsed("ps_player_name", ownerName)))
        .decoration(TextDecoration.ITALIC, false);
  }
}
