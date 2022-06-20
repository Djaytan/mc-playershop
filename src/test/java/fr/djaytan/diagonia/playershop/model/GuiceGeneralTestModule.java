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

package fr.djaytan.diagonia.playershop.model;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import fr.djaytan.diagonia.playershop.model.dao.api.PlayerShopDao;
import fr.djaytan.diagonia.playershop.model.dao.implementation.PlayerShopDaoImpl;
import fr.djaytan.diagonia.playershop.model.service.api.PlayerShopService;
import fr.djaytan.diagonia.playershop.model.service.implementation.PlayerShopServiceImpl;
import javax.inject.Named;
import javax.inject.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiceGeneralTestModule extends AbstractModule {

  @Override
  public void configure() {
    bind(PlayerShopDao.class).to(PlayerShopDaoImpl.class);
    bind(PlayerShopService.class).to(PlayerShopServiceImpl.class);
  }

  @Provides
  @Singleton
  @Named("debugMode")
  public boolean provideDebugMode() {
    return false;
  }

  @Provides
  @Singleton
  public @NotNull Logger provideSlf4jLogger() {
    return LoggerFactory.getLogger(GuiceGeneralTestModule.class);
  }

  @Provides
  @Singleton
  public @NotNull SessionFactory provideSessionFactory() {
    // The SessionFactory must be built only once for application lifecycle
    return new Configuration().configure().buildSessionFactory();
  }
}
