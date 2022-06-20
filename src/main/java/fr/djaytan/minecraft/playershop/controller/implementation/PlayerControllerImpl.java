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

package fr.djaytan.minecraft.playershop.controller.implementation;

import com.google.common.base.Preconditions;
import fr.djaytan.minecraft.playershop.controller.api.PlayerController;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public class PlayerControllerImpl implements PlayerController {

  private final Logger logger;
  private final ResourceBundle resourceBundle;

  @Inject
  public PlayerControllerImpl(@NotNull Logger logger, @NotNull ResourceBundle resourceBundle) {
    this.logger = logger;
    this.resourceBundle = resourceBundle;
  }

  @Override
  public @NotNull String getOfflinePlayerName(@NotNull OfflinePlayer offlinePlayer) {
    Preconditions.checkNotNull(offlinePlayer);

    String offlinePlayerName = offlinePlayer.getName();

    if (offlinePlayerName == null) {
      logger.warn("The UUID {} isn't associated to any player name.", offlinePlayer.getUniqueId());
      offlinePlayerName = resourceBundle.getString("playershop.common.default_player_name");
    }

    return offlinePlayerName;
  }
}
