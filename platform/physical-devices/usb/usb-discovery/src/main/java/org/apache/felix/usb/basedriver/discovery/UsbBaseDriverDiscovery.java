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
package org.apache.felix.usb.basedriver.discovery;

import java.util.Map;

import org.apache.felix.usb.basedriver.descriptor.UsbDevice;

/**
 * The Interface UsbBaseDriverDiscovery.
 */
public interface UsbBaseDriverDiscovery {

    /**
     * List devices.
     * 
     * @return list of usb devices or empty map
     */
    public Map<String, UsbDevice> listDevices();

    /**
     * Find devices.
     * 
     * @param vendorID the vendor id
     * @param productID the product id
     * @return the map
     */
    public Map<String, UsbDevice> findDevices(short vendorID, short productID);

    /**
     * Gets the device.
     * 
     * @param id the id
     * @return the device
     */
    public UsbDevice getDevice(String id);

}