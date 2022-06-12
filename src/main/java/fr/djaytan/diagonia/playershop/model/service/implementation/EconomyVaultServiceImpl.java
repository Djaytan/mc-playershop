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
