/**
 *
 *   Copyright 2011-2012 Universite Joseph Fourier, LIG, ADELE team
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package fr.liglab.adele.icasa.device.gazSensor;

import fr.liglab.adele.icasa.device.GenericDevice;

/**
 * Service definition of a CO2 sensor device.
 * 
 * @author jeremy
 */
public interface CarbonDioxydeSensor extends GenericDevice {

    /**
     * Service property indicating the current value of CO2 measured by the gaz sensor, expressed in µg/m^3.
     * 
     * <ul>
     * <li>This property is <b>mandatory</b></li>
     * <li>Type of values : <b><code>java.lang.Double</code></b></li>
     * <li>Description : value is a CO2 concentration expressed in µg/m^3, so it
     * is <code>always positive</code>.</li>
     * </ul>
     * 
     * @see #getCO2Concentration()
     */
    String CO2_CURRENT_CONCENTRATION = "co2_current_concentration";

    /**
     * Return the current CO2 concentration sensed by this gaz sensor, expressed in
     * µg/m^3.
     * 
     * @return the current CO2 concentration sensed by this gaz sensor, expressed in
     *         µg/m^3.
     */
    double getCO2Concentration();

}
