package fr.djaytan.diagonia.playershop.plugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.djaytan.diagonia.playershop.controller.api.ConfigController;
import fr.djaytan.diagonia.playershop.controller.api.PluginController;
import fr.djaytan.diagonia.playershop.controller.implementation.ConfigControllerImpl;
import fr.djaytan.diagonia.playershop.model.config.data.PluginConfig;
import fr.djaytan.diagonia.playershop.model.config.serializers.PlayerShopConfigSerializersFactory;
import javax.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;

public class DiagoniaPlayerShopPlugin extends JavaPlugin {

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
