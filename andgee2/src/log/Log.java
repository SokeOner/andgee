package log;
/**
 * To be used to replace System.out.println for portability
 * 
 * @author liangj01
 *
 */
public class Log {
    
    public static interface Logger{
        public void println(String... msg);
        public void print(String msg);
    }
    
    
    private static Logger logger = null;
    
    /**
     * Sets a system wide logger
     * 
     * This is not thread safe
     * 
     * @param logger
     */
    public static void setLogger(Logger logger) {
        Log.logger = logger;
    }
    
    /**
     * if {@link #setLogger(Logger)} is not called, prints to System.out,
     * otherwise prints to logger
     * 
     * @param msg debug message
     */
    public static void println(String... msg){
        if(logger != null){
            logger.println(msg);
        }else if(msg != null && msg.length > 0){
            System.out.println(msg[0]);
        }
    }
    /**
     * if {@link #setLogger(Logger)} is not called, prints to System.out,
     * otherwise prints to logger
     * 
     * @param msg
     */
    public static void print(String msg){
        if(logger != null){
            logger.print(msg);
        }else{
            System.out.print(msg);
        }
    }
    
}