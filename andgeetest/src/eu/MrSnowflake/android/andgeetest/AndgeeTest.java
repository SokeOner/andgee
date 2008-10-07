package eu.MrSnowflake.android.andgeetest;

import org.openintents.hardware.SensorManagerSimulator;
import org.openintents.provider.Hardware;

import control.Andgee;
import event.GestureEvent;
import event.GestureListener;
import event.StateEvent;
import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

/** 
 * @author Maarten 'MrSnowflake' Krijn
 */
public class AndgeeTest extends Activity {
    protected static final String TAG = "AndgeeTest";
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
	    if (isEmulator(this)) {
	        // Android sensor Manager
	    	sensorMgr = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
	    } else {
	    	// OpenIntents Sensor Emulator!
			// Before calling any of the Simulator data, 
			// the Content resolver has to be set !! 
			Hardware.mContentResolver = getContentResolver(); 
			
			// Link sensor manager to OpenIntents Sensor simulator 
			sensorMgr = (SensorManager) new SensorManagerSimulator((SensorManager)
					getSystemService(SENSOR_SERVICE));
			
			SensorManagerSimulator.connectSimulator(); 
	    }
	    
	    lblStatus = (TextView)findViewById(R.id.status);
	    lblStartKey = (TextView)findViewById(R.id.lblStartKey);
	    lblStartKey.setText("Hold Space to recognize");
	    lblStopKey = (TextView)findViewById(R.id.lblStopKey);
	    lblStopKey.setText("Press enter to stop");
	    lblLearnKey = (TextView)findViewById(R.id.lblLearnKey);
	    lblLearnKey.setText("Hold T to learn");
	    
	    andgee = Andgee.getInstance(sensorMgr);
        
        andgee.setRecognitionButton(START_KEY);
        andgee.setCloseGestureButton(STOP_KEY);
        andgee.setTrainButton(LEARN_KEY);
        
        andgee.addGestureListener(new GestureListener() {
			@Override
			public void gestureReceived(GestureEvent event) {
				Log.i(TAG, "GestureReceived "+event.getId());
				lblStatus.setText("Recognized: "+event.getId()+" Probability: "+event.getProbability());
			}

			@Override
			public void stateReceived(StateEvent event) {
				switch (event.getState()) {
				case StateEvent.STATE_LEARNING:
					Log.i(TAG, "StateReceived learning");
					break;
				case StateEvent.STATE_RECOGNIZING:
					Log.i(TAG, "StateReceived Recognizing");
					break;
				default:
					Log.i(TAG, "StateReceived Unknown "+event.getState());
				}
			}
        });
    }
    
    private static final int LEARN_KEY = KeyEvent.KEYCODE_T;
    private static final int START_KEY = KeyEvent.KEYCODE_SPACE;
    private static final int STOP_KEY = KeyEvent.KEYCODE_ENTER;

	public static final String EMULATOR_IMEI = "000000000000000";
	
	/**
	 * Returns whether the context is running on the android emulator. 
	 * @param ctx The calling context.
	 * @return True: Running on emulator. False: Running on a real device
	 */
	public static boolean isEmulator(Context ctx) {
		TelephonyManager telephonyMgr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);        
		//to be deleted in final
		// always use simulator when in emulator 
		return !telephonyMgr.getDeviceId().equals(EMULATOR_IMEI);
	}
	
    /**
     * TODO Zorg voor juiste return waardes van Andgee.onKeyDown() en Up()
     */
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == STOP_KEY)
    		lblStatus.setText("Stop");
    	else if (keyCode == LEARN_KEY)
    		lblStatus.setText("Learning");
    	else if (keyCode == START_KEY)
    		lblStatus.setText("Recognizing");
		
    	if (keyCode == STOP_KEY || keyCode == LEARN_KEY || keyCode == START_KEY) {
    		andgee.onKeyDown(event);
    		return true;
    	}

		return super.onKeyDown(keyCode, event);
	}

    /**
     * TODO Zorg voor juiste return waardes
     */
    @Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if (keyCode == STOP_KEY || keyCode == LEARN_KEY || keyCode == START_KEY) {
    		andgee.onKeyUp(event);
        	lblStatus.setText("");
    		return true;
    	}
		return super.onKeyUp(keyCode, event);
	}

    private TextView lblStatus;
    private TextView lblStartKey;
    private TextView lblStopKey;
    private TextView lblLearnKey;
    
	private SensorManager sensorMgr;
    private Andgee andgee; 
}