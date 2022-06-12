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

package fr.voltariuss.diagonia.model.config.serializers.adventure;

import java.lang.reflect.Type;
import javax.inject.Singleton;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.enchantments.Enchantment;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

@Singleton
public class TextColorConfigSerializer implements TypeSerializer<TextColor> {

  @Override
  public TextColor deserialize(Type type, ConfigurationNode node) throws SerializationException {
    String textColorStr = node.getString();

    if (node.virtual() || textColorStr == null) {
      throw new SerializationException(
          node, Enchantment.class, "Required field of type TextColor not present.");
    }

    // TODO: What about TextColor#fromCSSHexString(String)?
    return TextColor.fromHexString(textColorStr);
  }

  @Override
  public void serialize(Type type, @Nullable TextColor obj, ConfigurationNode node)
      throws SerializationException {
    throw new NotImplementedException("TextColor config serialization not implemented yet.");
  }
}
