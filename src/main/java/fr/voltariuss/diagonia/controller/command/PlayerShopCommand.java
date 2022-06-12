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

package fr.voltariuss.diagonia.controller.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.voltariuss.diagonia.controller.api.PlayerShopController;
import fr.voltariuss.diagonia.controller.api.PlayerShopListController;
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
