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

package fr.voltariuss.diagonia.model.dao;

import fr.voltariuss.diagonia.model.AbstractBaseTest;
import fr.voltariuss.diagonia.model.dao.implementation.PlayerShopDaoImpl;
import fr.voltariuss.diagonia.model.entity.PlayerShop;
import fr.voltariuss.diagonia.model.service.api.parameter.LocationDto;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import junit.framework.Assert;
import org.bukkit.Material;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PlayerShop DAO Test")
class PlayerShopDaoTest extends AbstractBaseTest {

  @Inject private PlayerShopDaoImpl playerShopDao;

  @Test
  void givenDefaultObject_whenPersisted_thenFindable() {
    PlayerShop ps = new PlayerShop(UUID.randomUUID());

    playerShopDao.persist(ps).join();
    List<PlayerShop> playerShopList = playerShopDao.findAll().join();

    Assert.assertEquals(1, playerShopList.size());
  }

  @Test
  void givenDefaultInstance_whenUpdated_thenSucceed() {
    Material initItemIcon = Material.STONE;
    Material updatedItemIcon = Material.COBBLESTONE;

    PlayerShop ps = new PlayerShop(UUID.randomUUID());
    ps.setItemIcon(initItemIcon);

    playerShopDao.persist(ps).join();

    ps.setItemIcon(updatedItemIcon);

    playerShopDao.update(ps).join();
    PlayerShop psBis = playerShopDao.findAll().join().get(0);

    Assert.assertEquals(updatedItemIcon, psBis.getItemIcon());
  }

  @Nested
  @DisplayName("FindById method")
  class FindByIdTest {

    @Test
    void givenDefaultObjectPersisted_whenFindById_thenMatchInitialObject() {
      PlayerShop ps = new PlayerShop(UUID.randomUUID());

      playerShopDao.persist(ps).join();
      PlayerShop psBis = playerShopDao.findAll().join().get(0);

      Assert.assertEquals(ps.getOwnerUuid(), psBis.getOwnerUuid());
      Assert.assertEquals(ps.getItemIcon(), psBis.getItemIcon());
      Assert.assertEquals(ps.getTpLocationDto(), psBis.getTpLocationDto());
      Assert.assertEquals(ps.isActive(), psBis.isActive());
    }

    @Test
    void givenCustomizedObjectPersisted_whenFindById_thenMatchInitialObject() {
      PlayerShop ps = new PlayerShop(UUID.randomUUID());
      ps.setItemIcon(Material.NAME_TAG);
      ps.setTpLocationDto(
          LocationDto.builder()
              .worldName("two")
              .x(0.5D)
              .y(0.5D)
              .z(0.5D)
              .pitch(0.5f)
              .yaw(0.5f)
              .build());
      ps.setActive(true);

      playerShopDao.persist(ps).join();
      List<PlayerShop> playerShopList = playerShopDao.findAll().join();

      PlayerShop psBis = playerShopList.get(0);

      Assert.assertEquals(ps.getOwnerUuid(), psBis.getOwnerUuid());
      Assert.assertEquals(ps.getItemIcon(), psBis.getItemIcon());
      Assert.assertEquals(ps.getTpLocationDto(), psBis.getTpLocationDto());
      Assert.assertEquals(ps.isActive(), psBis.isActive());
    }

    @Test
    void givenNoneObjectPersisted_whenFindById_thenRecoverNullObject() {
      PlayerShop ps = playerShopDao.findById(1L).join().orElse(null);

      Assert.assertNull(ps);
    }
  }

  @Test
  void givenObjectPersisted_whenDeleted_thenAbsentFromDb() {
    PlayerShop ps = new PlayerShop(UUID.randomUUID());

    playerShopDao.persist(ps).join();
    playerShopDao.delete(ps).join();
    PlayerShop psBis = playerShopDao.findById(1L).join().orElse(null);
    System.out.println("DEBUG: " + psBis);

    Assert.assertNull(psBis);
  }

  @Nested
  @DisplayName("FindAll method")
  class FindAllTest {

    @Test
    void givenMultipleObjects_whenFindAll_thenAllRecovered() {
      PlayerShop ps1 = new PlayerShop(UUID.randomUUID());
      PlayerShop ps2 = new PlayerShop(UUID.randomUUID());

      playerShopDao.persist(ps1).join();
      playerShopDao.persist(ps2).join();
      List<PlayerShop> playerShopList = playerShopDao.findAll().join();

      Assert.assertEquals(2, playerShopList.size());
      Assert.assertEquals(ps1, playerShopList.get(0));
      Assert.assertEquals(ps2, playerShopList.get(1));
    }

    @Test
    void givenNoneObject_whenFindAll_thenRecoverNothing() {
      List<PlayerShop> playerShopList = playerShopDao.findAll().join();

      Assert.assertEquals(0, playerShopList.size());
    }
  }
}
