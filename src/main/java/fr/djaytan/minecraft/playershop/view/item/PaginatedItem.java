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

package fr.djaytan.minecraft.playershop.view.item;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PaginatedItem {

  private static final Material PAGE_ITEM = Material.ARROW;

  private final MiniMessage miniMessage;
  private final ResourceBundle resourceBundle;

  @Inject
  public PaginatedItem(@NotNull MiniMessage miniMessage, @NotNull ResourceBundle resourceBundle) {
    this.miniMessage = miniMessage;
    this.resourceBundle = resourceBundle;
  }

  public @NotNull GuiItem createPreviousPageItem(@NotNull PaginatedGui guiHolder) {
    return ItemBuilder.from(PAGE_ITEM)
        .name(
            miniMessage
                .deserialize(resourceBundle.getString("diagonia.gui.page.previous"))
                .decoration(TextDecoration.ITALIC, false))
        .asGuiItem(inventoryClickEvent -> guiHolder.previous());
  }

  public @NotNull GuiItem createNextPageItem(@NotNull PaginatedGui guiHolder) {
    return ItemBuilder.from(PAGE_ITEM)
        .name(
            miniMessage
                .deserialize(resourceBundle.getString("diagonia.gui.page.next"))
                .decoration(TextDecoration.ITALIC, false))
        .asGuiItem(inventoryClickEvent -> guiHolder.next());
  }
}
