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
package fr.liglab.adele.icasa.location.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.liglab.adele.icasa.ContextManager;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.location.LocatedDevice;
import fr.liglab.adele.icasa.location.LocatedDeviceListener;
import fr.liglab.adele.icasa.location.Position;
import fr.liglab.adele.icasa.location.Zone;

public class LocatedDeviceImpl extends LocatedObjectImpl implements LocatedDevice, DeviceListener {

	private String m_serialNumber;

	private final List<LocatedDeviceListener> listeners = new ArrayList<LocatedDeviceListener>();
		
	private GenericDevice deviceComponent;
	
	private ContextManager manager;

    private String _type;

	public LocatedDeviceImpl(String serialNumber, Position position, GenericDevice deviceComponent, String type, ContextManager manager) {
		super(position);
		m_serialNumber = serialNumber;
		this.deviceComponent = deviceComponent;			
		this.manager = manager;
      this._type = type;
	}

	@Override
	public String getSerialNumber() {
		return m_serialNumber;
	}

    @Override
    public String getType() {
        return _type;
    }

    @Override
	public Set<String> getProperties() {
		/*
		synchronized (properties) {
			return properties.keySet();
		}
		*/
		return deviceComponent.getProperties();
	}

	@Override
	public Object getPropertyValue(String propertyName) {		
		if (deviceComponent!=null)
			return deviceComponent.getPropertyValue(propertyName);
		return null;
	}

	@Override
	public void setPropertyValue(String propertyName, Object value) {
		
		if (deviceComponent==null)
			return;
		//TODO: Test which properties are readonly
		deviceComponent.setPropertyValue(propertyName, value);
	}

	@Override
	public synchronized void addListener(final LocatedDeviceListener listener) {
		if (listener == null) {
			throw new NullPointerException("listener");
		}
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public synchronized void removeListener(final LocatedDeviceListener listener) {
		if (listener == null) {
			throw new NullPointerException("listener");
		}
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}



	@Override
   public void setCenterAbsolutePosition(Position position) {
		Position oldPosition = getCenterAbsolutePosition();
		super.setCenterAbsolutePosition(position);
				
		// Listeners notification
		for (LocatedDeviceListener listener : listeners) {
			listener.deviceMoved(this, oldPosition);
		}
		
		// Computes the new location
		if (deviceComponent!=null) {
			Zone zone = manager.getZoneFromPosition(getCenterAbsolutePosition());
			String location = GenericDevice.LOCATION_UNKNOWN;
			if (zone!=null)			
				location = zone.getId();			
			deviceComponent.setPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME, location);
		}
			
   }
	
	@Override
	public String toString() {
		return "Id: " + getSerialNumber() + " - Position: " + getCenterAbsolutePosition();
	}

	@Override
   public void enterInZones(List<Zone> zones) {			
		deviceComponent.enterInZones(zones);			
   }

	@Override
   public void leavingZones(List<Zone> zones) {
		deviceComponent.leavingZones(zones);	   
   }

	// --- Listeners methods -- //
	
	@Override
   public void deviceAdded(GenericDevice device) {
	   // TODO Auto-generated method stub
	   
   }

	@Override
   public void deviceRemoved(GenericDevice device) {
	   // TODO Auto-generated method stub
	   
   }

	@Override
   public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue) {
		// Listeners notification
		for (LocatedDeviceListener listener : listeners) {
			listener.devicePropertyModified(this, propertyName, oldValue);
		}		   
   }

	@Override
   public void devicePropertyAdded(GenericDevice device, String propertyName) {
		// Listeners notification
		for (LocatedDeviceListener listener : listeners) {
			listener.devicePropertyAdded(this, propertyName);
		}		   	   
   }

	@Override
   public void devicePropertyRemoved(GenericDevice device, String propertyName) {
	   // TODO Auto-generated method stub
		for (LocatedDeviceListener listener : listeners) {
			listener.devicePropertyRemoved(this, propertyName);
		}		
   }

}
