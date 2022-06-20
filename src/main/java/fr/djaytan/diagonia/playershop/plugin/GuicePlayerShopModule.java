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

package fr.djaytan.diagonia.playershop.plugin;

import co.aikar.commands.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import fr.djaytan.diagonia.playershop.JdbcUrl;
import fr.djaytan.diagonia.playershop.PlayerShopException;
import fr.djaytan.diagonia.playershop.PlayerShopRuntimeException;
import fr.djaytan.diagonia.playershop.controller.api.ConfigController;
import fr.djaytan.diagonia.playershop.controller.api.MessageController;
import fr.djaytan.diagonia.playershop.controller.api.PlayerController;
import fr.djaytan.diagonia.playershop.controller.implementation.ConfigControllerImpl;
import fr.djaytan.diagonia.playershop.controller.implementation.MessageControllerImpl;
import fr.djaytan.diagonia.playershop.controller.implementation.PlayerControllerImpl;
import fr.djaytan.diagonia.playershop.controller.implementation.PluginControllerImpl;
import fr.djaytan.diagonia.playershop.model.config.data.PluginConfig;
import fr.djaytan.diagonia.playershop.model.dao.api.PlayerShopDao;
import fr.djaytan.diagonia.playershop.model.dao.implementation.PlayerShopDaoImpl;
import fr.djaytan.diagonia.playershop.model.entity.PlayerShop;
import fr.djaytan.diagonia.playershop.model.service.api.EconomyService;
import fr.djaytan.diagonia.playershop.model.service.api.PlayerShopService;
import fr.djaytan.diagonia.playershop.model.service.implementation.EconomyVaultServiceImpl;
import fr.djaytan.diagonia.playershop.model.service.implementation.PlayerShopServiceImpl;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopConfigController;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopController;
import fr.djaytan.diagonia.playershop.controller.api.PlayerShopListController;
import fr.djaytan.diagonia.playershop.controller.api.PluginController;
import fr.djaytan.diagonia.playershop.controller.implementation.PlayerShopConfigControllerImpl;
import fr.djaytan.diagonia.playershop.controller.implementation.PlayerShopControllerImpl;
import fr.djaytan.diagonia.playershop.controller.implementation.PlayerShopListControllerImpl;
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
