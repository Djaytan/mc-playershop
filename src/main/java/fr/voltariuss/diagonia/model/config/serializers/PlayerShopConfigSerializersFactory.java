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
import fr.voltariuss.diagonia.model.config.serializers.adventure.TextColorConfigSerializer;
import fr.voltariuss.diagonia.model.config.serializers.bukkit.BukkitConfigSerializers;
import fr.voltariuss.diagonia.model.config.serializers.bukkit.EnchantmentConfigSerializer;
import fr.voltariuss.diagonia.model.config.serializers.bukkit.MaterialConfigSerializer;
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
