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

package fr.djaytan.diagonia.playershop.model.entity;

import fr.djaytan.diagonia.playershop.model.entity.converter.LocationDtoConverter;
import fr.djaytan.diagonia.playershop.model.entity.converter.UUIDConverter;
import fr.djaytan.diagonia.playershop.model.service.api.parameter.LocationDto;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Material;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "diagonia_ps_playershop")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class PlayerShop {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ps_id", nullable = false, updatable = false)
  private long id;

  @NaturalId
  @Column(name = "ps_owner_uuid", nullable = false, unique = true, updatable = false)
  @Convert(converter = UUIDConverter.class)
  @Setter
  @NonNull
  private UUID ownerUuid;

  @Column(name = "ps_item_icon")
  @Enumerated(EnumType.STRING)
  @Setter
  private Material itemIcon;

  @Column(name = "ps_tp_location")
  @Convert(converter = LocationDtoConverter.class)
  @Setter
  private LocationDto tpLocationDto;

  @Column(name = "ps_is_active", nullable = false)
  @Setter
  private boolean isActive;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayerShop that)) {
      return false;
    }
    return new EqualsBuilder().append(ownerUuid, that.ownerUuid).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(ownerUuid).toHashCode();
  }

  public boolean hasItemIcon() {
    return itemIcon != null;
  }
}
