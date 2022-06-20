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

package fr.djaytan.diagonia.playershop.controller.api;

import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerShopListController {

  void teleportToPlayerShop(@NotNull Player playerToTp, @NotNull PlayerShop playerShopDestination);

  void teleportToPlayerShop(@NotNull Player playerToTp, @NotNull String targetedPlayerName);

  void buyPlayerShop(@NotNull Player player);
}