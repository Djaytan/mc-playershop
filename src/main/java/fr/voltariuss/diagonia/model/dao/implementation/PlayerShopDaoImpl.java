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

package fr.voltariuss.diagonia.model.dao.implementation;

import com.google.common.base.Preconditions;
import fr.voltariuss.diagonia.model.dao.JpaDao;
import fr.voltariuss.diagonia.model.dao.api.PlayerShopDao;
import fr.voltariuss.diagonia.model.entity.PlayerShop;
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
