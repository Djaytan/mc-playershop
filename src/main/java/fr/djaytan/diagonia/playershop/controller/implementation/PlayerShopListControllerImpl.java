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

import fr.djaytan.diagonia.playershop.controller.api.MessageController;
import fr.djaytan.diagonia.playershop.controller.api.PlayerController;
import fr.djaytan.diagonia.playershop.model.config.data.PluginConfig;
import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.model.service.api.EconomyService;
import fr.djaytan.diagonia.playershop.model.service.api.PlayerShopService;
import fr.djaytan.diagonia.playershop.model.service.api.exception.EconomyException;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.EconomyResponse;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.mapper.LocationMapper;
import fr.djaytan.diagonia.playershop.view.message.CommonMessage;
import fr.djaytan.diagonia.playershop.view.message.PlayerShopMessage;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopController;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopListController;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public class PlayerShopListControllerImpl implements PlayerShopListController {

  private final Executor mainThreadExecutor;
  private final CommonMessage commonMessage;
  private final EconomyService economyService;
  private final LocationMapper locationMapper;
  private final Logger logger;
  private final MessageController messageController;
  private final PlayerController playerController;
  private final PlayerShopController playerShopController;
  private final PlayerShopMessage playerShopMessage;
  private final PlayerShopService playerShopService;
  private final PluginConfig pluginConfig;
  private final Server server;

  @Inject
  public PlayerShopListControllerImpl(
      @NotNull Executor mainThreadExecutor,
      @NotNull CommonMessage commonMessage,
      @NotNull EconomyService economyService,
      @NotNull LocationMapper locationMapper,
      @NotNull Logger logger,
      @NotNull MessageController messageController,
      @NotNull PlayerController playerController,
      @NotNull PlayerShopController playerShopController,
      @NotNull PlayerShopMessage playerShopMessage,
      @NotNull PlayerShopService playerShopService,
      @NotNull PluginConfig pluginConfig,
      @NotNull Server server) {
    this.mainThreadExecutor = mainThreadExecutor;
    this.commonMessage = commonMessage;
    this.economyService = economyService;
    this.locationMapper = locationMapper;
    this.logger = logger;
    this.messageController = messageController;
    this.playerController = playerController;
    this.playerShopController = playerShopController;
    this.playerShopMessage = playerShopMessage;
    this.playerShopService = playerShopService;
    this.pluginConfig = pluginConfig;
    this.server = server;
  }

  @Override
  public void teleportToPlayerShop(
      @NotNull Player playerToTp, @NotNull PlayerShop playerShopDestination) {
    logger.debug(
        "Start event handling of teleporting a player to a playershop: playerToTpUuid={},"
            + " playerShopDestinationId={}",
        playerToTp.getUniqueId(),
        playerShopDestination.getId());

    Location tpLocation = locationMapper.fromDto(playerShopDestination.getTpLocationDto());

    if (tpLocation == null) {
      logger.warn(
          "Failed to teleport a player to a playershop because no teleport point has been defined."
              + " This may be an error because activated playershops are supposed to have a"
              + " teleport point defined: playerToTpUuid={}, playerShopId={}",
          playerToTp.getUniqueId(),
          playerShopDestination.getId());
      messageController.sendFailureMessage(playerToTp, playerShopMessage.noTeleportPointDefined());
      return;
    }

    playerToTp.teleport(tpLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);

    OfflinePlayer psOwner = server.getOfflinePlayer(playerShopDestination.getOwnerUuid());
    String psOwnerName = playerController.getOfflinePlayerName(psOwner);
    messageController.sendInfoMessage(playerToTp, playerShopMessage.successTeleport(psOwnerName));

    logger.debug(
        "Teleportation of a player to a playershop: playerToTpUuid={}, playerShopId={}",
        playerToTp.getUniqueId(),
        playerShopDestination.getId());
  }

  @Override
  public void teleportToPlayerShop(@NotNull Player playerToTp, @NotNull String targetedPlayerName) {
    OfflinePlayer targetedOfflinePlayer = server.getOfflinePlayerIfCached(targetedPlayerName);

    if (targetedOfflinePlayer == null) {
      messageController.sendFailureMessage(
          playerToTp, commonMessage.playerNotFound(targetedPlayerName));
      return;
    }

    playerShopService
        .findByUuid(targetedOfflinePlayer.getUniqueId())
        .thenAcceptAsync(
            playerShop -> {
              if (playerShop.isEmpty()) {
                messageController.sendFailureMessage(
                    playerToTp, playerShopMessage.noPlayerShopForSpecifiedPlayer());
                return;
              }

              if (!playerShop.get().isActive()) {
                messageController.sendFailureMessage(
                    playerToTp, playerShopMessage.playerShopDeactivated());
                return;
              }

              teleportToPlayerShop(playerToTp, playerShop.get());
            },
            mainThreadExecutor);
  }

  @Override
  public void buyPlayerShop(@NotNull Player player) {
    // TODO: 2PC with JTA
    logger.debug("Start buying a playershop for player {}", player.getName());

    double playerShopPrice = pluginConfig.getPlayerShop().getBuyCost();

    if (!economyService.isAffordable(player, playerShopPrice)) {
      messageController.sendFailureMessage(player, playerShopMessage.insufficientFunds());
      logger.debug(
          "The player can't afford a playershop: playerName={}, playerShopPrice={}",
          player.getName(),
          playerShopPrice);
      return;
    }

    try {
      EconomyResponse economyResponse = economyService.withdraw(player, playerShopPrice);
      PlayerShop ps = new PlayerShop(player.getUniqueId());

      playerShopService
          .persist(ps)
          .thenRunAsync(
              () -> {
                messageController.sendSuccessMessage(
                    player, playerShopMessage.buySuccess(economyResponse));
                playerShopController.openPlayerShopListGui(player);
                logger.info(
                    "Purchase of a playershop for the player {} ({}) for the price of {}. New"
                        + " solde: {}",
                    player.getName(),
                    player.getUniqueId(),
                    economyResponse.getModifiedAmount(),
                    economyResponse.getNewBalance());
              },
              mainThreadExecutor);
    } catch (EconomyException e) {
      logger.error(
          "Failed to withdraw money from the player's balance: playerShopPrice={}, playerName={},"
              + " errorMessage={}",
          playerShopPrice,
          player.getName(),
          e.getMessage());
      messageController.sendErrorMessage(player, playerShopMessage.transactionFailed());
    }
  }
}
