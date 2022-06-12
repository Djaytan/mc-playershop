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

package fr.voltariuss.diagonia;

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
