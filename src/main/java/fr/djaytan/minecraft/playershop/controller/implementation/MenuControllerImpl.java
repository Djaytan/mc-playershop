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

package fr.djaytan.minecraft.playershop.controller.implementation;

import fr.djaytan.minecraft.playershop.controller.api.MenuController;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public class MenuControllerImpl implements MenuController {

  private final Logger logger;
  private final Server server;

  @Inject
  public MenuControllerImpl(@NotNull Logger logger, @NotNull Server server) {
    this.logger = logger;
    this.server = server;
  }

  @Override
  public void openMainMenu(@NotNull Player whoOpen) {
    logger.debug("Open MainMenu GUI for a player: playerName={}", whoOpen.getName());
    whoOpen.closeInventory(Reason.PLUGIN);
    server.dispatchCommand(whoOpen, "dm open advanced_menu");
  }
}
