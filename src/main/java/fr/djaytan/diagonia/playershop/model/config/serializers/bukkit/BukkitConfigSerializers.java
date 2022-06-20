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

package fr.djaytan.diagonia.playershop.model.config.serializers.bukkit;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@Singleton
public class BukkitConfigSerializers {

  private final EnchantmentConfigSerializer enchantmentConfigSerializer;
  private final MaterialConfigSerializer materialConfigSerializer;

  @Inject
  public BukkitConfigSerializers(
      @NotNull EnchantmentConfigSerializer enchantmentConfigSerializer,
      @NotNull MaterialConfigSerializer materialConfigSerializer) {
    this.enchantmentConfigSerializer = enchantmentConfigSerializer;
    this.materialConfigSerializer = materialConfigSerializer;
  }

  public @NotNull TypeSerializerCollection collection() {
    return TypeSerializerCollection.builder()
        .registerExact(Enchantment.class, enchantmentConfigSerializer)
        .registerExact(Material.class, materialConfigSerializer)
        .build();
  }
}
