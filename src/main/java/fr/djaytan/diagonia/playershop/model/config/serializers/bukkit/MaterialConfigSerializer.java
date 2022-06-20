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

import java.lang.reflect.Type;
import javax.inject.Singleton;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@Singleton
public class MaterialConfigSerializer implements TypeSerializer<Material> {

  @Override
  public Material deserialize(Type type, ConfigurationNode node) throws SerializationException {
    String materialName = node.getString();

    if (node.virtual() || materialName == null) {
      throw new SerializationException(
          node, Material.class, "Required field of type Material not present.");
    }
    return Material.matchMaterial(materialName);
  }

  @Override
  public void serialize(Type type, @Nullable Material obj, ConfigurationNode node)
      throws SerializationException {
    throw new NotImplementedException("Material config serialization not implemented yet.");
  }
}
