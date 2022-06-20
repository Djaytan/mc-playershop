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

package fr.djaytan.diagonia.playershop.controller.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopController;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopListController;
import javax.inject.Inject;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@CommandAlias("playershop|ps")
public class PlayerShopCommand extends BaseCommand {

  private final Logger logger;
  private final PlayerShopController playerShopController;
  private final PlayerShopListController playerShopListController;

  @Inject
  public PlayerShopCommand(
      @NotNull Logger logger,
      @NotNull PlayerShopController playerShopController,
      @NotNull PlayerShopListController playerShopListController) {
    this.logger = logger;
    this.playerShopController = playerShopController;
    this.playerShopListController = playerShopListController;
  }

  @Default
  public void onExecute(@NotNull Player player) {
    logger.debug("/playershop command executed by {}", player.getName());
    playerShopController.openPlayerShopListGui(player);
  }

  @Subcommand("tp")
  @CommandCompletion("@playershops")
  public void onTeleport(@NotNull Player player, @NotNull String targetedPlayerName) {
    playerShopListController.teleportToPlayerShop(player, targetedPlayerName);
  }
}
