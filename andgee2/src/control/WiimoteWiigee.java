///*
// * wiigee - accelerometerbased gesture recognition
// * Copyright (C) 2007, 2008 Benjamin Poppinga
// * 
// * Developed at University of Oldenburg
// * Contact: benjamin.poppinga@informatik.uni-oldenburg.de
// *
// * This file is part of wiigee.
// *
// * wiigee is free software; you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as published by
// * the Free Software Foundation; either version 2 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Lesser General Public License for more details.
// * 
// * You should have received a copy of the GNU Lesser General Public License along
// * with this program; if not, write to the Free Software Foundation, Inc.,
// * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
// */
//
//package control;
//
//import java.io.IOException;
//import java.util.Vector;
//
//import javax.bluetooth.DiscoveryAgent;
//import javax.bluetooth.LocalDevice;
//
//import util.Log;
//import device.Wiimote;
//import event.DeviceListener;
//import event.GestureListener;
//import filter.Filter;
//
//// Singleton
//public class WiimoteWiigee {
//	
//	protected static String version = "1.3.1 alpha";
//	protected static String releasedate = "20081215";
//	
//	protected static WiimoteWiigee instance;
//	private static Object lock = new Object();
//	private Vector<Wiimote> devices;
//	
//	private WiimoteWiigee() throws IOException {
//		Log.write("This is wiigee version "+version+" ("+releasedate+")");
//		this.devices=this.discoverWiimotes();
//		for(int i=0; i<this.devices.size(); i++) {
//			this.devices.elementAt(i).setLED(i+1);
//		}
//	}
//	
//	private WiimoteWiigee(String btaddr) throws IOException {
//		Log.write("This is wiigee version "+version+" ("+releasedate+")");
//		this.devices = new Vector<Wiimote>();
//		this.devices.add(new Wiimote(btaddr));
//		for(int i=0; i<this.devices.size(); i++) {
//			this.devices.elementAt(i).setLED(i+1);
//		}
//	}
//	
//	public static WiimoteWiigee getInstance() throws IOException {
//		if(instance==null) {
//			instance=new WiimoteWiigee();
//			return instance;
//		} else {
//			return instance;
//		}
//	}
//
//	public static WiimoteWiigee getInstance(String btaddr) throws IOException {
//		if(instance==null) {
//			instance=new WiimoteWiigee(btaddr);
//			return instance;
//		} else {
//			return instance;
//		}
//	}
//	
//	/**
//	 * Returns an array of discovered wiimotes.
//	 * 
//	 * @return Array of discovered wiimotes or null if
//	 * none discoverd.
//	 */
//	public Wiimote[] getDevices() {
//		Wiimote[] out = new Wiimote[this.devices.size()];
//		for(int i=0; i<this.devices.size(); i++) {
//			out[i] = this.devices.elementAt(i);
//		}
//		return out;
//	}
//	
//	/**
//	 * Discover the wiimotes around the bluetooth host and
//	 * make them available public via getWiimotes method.
//	 * 
//	 * @return Array of discovered wiimotes.
//	 */
//	private Vector<Wiimote> discoverWiimotes() throws IOException {
//			WiimoteDeviceDiscovery deviceDiscovery = new WiimoteDeviceDiscovery(lock);
//			LocalDevice localDevice = LocalDevice.getLocalDevice();
//			Log.write("Your Computers Bluetooth MAC: "+localDevice.getBluetoothAddress());
//			
//			DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
//			
//			Log.write("Starting device inquiry...");
//			discoveryAgent.startInquiry(DiscoveryAgent.GIAC, deviceDiscovery);
//			
//			
//			try {
//				synchronized(lock){
//					lock.wait();
//				}
//		    } catch (InterruptedException e) {
//		    		Log.write("Problems during device discovery.");
//			        e.printStackTrace();
//			}
//
//		    Log.write("Device discovery completed!");			
//			return deviceDiscovery.getDiscoveredWiimotes();
//	}
//	
//	/**
//	 * Returns the number of wiimotes discovered.
//	 * 
//	 * @return Number of wiimotes discovered.
//	 */
//	public int getNumberOfDevices() {
//		return this.devices.size();
//	}
//	
//	/**
//	 * Sets the Trainbutton for all wiimotes;
//	 * 
//	 * @param b Button encoding, see static Wiimote values
//	 */
//	public void setTrainButton(int b) {
//		for(int i=0; i<this.devices.size(); i++) {
//			this.devices.elementAt(i).setTrainButton(b);
//		}
//	}
//	
//	/**
//	 * Sets the Recognitionbutton for all wiimotes;
//	 * 
//	 * @param b Button encoding, see static Wiimote values
//	 */
//	public void setRecognitionButton(int b) {
//		for(int i=0; i<this.devices.size(); i++) {
//			this.devices.elementAt(i).setRecognitionButton(b);
//		}
//	}
//	
//	/**
//	 * Sets the CloseGesturebutton for all wiimotes;
//	 * 
//	 * @param b Button encoding, see static Wiimote values
//	 */
//	public void setCloseGestureButton(int b) {
//		for(int i=0; i<this.devices.size(); i++) {
//			this.devices.elementAt(i).setCloseGestureButton(b);
//		}
//	}
//	
//	public void addDeviceListener(DeviceListener listener) {
//		if(this.devices.size()>0) {
//			this.devices.elementAt(0).addDeviceListener(listener);
//		}
//	}
//	
//	public void addGestureListener(GestureListener listener) {
//		if(this.devices.size()>0) {
//			this.devices.elementAt(0).addGestureListener(listener);
//		}
//	}
//	
//	public void addFilter(Filter filter) {
//		if(this.devices.size()>0) {
//			this.devices.elementAt(0).addFilter(filter);
//		}
//	}
//
//}
