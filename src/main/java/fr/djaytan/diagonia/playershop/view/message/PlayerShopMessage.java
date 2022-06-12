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

package fr.djaytan.diagonia.playershop.view.message;

import fr.djaytan.diagonia.playershop.model.service.api.parameter.EconomyResponse;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.LocationDto;
import fr.djaytan.diagonia.playershop.view.EconomyFormatter;
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
        resourceBundle.getString("diagonia.playershop.buy.success"),
        TagResolver.resolver(
            Placeholder.unparsed(
                "diag_price", economyFormatter.format(economyResponse.getModifiedAmount())),
            Placeholder.unparsed(
                "diag_new_balance", economyFormatter.format(economyResponse.getNewBalance()))));
  }

  public @NotNull Component teleportPointDefined(@NotNull LocationDto locationDto) {
    return miniMessage.deserialize(
        resourceBundle.getString("diagonia.playershop.config.teleportation.defined"),
        TagResolver.resolver(
            Placeholder.component(
                "diag_teleport_point",
                miniMessage.deserialize(
                    resourceBundle.getString(
                        "diagonia.playershop.config.teleportation.defined.location"),
                    TagResolver.resolver(
                        Placeholder.unparsed(
                            "diag_location_x", String.format("%.2f", locationDto.getX())),
                        Placeholder.unparsed(
                            "diag_location_y", String.format("%.2f", locationDto.getY())),
                        Placeholder.unparsed(
                            "diag_location_z", String.format("%.2f", locationDto.getZ())),
                        Placeholder.unparsed(
                            "diag_location_yaw", String.format("%.2f", locationDto.getYaw())),
                        Placeholder.unparsed(
                            "diag_location_pitch",
                            String.format("%.2f", locationDto.getPitch())))))));
  }

  public @NotNull Component toggleShop(boolean isPlayerShopActive) {
    return miniMessage.deserialize(
        resourceBundle.getString("diagonia.playershop.config.activation.toggled"),
        TagResolver.resolver(
            Placeholder.component(
                "diag_activation_state",
                miniMessage.deserialize(
                    resourceBundle.getString(
                        isPlayerShopActive
                            ? "diagonia.playershop.config.activation.toggled.enabled"
                            : "diagonia.playershop.config.activation.toggled.disabled")))));
  }

  public @NotNull Component shopActivationRequireTeleportPointFirst() {
    return miniMessage.deserialize(
        resourceBundle.getString(
            "diagonia.playershop.config.activation.enabling.fail.teleport_point_definition_required"));
  }

  public @NotNull Component insufficientFunds() {
    return miniMessage.deserialize(
        resourceBundle.getString("diagonia.playershop.buy.fail.insufficient_funds"));
  }

  public @NotNull Component transactionFailed() {
    return miniMessage.deserialize(
        resourceBundle.getString("diagonia.playershop.buy.fail.transaction_failed"));
  }

  public @NotNull Component noPlayerShopForSpecifiedPlayer() {
    return miniMessage
        .deserialize(
            resourceBundle.getString(
                "diagonia.playershop.teleportation.fail.no_playershop_for_specified_player"))
        .decoration(TextDecoration.ITALIC, false);
  }

  public @NotNull Component playerShopDeactivated() {
    return miniMessage
        .deserialize(
            resourceBundle.getString(
                "diagonia.playershop.teleportation.fail.playershop_deactivated"))
        .decoration(TextDecoration.ITALIC, false);
  }

  public @NotNull Component noTeleportPointDefined() {
    return miniMessage.deserialize(
        resourceBundle.getString("diagonia.playershop.teleportation.fail.no_tp_defined_error"));
  }

  public @NotNull Component tpCreationImpossibleInThisWorld() {
    return miniMessage
        .deserialize(
            resourceBundle.getString(
                "diagonia.playershop.teleportation.fail.tp_creation_impossible_in_this_world"))
        .decoration(TextDecoration.ITALIC, false);
  }

  public @NotNull Component successTeleport(@NotNull String ownerName) {
    return miniMessage.deserialize(
        resourceBundle.getString("diagonia.playershop.teleportation.success"),
        TagResolver.resolver(Placeholder.unparsed("diag_owner_name", ownerName)));
  }
}
