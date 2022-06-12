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

package fr.djaytan.diagonia.playershop.plugin;

import co.aikar.commands.PaperCommandManager;
import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.model.service.api.PlayerShopService;
import fr.djaytan.diagonia.playershop.controller.command.PlayerShopCommand;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CommandRegister {

  private final PaperCommandManager paperCommandManager;

  private final PlayerShopCommand playerShopCommand;
  private final PlayerShopService playerShopService;
  private final Server server;

  @Inject
  public CommandRegister(
      @NotNull PaperCommandManager paperCommandManager,
      @NotNull PlayerShopCommand playerShopCommand,
      @NotNull PlayerShopService playerShopService,
      @NotNull Server server) {
    this.paperCommandManager = paperCommandManager;
    this.playerShopCommand = playerShopCommand;
    this.playerShopService = playerShopService;
    this.server = server;
  }

  public void registerCommands() {
    paperCommandManager.registerCommand(playerShopCommand);
  }

  public void registerCommandCompletions() {
    paperCommandManager
        .getCommandCompletions()
        .registerAsyncCompletion(
            "allplayers",
            context -> {
              long size = Long.parseLong(context.getConfig("size", "100"));
              return Arrays.stream(server.getOfflinePlayers())
                  .limit(size)
                  .map(OfflinePlayer::getName)
                  .toList();
            });

    paperCommandManager
        .getCommandCompletions()
        .registerAsyncCompletion(
            "playershops",
            context ->
                playerShopService.findAll().join().stream()
                    .map(PlayerShop::getOwnerUuid)
                    .map(server::getOfflinePlayer)
                    .map(OfflinePlayer::getName)
                    .map(Objects::requireNonNull)
                    .toList());
  }
}
