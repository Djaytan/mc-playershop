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

package fr.djaytan.minecraft.playershop.model.service.api;

import fr.djaytan.minecraft.playershop.model.service.api.exception.EconomyException;
import fr.djaytan.minecraft.playershop.model.service.api.parameter.EconomyResponse;
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
