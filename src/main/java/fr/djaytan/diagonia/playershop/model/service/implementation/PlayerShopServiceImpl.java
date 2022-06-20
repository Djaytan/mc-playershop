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

import fr.djaytan.diagonia.playershop.model.dao.api.PlayerShopDao;
import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.model.service.api.PlayerShopService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerShopServiceImpl implements PlayerShopService {

  private final PlayerShopDao playerShopDao;

  @Inject
  public PlayerShopServiceImpl(@NotNull PlayerShopDao playerShopDao) {
    this.playerShopDao = playerShopDao;
  }

  @Override
  public @NotNull CompletableFuture<Void> persist(@NotNull PlayerShop playerShop) {
    return playerShopDao.persist(playerShop);
  }

  @Override
  public @NotNull CompletableFuture<Void> update(@NotNull PlayerShop playerShop) {
    return playerShopDao.update(playerShop);
  }

  @Override
  public @NotNull CompletableFuture<Optional<PlayerShop>> findById(long id) {
    return playerShopDao.findById(id);
  }

  @Override
  public @NotNull CompletableFuture<Optional<PlayerShop>> findByUuid(@NotNull UUID uuid) {
    return playerShopDao.findByUuid(uuid);
  }

  @Override
  public @NotNull CompletableFuture<List<PlayerShop>> findAll() {
    return playerShopDao.findAll();
  }
}
