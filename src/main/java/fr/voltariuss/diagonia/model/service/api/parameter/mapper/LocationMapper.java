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

package fr.voltariuss.diagonia.model.service.api.parameter.mapper;

import fr.voltariuss.diagonia.model.service.api.parameter.LocationDto;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class LocationMapper {

  private final Server server;

  @Inject
  public LocationMapper(@NotNull Server server) {
    this.server = server;
  }

  @Contract("null -> null; !null -> !null")
  public LocationDto toDto(@Nullable Location location) {
    LocationDto locationDto = null;
    if (location != null) {
      locationDto =
          LocationDto.builder()
              .worldName(location.getWorld().getName())
              .x(location.getX())
              .y(location.getY())
              .z(location.getZ())
              .yaw(location.getYaw())
              .pitch(location.getPitch())
              .build();
    }
    return locationDto;
  }

  @Contract("null -> null; !null -> !null")
  public Location fromDto(@Nullable LocationDto locationDto) {
    Location location = null;
    if (locationDto != null) {
      World world = server.getWorld(locationDto.getWorldName());
      location =
          new Location(
              world,
              locationDto.getX(),
              locationDto.getY(),
              locationDto.getZ(),
              locationDto.getYaw(),
              locationDto.getPitch());
    }
    return location;
  }
}
