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

package fr.voltariuss.diagonia.model.config.serializers;

import fr.voltariuss.diagonia.model.config.serializers.adventure.AdventureConfigSerializers;
import fr.voltariuss.diagonia.model.config.serializers.bukkit.BukkitConfigSerializers;
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
