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

package fr.djaytan.diagonia.playershop.view;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.milkbowl.vault.economy.Economy;
import org.jetbrains.annotations.NotNull;

@Singleton
public class EconomyFormatter {

  private final Economy economy;

  @Inject
  public EconomyFormatter(@NotNull Economy economy) {
    this.economy = economy;
  }

  public @NotNull String format(double amount) {
    return economy.format(amount);
  }
}
