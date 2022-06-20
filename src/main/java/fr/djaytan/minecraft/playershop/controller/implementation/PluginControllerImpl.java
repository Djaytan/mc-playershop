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

package fr.djaytan.minecraft.playershop.controller.implementation;

import fr.djaytan.minecraft.playershop.JdbcUrl;
import fr.djaytan.minecraft.playershop.controller.api.MessageController;
import fr.djaytan.minecraft.playershop.model.config.data.PluginConfig;
import fr.djaytan.minecraft.playershop.plugin.CommandRegister;
import fr.djaytan.minecraft.playershop.plugin.PrerequisitesValidation;
import fr.djaytan.minecraft.playershop.view.message.CommonMessage;
import fr.djaytan.minecraft.playershop.controller.api.PluginController;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.plugin.Plugin;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public class PluginControllerImpl implements PluginController {

  private final CommandRegister commandRegister;
  private final CommonMessage commonMessage;
  private final JdbcUrl jdbcUrl;
  private final Logger logger;
  private final MessageController messageController;
  private final Plugin plugin;
  private final PluginConfig pluginConfig;
  private final PrerequisitesValidation prerequisitesValidation;
  private final SessionFactory sessionFactory;

  @Inject
  public PluginControllerImpl(
      @NotNull CommandRegister commandRegister,
      @NotNull CommonMessage commonMessage,
      @NotNull JdbcUrl jdbcUrl,
      @NotNull Logger logger,
      @NotNull MessageController messageController,
      @NotNull Plugin plugin,
      @NotNull PluginConfig pluginConfig,
      @NotNull PrerequisitesValidation prerequisitesValidation,
      @NotNull SessionFactory sessionFactory) {
    this.commandRegister = commandRegister;
    this.commonMessage = commonMessage;
    this.jdbcUrl = jdbcUrl;
    this.logger = logger;
    this.messageController = messageController;
    this.plugin = plugin;
    this.pluginConfig = pluginConfig;
    this.prerequisitesValidation = prerequisitesValidation;
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void disablePlugin() {
    sessionFactory.close();
    logger.info("Database connections closed");
    logger.info("Plugin successfully disabled");
  }

  @Override
  public void enablePlugin() {
    try {
      // TODO: startup banner in separated class in view
      messageController.sendConsoleMessage(commonMessage.startupBanner());
      messageController.sendConsoleMessage(
          commonMessage.startupBannerVersionLine(plugin.getDescription()));

      messageController.sendConsoleMessage(
          commonMessage.startupBannerProgressionLine("General config file loading"));
      messageController.sendConsoleMessage(
          commonMessage.startupBannerStateLine(
              "Debug Mode", Boolean.toString(pluginConfig.isDebug())));
      messageController.sendConsoleMessage(
          commonMessage.startupBannerStateLine("Database connection URL", jdbcUrl.asStringUrl()));
      messageController.sendConsoleMessage(
          commonMessage.startupBannerStateLine(
              "Database username", pluginConfig.getDatabase().getUsername()));

      messageController.sendConsoleMessage(
          commonMessage.startupBannerProgressionLine("Guice full injection"));

      prerequisitesValidation.validate();

      messageController.sendConsoleMessage(
          commonMessage.startupBannerProgressionLine("Dependencies validation"));

      commandRegister.registerCommands();
      commandRegister.registerCommandCompletions();

      messageController.sendConsoleMessage(
          commonMessage.startupBannerProgressionLine("Commands registration"));

      messageController.sendConsoleMessage(commonMessage.startupBannerEnablingSuccessLine());
    } catch (Exception e) {
      // TODO: more centralized error management (listeners, commands, ...)
      messageController.sendConsoleMessage(commonMessage.startupBannerEnablingFailureLine());
      logger.error("Something went wrong and prevent plugin activation.", e);
      logger.error("Disabling plugin...");
      plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
  }
}
