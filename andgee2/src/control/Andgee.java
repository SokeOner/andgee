package control;

import android.util.Log;
import device.AndroidDevice;
import event.GestureListener;
import filter.Filter;

public class Andgee{
    private static final String TAG = "Andgee";
    
    private static Andgee instance;
    private static String version = ".alpha";
    private static String releasedate = "TBD";
    
    private AndroidDevice device;
    
    private Andgee() {
            device = new AndroidDevice();
    }
    
    public static synchronized Andgee getInstance() {
            Log.i(TAG, "This is Andgee version "+version+" ("+releasedate+")");
            Log.i(TAG, "This is an Android adaptation of Wiigee (http://wiigee.sourceforge.net/)");
            Log.i(TAG, "So many thanks to the Wiigee team for their awsome recognition lib!");
            if(instance == null) {
                    instance = new Andgee();
                    return instance;
            } else {
                    return instance;
            }
    }
    
    public void addGestureListener(GestureListener listener) {
            device.addGestureListener(listener);
    }
    
    public void addFilter(Filter filter) {
            device.addFilter(filter);
    }
    

    public AndroidDevice getDevice() {
        return device;
    }
    
            
    /**
     * Sets the Trainbutton for all wiimotes;
     * 
     * @param b Button encoding, see static Wiimote values
     */
    public void setTrainButton(int b) {
            device.setTrainButton(b);
    }
    
    /**
     * Sets the Recognitionbutton for all wiimotes;
     * 
     * @param b Button encoding, see static Wiimote values
     */
    public void setRecognitionButton(int b) {
            device.setRecognitionButton(b);
    }
    
    /**
     * Sets the CloseGesturebutton for all wiimotes;
     * 
     * @param b Button encoding, see static Wiimote values
     */
    public void setCloseGestureButton(int b) {
            device.setCloseGestureButton(b);
    }

}