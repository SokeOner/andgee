
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import control.Andgee;
import event.GestureEvent;
import event.GestureListener;
import event.StateEvent;

/**
 * @author Maarten 'MrSnowflake' Krijn
 */
public class AndgeeTest extends Activity {
    protected static final String TAG = "AndgeeTest";

        /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.andgee);
       
           
        mSensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
            lblStatus = (TextView)findViewById(R.id.status);
            lblStartKey = (TextView)findViewById(R.id.lblStartKey);
            lblStartKey.setText("Hold Space to recognize");
            lblStopKey = (TextView)findViewById(R.id.lblStopKey);
            lblStopKey.setText("Press enter to stop");
            lblLearnKey = (TextView)findViewById(R.id.lblLearnKey);
            lblLearnKey.setText("Hold T to learn");
           
            Button reg = (Button) findViewById(R.id.regonize);
            reg.setOnTouchListener(new OnTouchListener(){

              @Override
              public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                  lblStatus.setText("Recognizing");
                  andgee.getDevice().fireButtonPressedEvent(START_KEY);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                  lblStatus.setText("");
                  andgee.getDevice().fireButtonReleasedEvent();
                }
                return false;
              }
              
            });
            
          Button learn = (Button) findViewById(R.id.learn);
          learn.setOnTouchListener(new OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
              if(event.getAction() == MotionEvent.ACTION_DOWN){
                lblStatus.setText("Learning");
                andgee.getDevice().fireButtonPressedEvent(LEARN_KEY);
              }else if(event.getAction() == MotionEvent.ACTION_UP){
                lblStatus.setText("");
                andgee.getDevice().fireButtonPressedEvent(STOP_KEY);
//                andgee.getDevice().fireButtonReleasedEvent();
              }
              return false;
            }
            
          });
        
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
                            if(event.getState() == event.STATE_LEARNING){
                                Log.i(TAG, "StateReceived learning");
                            }else if(event.getState() == event.STATE_RECOGNIZING){
                                Log.i(TAG, "StateReceived Recognizing");
                            }else{
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
            andgee.getDevice().fireButtonPressedEvent(event.getKeyCode());
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
                andgee.getDevice().fireButtonReleasedEvent();
                lblStatus.setText("");
                return true;
        }
                return super.onKeyUp(keyCode, event);
        }

    
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(andgee.getDevice(), 
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME   );
        try {
            andgee.getDevice().enableAccelerationSensors();
        } catch (IOException e) {
            Log.e(getClass().toString(), e.getMessage(), e);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(andgee.getDevice());
        try {
            andgee.getDevice().disableAccelerationSensors();
        } catch (Exception e) {
            Log.e(getClass().toString(), e.getMessage(), e);
        }
    }
    
    private TextView lblStatus;
    private TextView lblStartKey;
    private TextView lblStopKey;
    private TextView lblLearnKey;
   
    private SensorManager mSensorManager;
    private Andgee andgee = Andgee.getInstance();
}

