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

package fr.djaytan.minecraft.playershop.model.service.api.parameter.mapper;

import fr.djaytan.minecraft.playershop.model.service.api.parameter.LocationDto;
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
