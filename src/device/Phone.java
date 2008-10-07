/*
 * andgee - accelerometer based gesture recognition
 * Copyright (C) 2007, 2008 Benjamin Poppinga
 * Copyright (C) 2008 Maarten Krijn 
 * 
 * Developed at University of Oldenburg
 * Contact: benjamin.poppinga@informatik.uni-oldenburg.de
 *
 * This file is part of wiigee (v1.1).
 * 
 * This file got adapted to work with Android devices by
 * Maarten Krijn (mrsnowflake@gmail.com)
 *
 * andgee is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package device;

import java.util.EventObject;
import java.util.Vector;

import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;

import logic.AccelerationStreamAnalyzer;
import event.AccelerationEvent;
import event.ButtonPressedEvent;
import event.GestureListener;
import event.ButtonReleasedEvent;
import event.MotionStartEvent;
import event.MotionStopEvent;
import filter.DirectionalEquivalenceFilter;
import filter.Filter;
import filter.IdleStateFilter;
import filter.MotionDetectFilter;

/**
* @author Benjamin 'BePo' Poppinga
* @author Maarten 'MrSnowflake' Krijn
* 
*/
public class Phone implements SensorListener {
	private static final String TAG = "Phone";

	public static final int MOTION = 0;

	// Filters, can filter the data stream
	Vector<Filter> filters = new Vector<Filter>();
	
	// Listeners, receive generated events
	Vector<event.SensorListener> wiimotelistener = new Vector<event.SensorListener>();
	AccelerationStreamAnalyzer analyzer = new AccelerationStreamAnalyzer();

	private boolean accelerationenabled;
	private SensorManager sensorMgr;

	private int recognitionbutton;
	private int trainbutton;
	private int closegesturebutton;
	
	public Phone(SensorManager sensorMgr) {
		// 'Calibrate'
		this.x0 = 0;
		this.y0 = -SensorManager.STANDARD_GRAVITY;
		this.z0 = 0;
		this.x1 = SensorManager.STANDARD_GRAVITY;
		this.y1 = 0;
		this.z1 = SensorManager.STANDARD_GRAVITY;
		
		this.sensorMgr = sensorMgr;
		this.enableAccelerationSensors();
		this.addFilter(new IdleStateFilter());
		this.addFilter(new MotionDetectFilter(this));
		this.addFilter(new DirectionalEquivalenceFilter());
		this.addWiimoteListener(this.analyzer);
		if (sensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER))
			Log.e(TAG, "Could not register SensorListener");
	}

	private void enableAccelerationSensors() {
		this.accelerationenabled = true;
	}

	/**
	 * Adds a Filter for processing the acceleration values.
	 * @param filter The Filter instance.
	 */
	public void addFilter(Filter filter) {
		this.filters.add(filter);
		Log.i(TAG, "Filter added...");
	}
	
	/**
	 * Resets all the filters, which are resetable.
	 * Sometimes they have to be resettet if a new gesture starts.
	 */
	public void resetFilters() {
		for(int i=0; i<this.filters.size(); i++) {
			this.filters.elementAt(i).reset();
		}
	}
	public int getRecognitionButton() {
		return this.recognitionbutton;
	}
	
	public void setRecognitionButton(int b) {
		this.recognitionbutton=b;
	}
	
	public int getTrainButton() {
		return this.trainbutton;
	}
	
	public void setTrainButton(int b) {
		this.trainbutton=b;
	}
	
	public int getCloseGestureButton() {
		return this.closegesturebutton;
	}
	
	public void setCloseGestureButton(int b) {
		this.closegesturebutton=b;
	}
	
	/**
	 * Adds an WiimoteListener to the wiimote. Everytime an action
	 * on the wiimote is performed the WiimoteListener would receive
	 * an event of this action.
	 * 
	 */
	public void addWiimoteListener(event.SensorListener listener) {
		this.wiimotelistener.add(listener);
		Log.i(TAG, "WiimoteListener added...");
	}
	
	/**
	 * Adds a GestureListener to the wiimote. Everytime a gesture
	 * is performed the GestureListener would receive an event of
	 * this gesture.
	 */
	public void addGestureListener(GestureListener listener) {
		this.analyzer.addGestureListener(listener);
		Log.i(TAG, "GestureListener added...");
	}
	
	public AccelerationStreamAnalyzer getAccelerationStreamAnalyzer() {
		return this.analyzer;
	}
	
	public boolean accelerationEnabled() {
		return this.accelerationenabled;
	}
	
	/** Fires an acceleration event.
	 * @param x
	 * 		Acceleration in x direction
	 * @param y
	 * 		Acceleration in y direction
	 * @param z
	 * 		Acceleration in z direction
	 */
	public void fireAccelerationEvent(double[] vector) {
		for(int i=0; i<this.filters.size(); i++) {
			vector = this.filters.get(i).filter(vector);
			// cannot return here if null, because of time-dependent filters
		}
		
		// don't need to create an event if filtered away
		if(vector!=null) {
				// 	calculate the absolute value for the accelerationevent
			double absvalue = Math.sqrt((vector[0]*vector[0])+
					(vector[1]*vector[1])+(vector[2]*vector[2]));
		
			AccelerationEvent w = new AccelerationEvent(this,
					vector[0], vector[1], vector[2], absvalue);
			for(int i=0; i<this.wiimotelistener.size(); i++) {
				this.wiimotelistener.get(i).accelerationReceived(w);
			}
		}

	} // fireaccelerationevent

	/**
	 * Fires a motion start event.
	 */
	public void fireMotionStartEvent() {
		MotionStartEvent w = new MotionStartEvent(this);
		for(int i=0; i<this.wiimotelistener.size(); i++) {
			this.wiimotelistener.get(i).motionStartReceived(w);
		}
	}
	
	/**
	 * Fires a motion stop event.
	 */
	public void fireMotionStopEvent() {
		MotionStopEvent w = new MotionStopEvent(this);
		for(int i=0; i<this.wiimotelistener.size(); i++) {
			this.wiimotelistener.get(i).motionStopReceived(w);
		}
	}

	@Override
	public void onAccuracyChanged(int arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(int sensor, float[] values) {
		double x, y, z;
		float xraw, yraw, zraw;

		// if the wiimote is sending acceleration data...
		if (this.accelerationEnabled() && (sensor & SensorManager.SENSOR_ACCELEROMETER) != 0) {
			/*
			 * calculation of acceleration vectors starts here. further
			 * information about normation exist in the public papers or
			 * the various www-sources.
			 * 
			 */
			xraw = values[SensorManager.DATA_X];
			yraw = values[SensorManager.DATA_Y];
			zraw = values[SensorManager.DATA_Z];

			x = (double) (xraw - x0) / (double) (x1 - x0);
			y = (double) (yraw - y0) / (double) (y1 - y0);
			z = (double) (zraw - z0) / (double) (z1 - z0);

			// try to fire event, there could be filters added to the
			// wiimote class which may prevents from firing.
			this.fireAccelerationEvent(new double[] {x, y, z});
		}		
	}
	
	public void onKeyDown(KeyEvent event) {
		if (!(this.lastevent instanceof ButtonPressedEvent)) {
			this.fireButtonPressedEvent(event.getKeyCode());
			this.lastevent = new ButtonPressedEvent(this, event.getKeyCode());
		}
	}

	public void onKeyUp(KeyEvent event) {
		if (!(this.lastevent instanceof ButtonReleasedEvent)) {
			this.fireButtonReleasedEvent();
			this.lastevent = new ButtonReleasedEvent(this);
		}
	}

	/** Fires a button pressed event.
	 * @param button
	 * 		Integer value of the pressed button.
	 */
	public void fireButtonPressedEvent(int button) {
		ButtonPressedEvent w = new ButtonPressedEvent(this, button);
		for(int i=0; i < this.wiimotelistener.size(); i++) {
			this.wiimotelistener.get(i).buttonPressReceived(w);
		}
		
		if(w.isRecognitionInitEvent() || w.isTrainInitEvent()) {
			this.resetFilters();
		}
	}
	
	/** Fires a button released event.
	 */
	public void fireButtonReleasedEvent() {
		ButtonReleasedEvent w = new ButtonReleasedEvent(this);
		for(int i=0; i<this.wiimotelistener.size(); i++) {
			this.wiimotelistener.get(i).buttonReleaseReceived(w);
		}
	}
	
	private double x0, x1, y0, y1, z0, z1;
	private EventObject lastevent;
}
