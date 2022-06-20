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

package fr.djaytan.diagonia.playershop.view;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.milkbowl.vault.economy.Economy;
import org.jetbrains.annotations.NotNull;

@Singleton
public class EconomyFormatter {

  private final Economy economy;

  @Inject
  public EconomyFormatter(@NotNull Economy economy) {
    this.economy = economy;
  }

  public @NotNull String format(double amount) {
    return economy.format(amount);
  }
}