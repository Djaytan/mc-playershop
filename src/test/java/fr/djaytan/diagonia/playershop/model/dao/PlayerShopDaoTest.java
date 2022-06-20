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

package fr.djaytan.diagonia.playershop.model.dao;

import fr.djaytan.diagonia.playershop.model.AbstractBaseTest;
import fr.djaytan.diagonia.playershop.model.dao.implementation.PlayerShopDaoImpl;
import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.LocationDto;
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
