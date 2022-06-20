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

package fr.djaytan.minecraft.playershop;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@EqualsAndHashCode
// TODO: get rid of it and include JDBC URL formatting into DatabaseConfig class instead
public final class JdbcUrl {

  private final String jdbcDriver;
  private final String host;
  private final int port;
  private final String database;

  public JdbcUrl(@NotNull String host, int port, @NotNull String database) {
    this("mariadb", host, port, database);
  }

  public JdbcUrl(
      @NotNull String jdbcDriver, @NotNull String host, int port, @NotNull String database) {
    this.jdbcDriver = jdbcDriver;
    this.host = host;
    this.port = port;
    this.database = database;
  }

  public @NotNull String asStringUrl() {
    return String.format("jdbc:%s://%s:%d/%s", jdbcDriver, host, port, database);
  }
}
