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

package fr.djaytan.diagonia.playershop.controller.api;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface MessageController {

  void sendInfoMessage(@NotNull Audience audience, @NotNull Component message);

  void sendSuccessMessage(@NotNull Audience audience, @NotNull Component message);

  void sendFailureMessage(@NotNull Audience audience, @NotNull Component message);

  void sendWarningMessage(@NotNull Audience audience, @NotNull Component message);

  void sendErrorMessage(@NotNull Audience audience, @NotNull Component message);

  void sendConsoleMessage(@NotNull Component message);

  void broadcastMessage(@NotNull Component message);
}
