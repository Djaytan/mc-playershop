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
import fr.djaytan.diagonia.playershop.model.config.data.PluginConfig;
import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.model.service.api.PlayerShopService;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.LocationDto;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.mapper.LocationMapper;
import fr.djaytan.diagonia.playershop.view.message.PlayerShopMessage;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopConfigController;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopController;
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
