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

package fr.djaytan.diagonia.playershop.model.service.api;

import fr.djaytan.diagonia.playershop.model.service.api.exception.EconomyException;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This interface describes contracts to be followed by implementation services for economy
 * management.
 *
 * @author Voltariuss
 */
public interface EconomyService {

  /**
   * Gets and returns the current balance value of the specified player.
   *
   * @param offlinePlayer The player concerned by the consultation.
   * @return The current balance value of the specified player.
   */
  double getBalance(@NotNull OfflinePlayer offlinePlayer);

  /**
   * Withdraws the specified amount of money to the targeted player.
   *
   * @param offlinePlayer The targeted player.
   * @param amount The amount of money to withdraw.
   * @return The economy response when the operation has been correctly executed.
   * @throws EconomyException If the implementation service failed to withdraw.
   */
  @NotNull
  EconomyResponse withdraw(@NotNull OfflinePlayer offlinePlayer, double amount)
      throws EconomyException;

  /**
   * Checks if the given price is affordable for the specified player.
   *
   * @param player The player involved in the check.
   * @param price The price to check if it's affordable.
   * @return <code>true</code> if the price if affordable for the specified player, <code>false
   *     </code> otherwise.
   */
  boolean isAffordable(@NotNull Player player, double price);
}
