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

package fr.voltariuss.diagonia.controller.implementation;

import fr.voltariuss.diagonia.controller.api.MessageController;
import fr.voltariuss.diagonia.controller.api.PlayerShopConfigController;
import fr.voltariuss.diagonia.controller.api.PlayerShopController;
import fr.voltariuss.diagonia.model.config.data.PluginConfig;
import fr.voltariuss.diagonia.model.entity.PlayerShop;
import fr.voltariuss.diagonia.model.service.api.PlayerShopService;
import fr.voltariuss.diagonia.model.service.api.parameter.LocationDto;
import fr.voltariuss.diagonia.model.service.api.parameter.mapper.LocationMapper;
import fr.voltariuss.diagonia.view.message.PlayerShopMessage;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public class PlayerShopConfigControllerImpl implements PlayerShopConfigController {

  private final Logger logger;
  private final LocationMapper locationMapper;
  private final MessageController messageController;
  private final PlayerShopController playerShopController;
  private final PlayerShopMessage playerShopMessage;
  private final PlayerShopService playerShopService;
  private final PluginConfig pluginConfig;

  @Inject
  public PlayerShopConfigControllerImpl(
      @NotNull Logger logger,
      @NotNull LocationMapper locationMapper,
      @NotNull MessageController messageController,
      @NotNull PlayerShopController playerShopController,
      @NotNull PlayerShopMessage playerShopMessage,
      @NotNull PlayerShopService playerShopService,
      @NotNull PluginConfig pluginConfig) {
    this.logger = logger;
    this.locationMapper = locationMapper;
    this.messageController = messageController;
    this.playerShopController = playerShopController;
    this.playerShopMessage = playerShopMessage;
    this.playerShopService = playerShopService;
    this.pluginConfig = pluginConfig;
  }

  @Override
  public void defineTeleportPoint(
      @NotNull CommandSender sender,
      @NotNull PlayerShop playerShop,
      @NotNull Location newLocation) {
    logger.debug(
        "Start defining a teleport point: senderName={}, playerShopId={},"
            + " playerShopCurrentTpLocation={}, newLocation={}",
        sender.getName(),
        playerShop.getId(),
        playerShop.getTpLocationDto(),
        newLocation);

    if (!newLocation
        .getWorld()
        .getName()
        .equals(pluginConfig.getPlayerShop().getTpCreationAllowedWorld())) {
      messageController.sendFailureMessage(
          sender, playerShopMessage.tpCreationImpossibleInThisWorld());
      return;
    }

    LocationDto newLocationDto = locationMapper.toDto(newLocation);

    playerShop.setTpLocationDto(newLocationDto);
    playerShopService.update(playerShop);

    logger.debug(
        "Updated teleport point for playershop: playerShopOwnerUuid={}, playerShopId={},"
            + " playerShopNewTpLocation={}",
        playerShop.getOwnerUuid(),
        playerShop.getId(),
        playerShop.getTpLocationDto());

    messageController.sendInfoMessage(
        sender, playerShopMessage.teleportPointDefined(newLocationDto));
  }

  @Override
  public void togglePlayerShop(@NotNull Player sender, @NotNull PlayerShop playerShop) {
    logger.debug(
        "Start toggling playershop: senderName={}, playerShopId={}, playerShopIsActive={},"
            + " playerShopTpLocation={}",
        sender.getName(),
        playerShop.getId(),
        playerShop.isActive(),
        playerShop.getTpLocationDto());

    if (playerShop.getTpLocationDto() == null && playerShop.isActive()) {
      throw new IllegalStateException(
          "A playershop without teleport point defined mustn't be activated.");
    }

    if (playerShop.getTpLocationDto() == null) {
      logger.debug("Failed to toggle playershop: tp location must be defined.");

      messageController.sendFailureMessage(
          sender, playerShopMessage.shopActivationRequireTeleportPointFirst());

      return;
    }

    playerShop.setActive(!playerShop.isActive());
    playerShopService.update(playerShop);

    logger.debug(
        "Toggled playershop: senderName={}, playerShopId={}, isNowActive={}",
        sender.getName(),
        playerShop.getId(),
        playerShop.isActive());

    messageController.sendInfoMessage(sender, playerShopMessage.toggleShop(playerShop.isActive()));
    playerShopController.openPlayerShopConfigGui(sender);
  }
}
