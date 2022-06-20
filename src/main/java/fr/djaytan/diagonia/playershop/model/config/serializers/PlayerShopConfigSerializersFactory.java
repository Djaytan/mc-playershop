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

package fr.djaytan.diagonia.playershop.model.config.serializers;

import fr.djaytan.diagonia.playershop.model.config.serializers.adventure.TextColorConfigSerializer;
import fr.djaytan.diagonia.playershop.model.config.serializers.bukkit.MaterialConfigSerializer;
import fr.djaytan.diagonia.playershop.model.config.serializers.adventure.AdventureConfigSerializers;
import fr.djaytan.diagonia.playershop.model.config.serializers.bukkit.BukkitConfigSerializers;
import fr.djaytan.diagonia.playershop.model.config.serializers.bukkit.EnchantmentConfigSerializer;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerShopConfigSerializersFactory {

  public @NotNull PlayerShopConfigSerializers factory() {
    return new PlayerShopConfigSerializers(
        new AdventureConfigSerializers(new TextColorConfigSerializer()),
        new BukkitConfigSerializers(
            new EnchantmentConfigSerializer(), new MaterialConfigSerializer()));
  }
}
