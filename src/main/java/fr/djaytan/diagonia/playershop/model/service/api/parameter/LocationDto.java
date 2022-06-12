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

package fr.djaytan.diagonia.playershop.model.service.api.parameter;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class LocationDto {

  @NonNull private String worldName;
  @NonNull private Double x;
  @NonNull private Double y;
  @NonNull private Double z;
  @NonNull private Float yaw;
  @NonNull private Float pitch;
}
