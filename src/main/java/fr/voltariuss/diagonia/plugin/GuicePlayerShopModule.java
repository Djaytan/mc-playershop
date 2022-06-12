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

package fr.voltariuss.diagonia.plugin;

import co.aikar.commands.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import fr.voltariuss.diagonia.JdbcUrl;
import fr.voltariuss.diagonia.PlayerShopException;
import fr.voltariuss.diagonia.PlayerShopRuntimeException;
import fr.voltariuss.diagonia.controller.api.ConfigController;
import fr.voltariuss.diagonia.controller.api.MessageController;
import fr.voltariuss.diagonia.controller.api.PlayerController;
import fr.voltariuss.diagonia.controller.api.PlayerShopConfigController;
import fr.voltariuss.diagonia.controller.api.PlayerShopController;
import fr.voltariuss.diagonia.controller.api.PlayerShopListController;
import fr.voltariuss.diagonia.controller.api.PluginController;
import fr.voltariuss.diagonia.controller.implementation.ConfigControllerImpl;
import fr.voltariuss.diagonia.controller.implementation.MessageControllerImpl;
import fr.voltariuss.diagonia.controller.implementation.PlayerControllerImpl;
import fr.voltariuss.diagonia.controller.implementation.PlayerShopConfigControllerImpl;
import fr.voltariuss.diagonia.controller.implementation.PlayerShopControllerImpl;
import fr.voltariuss.diagonia.controller.implementation.PlayerShopListControllerImpl;
import fr.voltariuss.diagonia.controller.implementation.PluginControllerImpl;
import fr.voltariuss.diagonia.model.config.data.PluginConfig;
import fr.voltariuss.diagonia.model.dao.api.PlayerShopDao;
import fr.voltariuss.diagonia.model.dao.implementation.PlayerShopDaoImpl;
import fr.voltariuss.diagonia.model.entity.PlayerShop;
import fr.voltariuss.diagonia.model.service.api.EconomyService;
import fr.voltariuss.diagonia.model.service.api.PlayerShopService;
import fr.voltariuss.diagonia.model.service.implementation.EconomyVaultServiceImpl;
import fr.voltariuss.diagonia.model.service.implementation.PlayerShopServiceImpl;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.inject.Named;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/** General Guice module. */
public class GuicePlayerShopModule extends AbstractModule {

  private final JavaPlugin javaPlugin;
  private final JdbcUrl jdbcUrl;
  private final Logger logger;
  // TODO: use a provider of a config instead (required for reload feature of plugin)
  private final PluginConfig pluginConfig;

  public GuicePlayerShopModule(
      @NotNull Logger logger, @NotNull JavaPlugin javaPlugin, @NotNull PluginConfig pluginConfig) {
    this.javaPlugin = javaPlugin;
    this.jdbcUrl =
        new JdbcUrl(
            pluginConfig.getDatabase().getHost(),
            pluginConfig.getDatabase().getPort(),
            pluginConfig.getDatabase().getDatabase());
    this.logger = logger;
    this.pluginConfig = pluginConfig;
  }

  @Override
  public void configure() {
    bind(ConfigController.class).to(ConfigControllerImpl.class);
    bind(EconomyService.class).to(EconomyVaultServiceImpl.class);
    bind(MessageController.class).to(MessageControllerImpl.class);
    bind(PlayerController.class).to(PlayerControllerImpl.class);
    bind(PlayerShopController.class).to(PlayerShopControllerImpl.class);
    bind(PlayerShopConfigController.class).to(PlayerShopConfigControllerImpl.class);
    bind(PlayerShopDao.class).to(PlayerShopDaoImpl.class);
    bind(PlayerShopListController.class).to(PlayerShopListControllerImpl.class);
    bind(PlayerShopService.class).to(PlayerShopServiceImpl.class);
    bind(PluginController.class).to(PluginControllerImpl.class);
  }

  @Provides
  @Named("debugMode")
  public Boolean provideDebugMode() {
    logger.info("Debug mode: {}", pluginConfig.isDebug());
    return pluginConfig.isDebug();
  }

  @Provides
  public PluginConfig providePluginConfig() {
    return pluginConfig;
  }

  @Provides
  @Singleton
  public @NotNull ResourceBundle provideResourceBundle() {
    return ResourceBundle.getBundle("diagonia", Locale.FRANCE);
  }

  @Provides
  @Singleton
  public @NotNull JdbcUrl provideJdbcUrl() {
    return jdbcUrl;
  }

  @Provides
  @Singleton
  public @NotNull SessionFactory provideSessionFactory() {
    try {
      // The SessionFactory must be built only once for application lifecycle
      Configuration configuration = new Configuration();

      configuration.setProperty(AvailableSettings.URL, jdbcUrl.asStringUrl());
      configuration.setProperty(AvailableSettings.USER, pluginConfig.getDatabase().getUsername());
      configuration.setProperty(AvailableSettings.PASS, pluginConfig.getDatabase().getPassword());
      configuration.setProperty(
          AvailableSettings.CONNECTION_PROVIDER,
          "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
      configuration.setProperty(AvailableSettings.DRIVER, "org.mariadb.jdbc.Driver");
      configuration.setProperty(AvailableSettings.DATASOURCE, "org.mariadb.jdbc.MariaDbDataSource");
      configuration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.MariaDBDialect");
      configuration.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
      configuration.setProperty(AvailableSettings.SHOW_SQL, "false");
      configuration.setProperty(AvailableSettings.FORMAT_SQL, "false");
      configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, "update");
      configuration.setProperty(AvailableSettings.HBM2DDL_CHARSET_NAME, "UTF-8");
      // TODO: cache properties definition

      configuration.setProperty("hibernate.hikari.maximumPoolSize", "10");
      configuration.setProperty("hibernate.hikari.minimumIdle", "5");
      // Because plugin is mono-thread only one SQL request is dispatched at the same time, so there
      // isn't any concurrency with the database. It's why serializable transaction isolation is
      // actually the preference to ensure the best isolation as possible.
      configuration.setProperty(
          "hibernate.hikari.transactionIsolation", "TRANSACTION_SERIALIZABLE");

      configuration.addAnnotatedClass(PlayerShop.class);

      logger.info("Database connexion established.");
      return configuration.buildSessionFactory();
    } catch (HibernateException e) {
      throw new PlayerShopRuntimeException(
          String.format("Database connection failed: %s", jdbcUrl.asStringUrl()), e);
    }
  }

  @Provides
  @Singleton
  public @NotNull MiniMessage provideMiniMessage() {
    return MiniMessage.miniMessage();
  }

  @Provides
  @Singleton
  public @NotNull PaperCommandManager provideAcfPaperCommandManager() {
    return new PaperCommandManager(javaPlugin);
  }

  @Provides
  @Singleton
  public @NotNull Economy provideVaultEconomy() throws PlayerShopException {
    RegisteredServiceProvider<Economy> rsp =
        javaPlugin.getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      throw new PlayerShopException("Failed to found Economy service of Vault dependency.");
    }
    return rsp.getProvider();
  }
}
