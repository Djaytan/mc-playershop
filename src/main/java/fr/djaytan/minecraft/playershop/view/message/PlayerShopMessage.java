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

package fr.djaytan.minecraft.playershop.view.message;

import fr.djaytan.minecraft.playershop.model.service.api.parameter.EconomyResponse;
import fr.djaytan.minecraft.playershop.model.service.api.parameter.LocationDto;
import fr.djaytan.minecraft.playershop.view.EconomyFormatter;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerShopMessage {

  private final EconomyFormatter economyFormatter;
  private final MiniMessage miniMessage;
  private final ResourceBundle resourceBundle;

  @Inject
  public PlayerShopMessage(
      @NotNull EconomyFormatter economyFormatter,
      @NotNull MiniMessage miniMessage,
      @NotNull ResourceBundle resourceBundle) {
    this.economyFormatter = economyFormatter;
    this.miniMessage = miniMessage;
    this.resourceBundle = resourceBundle;
  }

  public @NotNull Component buySuccess(@NotNull EconomyResponse economyResponse) {
    return miniMessage.deserialize(
        resourceBundle.getString("playershop.playershop.buy.success"),
        TagResolver.resolver(
            Placeholder.unparsed(
                "ps_price", economyFormatter.format(economyResponse.getModifiedAmount())),
            Placeholder.unparsed(
                "ps_new_balance", economyFormatter.format(economyResponse.getNewBalance()))));
  }

  public @NotNull Component teleportPointDefined(@NotNull LocationDto locationDto) {
    return miniMessage.deserialize(
        resourceBundle.getString("playershop.playershop.config.teleportation.defined"),
        TagResolver.resolver(
            Placeholder.component(
                "ps_teleport_point",
                miniMessage.deserialize(
                    resourceBundle.getString(
                        "playershop.playershop.config.teleportation.defined.location"),
                    TagResolver.resolver(
                        Placeholder.unparsed(
                            "ps_location_x", String.format("%.2f", locationDto.getX())),
                        Placeholder.unparsed(
                            "ps_location_y", String.format("%.2f", locationDto.getY())),
                        Placeholder.unparsed(
                            "ps_location_z", String.format("%.2f", locationDto.getZ())),
                        Placeholder.unparsed(
                            "ps_location_yaw", String.format("%.2f", locationDto.getYaw())),
                        Placeholder.unparsed(
                            "ps_location_pitch",
                            String.format("%.2f", locationDto.getPitch())))))));
  }

  public @NotNull Component toggleShop(boolean isPlayerShopActive) {
    return miniMessage.deserialize(
        resourceBundle.getString("playershop.playershop.config.activation.toggled"),
        TagResolver.resolver(
            Placeholder.component(
                "ps_activation_state",
                miniMessage.deserialize(
                    resourceBundle.getString(
                        isPlayerShopActive
                            ? "playershop.playershop.config.activation.toggled.enabled"
                            : "playershop.playershop.config.activation.toggled.disabled")))));
  }

  public @NotNull Component shopActivationRequireTeleportPointFirst() {
    return miniMessage.deserialize(
        resourceBundle.getString(
            "playershop.playershop.config.activation.enabling.fail.teleport_point_definition_required"));
  }

  public @NotNull Component insufficientFunds() {
    return miniMessage.deserialize(
        resourceBundle.getString("playershop.playershop.buy.fail.insufficient_funds"));
  }

  public @NotNull Component transactionFailed() {
    return miniMessage.deserialize(
        resourceBundle.getString("playershop.playershop.buy.fail.transaction_failed"));
  }

  public @NotNull Component noPlayerShopForSpecifiedPlayer() {
    return miniMessage
        .deserialize(
            resourceBundle.getString(
                "playershop.playershop.teleportation.fail.no_playershop_for_specified_player"))
        .decoration(TextDecoration.ITALIC, false);
  }

  public @NotNull Component playerShopDeactivated() {
    return miniMessage
        .deserialize(
            resourceBundle.getString(
                "playershop.playershop.teleportation.fail.playershop_deactivated"))
        .decoration(TextDecoration.ITALIC, false);
  }

  public @NotNull Component noTeleportPointDefined() {
    return miniMessage.deserialize(
        resourceBundle.getString("playershop.playershop.teleportation.fail.no_tp_defined_error"));
  }

  public @NotNull Component tpCreationImpossibleInThisWorld() {
    return miniMessage
        .deserialize(
            resourceBundle.getString(
                "playershop.playershop.teleportation.fail.tp_creation_impossible_in_this_world"))
        .decoration(TextDecoration.ITALIC, false);
  }

  public @NotNull Component successTeleport(@NotNull String ownerName) {
    return miniMessage.deserialize(
        resourceBundle.getString("playershop.playershop.teleportation.success"),
        TagResolver.resolver(Placeholder.unparsed("ps_owner_name", ownerName)));
  }
}
