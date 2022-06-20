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

package fr.djaytan.diagonia.playershop.controller.implementation;

import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.model.service.api.PlayerShopService;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopController;
import fr.djaytan.diagonia.playershop.view.gui.PlayerShopConfigGui;
import fr.djaytan.diagonia.playershop.view.gui.PlayerShopListGui;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public class PlayerShopControllerImpl implements PlayerShopController {

  private final Executor mainThreadExecutor;
  private final Logger logger;
  private final PlayerShopService playerShopService;

  private final Provider<PlayerShopConfigGui> playerShopConfigGui;
  private final Provider<PlayerShopListGui> playerShopListGui;

  @Inject
  public PlayerShopControllerImpl(
      @NotNull Executor mainThreadExecutor,
      @NotNull Logger logger,
      @NotNull PlayerShopService playerShopService,
      @NotNull Provider<PlayerShopConfigGui> playerShopConfigGui,
      @NotNull Provider<PlayerShopListGui> playerShopListGui) {
    this.mainThreadExecutor = mainThreadExecutor;
    this.logger = logger;
    this.playerShopService = playerShopService;
    this.playerShopConfigGui = playerShopConfigGui;
    this.playerShopListGui = playerShopListGui;
  }

  @Override
  public void openPlayerShopListGui(@NotNull Player whoOpen) {
    logger.debug("Open PlayerShopList GUI for a player: playerName={}", whoOpen.getName());

    CompletableFuture.runAsync(
        () -> {
          // TODO: launch these requests in parallel then join, but inspect potential side effects
          Optional<PlayerShop> playerShopOwned =
              playerShopService.findByUuid(whoOpen.getUniqueId()).join();
          List<PlayerShop> playerShopList =
              playerShopService
                  .findAll()
                  .thenApplyAsync(
                      playerShops -> {
                        // TODO: move shuffle into business logic part
                        Collections.shuffle(playerShops);
                        return playerShops;
                      })
                  .join();

          CompletableFuture.runAsync(
              () ->
                  playerShopListGui
                      .get()
                      .open(whoOpen, playerShopList, playerShopOwned.isPresent()),
              mainThreadExecutor);
        });
  }

  @Override
  public void openPlayerShopConfigGui(@NotNull Player whoOpen) {
    logger.debug("Open PlayerShopConfig GUI for player {}", whoOpen.getName());

    CompletableFuture.runAsync(
        () -> {
          PlayerShop playerShop =
              playerShopService.findByUuid(whoOpen.getUniqueId()).join().orElseThrow();

          CompletableFuture.runAsync(
              () -> playerShopConfigGui.get().open(whoOpen, playerShop), mainThreadExecutor);
        });
  }
}
