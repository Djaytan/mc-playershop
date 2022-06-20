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

package fr.djaytan.diagonia.playershop.model.config.serializers.adventure;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@Singleton
public class AdventureConfigSerializers {

  private final TextColorConfigSerializer textColorConfigSerializer;

  @Inject
  public AdventureConfigSerializers(@NotNull TextColorConfigSerializer textColorConfigSerializer) {
    this.textColorConfigSerializer = textColorConfigSerializer;
  }

  public @NotNull TypeSerializerCollection collection() {
    return TypeSerializerCollection.builder()
        .registerExact(TextColor.class, textColorConfigSerializer)
        .build();
  }
}
