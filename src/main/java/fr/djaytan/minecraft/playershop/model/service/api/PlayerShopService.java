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

import fr.djaytan.minecraft.playershop.model.entity.PlayerShop;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public interface PlayerShopService {

  @NotNull
  CompletableFuture<Void> persist(@NotNull PlayerShop playerShop);

  @NotNull
  CompletableFuture<Void> update(@NotNull PlayerShop playerShop);

  @NotNull
  CompletableFuture<Optional<PlayerShop>> findById(long id);

  @NotNull
  CompletableFuture<Optional<PlayerShop>> findByUuid(@NotNull UUID uuid);

  @NotNull
  CompletableFuture<List<PlayerShop>> findAll();
}
