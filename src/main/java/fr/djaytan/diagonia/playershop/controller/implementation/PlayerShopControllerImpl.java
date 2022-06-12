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
