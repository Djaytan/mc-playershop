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

package fr.djaytan.minecraft.playershop.model.config.serializers;

import fr.djaytan.minecraft.playershop.model.config.serializers.adventure.AdventureConfigSerializers;
import fr.djaytan.minecraft.playershop.model.config.serializers.bukkit.BukkitConfigSerializers;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@Singleton
public class PlayerShopConfigSerializers {

  private final AdventureConfigSerializers adventureConfigSerializers;
  private final BukkitConfigSerializers bukkitConfigSerializers;

  @Inject
  public PlayerShopConfigSerializers(
      @NotNull AdventureConfigSerializers adventureConfigSerializers,
      @NotNull BukkitConfigSerializers bukkitConfigSerializers) {
    this.adventureConfigSerializers = adventureConfigSerializers;
    this.bukkitConfigSerializers = bukkitConfigSerializers;
  }

  public @NotNull TypeSerializerCollection collection() {
    return TypeSerializerCollection.builder()
        .registerAll(adventureConfigSerializers.collection())
        .registerAll(bukkitConfigSerializers.collection())
        .build();
  }
}
