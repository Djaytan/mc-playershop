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

package fr.djaytan.diagonia.playershop.model.entity.converter;

import com.google.gson.Gson;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.LocationDto;
import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;
import org.jetbrains.annotations.NotNull;

public class LocationDtoConverter implements AttributeConverter<LocationDto, String> {

  @Override
  public @NotNull String convertToDatabaseColumn(@Nullable LocationDto locationDto) {
    Gson gson = new Gson();
    return gson.toJson(locationDto);
  }

  @Override
  public @Nullable LocationDto convertToEntityAttribute(@Nullable String locationJson) {
    Gson gson = new Gson();
    return gson.fromJson(locationJson, LocationDto.class);
  }
}
