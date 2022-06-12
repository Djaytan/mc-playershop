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

package fr.voltariuss.diagonia.controller.implementation;

import fr.voltariuss.diagonia.JdbcUrl;
import fr.voltariuss.diagonia.controller.api.MessageController;
import fr.voltariuss.diagonia.controller.api.PluginController;
import fr.voltariuss.diagonia.model.config.data.PluginConfig;
import fr.voltariuss.diagonia.plugin.CommandRegister;
import fr.voltariuss.diagonia.plugin.PrerequisitesValidation;
import fr.voltariuss.diagonia.view.message.CommonMessage;
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
