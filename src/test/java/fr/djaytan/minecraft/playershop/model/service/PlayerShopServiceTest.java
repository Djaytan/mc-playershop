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

package fr.djaytan.minecraft.playershop.model.service;

import fr.djaytan.minecraft.playershop.model.AbstractBaseTest;
import fr.djaytan.minecraft.playershop.model.entity.PlayerShop;
import fr.djaytan.minecraft.playershop.model.service.api.PlayerShopService;
import java.util.UUID;
import javax.inject.Inject;
import junit.framework.Assert;
import org.bukkit.Material;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PlayerShop service")
class PlayerShopServiceTest extends AbstractBaseTest {

  @Inject private PlayerShopService playerShopService;

  @Test
  void givenNewPlayerShop_WhenPersisted_ThenShouldBeRegisteredIntoDatabase() {
    PlayerShop ps = new PlayerShop(UUID.randomUUID());
    ps.setItemIcon(Material.NAME_TAG);
    ps.setTpLocationDto(null);
    ps.setActive(true);

    playerShopService.persist(ps).join();

    PlayerShop retrievedPs = playerShopService.findById(ps.getId()).join().orElse(null);
    Assert.assertNotNull(retrievedPs);
    Assert.assertEquals(ps, retrievedPs);
  }
}
