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

package fr.djaytan.minecraft.playershop.plugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.djaytan.minecraft.playershop.controller.api.ConfigController;
import fr.djaytan.minecraft.playershop.controller.api.PluginController;
import fr.djaytan.minecraft.playershop.controller.implementation.ConfigControllerImpl;
import fr.djaytan.minecraft.playershop.model.config.data.PluginConfig;
import fr.djaytan.minecraft.playershop.model.config.serializers.PlayerShopConfigSerializersFactory;
import javax.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerShopPlugin extends JavaPlugin {

  @Inject private PluginController pluginController;

  @Override
  public void onEnable() {
    try {
      // This is tricky at startup, but don't found better way than that...
      // Perfect startup would inject Guice immediately, but some injections need config values
      ConfigController configController =
          new ConfigControllerImpl(new PlayerShopConfigSerializersFactory().factory(), this);

      configController.saveDefaultConfigs();
      PluginConfig pluginConfig = configController.loadPluginConfig();

      Injector injector =
          Guice.createInjector(
              new GuiceBukkitModule(this),
              new GuicePlayerShopModule(getSLF4JLogger(), this, pluginConfig));
      injector.injectMembers(this);

      pluginController.enablePlugin();
    } catch (Exception e) {
      getSLF4JLogger().error("An exception occurs preventing PlayerShop plugin to be enabled.", e);
      setEnabled(false);
    }
  }

  @Override
  public void onDisable() {
    if (pluginController != null) {
      pluginController.disablePlugin();
    }
  }
}
