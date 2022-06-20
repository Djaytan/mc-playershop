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

package fr.djaytan.minecraft.playershop.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public interface Dao<T, I extends Serializable> {

  @NotNull
  CompletableFuture<Optional<T>> findById(@NotNull I id);

  @NotNull
  CompletableFuture<List<T>> findAll();

  @NotNull
  CompletableFuture<Void> persist(@NotNull T entity);

  @NotNull
  CompletableFuture<Void> update(@NotNull T entity);

  @NotNull
  CompletableFuture<Void> delete(@NotNull T entity);
}
