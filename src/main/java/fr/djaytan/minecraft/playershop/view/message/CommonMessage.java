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

package fr.djaytan.minecraft.playershop.view.message;

import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CommonMessage {

  private static final Component STARTUP_BANNER_INDENT = Component.text("      ");

  private final MiniMessage miniMessage;
  private final ResourceBundle resourceBundle;

  @Inject
  public CommonMessage(@NotNull MiniMessage miniMessage, @NotNull ResourceBundle resourceBundle) {
    this.miniMessage = miniMessage;
    this.resourceBundle = resourceBundle;
  }

  public @NotNull Component startupBanner() {
    return miniMessage.deserialize(
        resourceBundle.getString("diagonia.common.message.startup.banner"),
        TagResolver.resolver(Placeholder.component("diag_indentation", STARTUP_BANNER_INDENT)));
  }

  public @NotNull Component startupBannerVersionLine(
      @NotNull PluginDescriptionFile pluginDescriptionFile) {
    return STARTUP_BANNER_INDENT.append(
        miniMessage.deserialize(
            resourceBundle.getString("diagonia.common.message.startup.current_version"),
            TagResolver.resolver(
                Placeholder.unparsed("diag_plugin_version", pluginDescriptionFile.getVersion()))));
  }

  public @NotNull Component startupBannerProgressionLine(@NotNull String text) {
    return STARTUP_BANNER_INDENT.append(
        miniMessage.deserialize(
            resourceBundle.getString("diagonia.common.message.startup.progression_line.format"),
            TagResolver.resolver(Placeholder.unparsed("diag_progression_message", text))));
  }

  public @NotNull Component startupBannerStateLine(
      @NotNull String key, @NotNull String value) {
    return STARTUP_BANNER_INDENT.append(Component.text("  ")).append(
        miniMessage.deserialize(
            resourceBundle.getString("diagonia.common.message.startup.state.format"),
            TagResolver.resolver(
                Placeholder.unparsed("diag_key", key), Placeholder.unparsed("diag_value", value))));
  }

  public @NotNull Component startupBannerEnablingSuccessLine() {
    return STARTUP_BANNER_INDENT.append(
        miniMessage.deserialize(
            resourceBundle.getString("diagonia.common.message.startup.enabling_successfully")));
  }

  public @NotNull Component startupBannerEnablingFailureLine() {
    return STARTUP_BANNER_INDENT.append(
        miniMessage.deserialize(
            resourceBundle.getString("diagonia.common.message.startup.enabling_failed")));
  }

  public @NotNull Component unexpectedError() {
    return miniMessage
        .deserialize(resourceBundle.getString("diagonia.common.fail.unexpected_error"))
        .decoration(TextDecoration.ITALIC, false);
  }

  public @NotNull Component playerNotFound(String playerName) {
    return miniMessage
        .deserialize(
            resourceBundle.getString("diagonia.common.fail.player_not_found"),
            TagResolver.resolver(Placeholder.unparsed("diag_player_name", playerName)))
        .decoration(TextDecoration.ITALIC, false);
  }
}
