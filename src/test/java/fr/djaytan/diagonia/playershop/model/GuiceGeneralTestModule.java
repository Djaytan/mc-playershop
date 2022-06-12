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
