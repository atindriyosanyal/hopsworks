/*
 * This file is part of Hopsworks
 * Copyright (C) 2019, Logical Clocks AB. All rights reserved
 *
 * Hopsworks is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Hopsworks is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package io.hops.hopsworks.persistence.entity.featurestore.statistics.distribution;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * XML/JSON DTO representing feature distributions for a featuregroup or training dataset
 */
@XmlRootElement
@XmlType(propOrder = {"featureDistributions"})
public class FeatureDistributionsDTO {

  private List<FeatureDistributionDTO> featureDistributions;

  public FeatureDistributionsDTO() {
    //Default empty constructor required for JAXB
  }

  @XmlElement
  public List<FeatureDistributionDTO> getFeatureDistributions() {
    return featureDistributions;
  }

  public void setFeatureDistributions(List<FeatureDistributionDTO> featureDistributions) {
    this.featureDistributions = featureDistributions;
  }

  @Override
  public String toString() {
    return "FeatureDistributionsDTO{" +
        "featureDistributions=" + featureDistributions +
        '}';
  }
}
