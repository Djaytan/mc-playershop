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

package fr.djaytan.diagonia.playershop.model.dao.implementation;

import com.google.common.base.Preconditions;
import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.model.dao.JpaDao;
import fr.djaytan.diagonia.playershop.model.dao.api.PlayerShopDao;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

/** DAO class of {@link PlayerShop} entity. */
@Singleton
public class PlayerShopDaoImpl extends JpaDao<PlayerShop, Long> implements PlayerShopDao {

  @Inject
  public PlayerShopDaoImpl(@NotNull SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public @NotNull CompletableFuture<Optional<PlayerShop>> findByUuid(@NotNull UUID uuid) {
    Preconditions.checkNotNull(uuid);

    return executeQueryTransaction(
            session ->
                session
                    .createQuery(
                        "SELECT ps FROM PlayerShop ps WHERE ps.ownerUuid = :uuid", PlayerShop.class)
                    .setParameter("uuid", uuid))
        .thenApplyAsync(
            playerShops ->
                playerShops.isEmpty() ? Optional.empty() : Optional.of(playerShops.get(0)));
  }
}
