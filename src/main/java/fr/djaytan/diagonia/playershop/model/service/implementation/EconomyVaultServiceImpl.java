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

package fr.djaytan.diagonia.playershop.model.service.implementation;

import com.google.common.base.Preconditions;
import fr.djaytan.diagonia.playershop.model.service.api.exception.EconomyException;
import fr.djaytan.diagonia.playershop.model.service.api.EconomyService;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.EconomyResponse;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public class EconomyVaultServiceImpl implements EconomyService {

  private final Logger logger;
  private final Economy economy;

  @Inject
  public EconomyVaultServiceImpl(@NotNull Logger logger, @NotNull Economy economy) {
    this.logger = logger;
    this.economy = economy;
  }

  @Override
  public double getBalance(@NotNull OfflinePlayer offlinePlayer) {
    double balance = economy.getBalance(offlinePlayer);

    logger.debug(
        "Economy balance recovered for a player: playerName={}, balance={}",
        offlinePlayer.getName(),
        balance);

    return balance;
  }

  @Override
  public @NotNull EconomyResponse withdraw(@NotNull OfflinePlayer offlinePlayer, double amount)
      throws EconomyException {
    Preconditions.checkArgument(amount >= 0, "The amount to withdraw must be a positive number");

    net.milkbowl.vault.economy.EconomyResponse vaultEconomyResponse =
        economy.withdrawPlayer(offlinePlayer, amount);

    boolean isTransactionFailed =
        vaultEconomyResponse.type.equals(
            net.milkbowl.vault.economy.EconomyResponse.ResponseType.FAILURE);

    if (isTransactionFailed) {
      throw new EconomyException(
          String.format(
              "[Vault] Failed to withdraw money to player %s: %s",
              offlinePlayer.getName(), vaultEconomyResponse.errorMessage));
    }

    boolean isMethodNotImplemented =
        vaultEconomyResponse.type.equals(
            net.milkbowl.vault.economy.EconomyResponse.ResponseType.NOT_IMPLEMENTED);

    if (isMethodNotImplemented) {
      throw new EconomyException("[Vault] Method not implemented");
    }

    logger.debug(
        "Economy withdraw for a player: playerName={}, amount={}, newBalance={}",
        offlinePlayer.getName(),
        vaultEconomyResponse.amount,
        vaultEconomyResponse.balance);

    return EconomyResponse.builder()
        .modifiedAmount(vaultEconomyResponse.amount)
        .newBalance(vaultEconomyResponse.balance)
        .build();
  }

  @Override
  public boolean isAffordable(@NotNull Player player, double price) {
    Preconditions.checkArgument(price >= 0, "The price must be higher or equal to 0.");

    double balance = getBalance(player);
    boolean isAffordable = price <= balance;

    logger.debug(
        "Is economy price affordable for a player: playerName={}, playerBalance={}, price={},"
            + " isAffordable={}",
        player.getName(),
        balance,
        price,
        isAffordable);

    return price <= balance;
  }
}
