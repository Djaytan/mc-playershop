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

import com.google.common.base.Preconditions;
import fr.voltariuss.diagonia.controller.api.PlayerController;
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
      offlinePlayerName = resourceBundle.getString("diagonia.common.default_player_name");
    }

    return offlinePlayerName;
  }
}
