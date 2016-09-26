Log
===

public class LogUtil {
    
    /** TAG */
    private final static String TAG = "TestApp[" + Constants.VERSION + "]";
    
    private final static boolean IS_DEBUG = isDebug();
    
    public static boolean isDebuggable() {
        return IS_DEBUG;
    }
    
    public static void e(String tag, String msg) {
        Log.e(TAG, formatMessage(tag, msg));
    }
    
    private static String formatMessage(String tag, String msg) {
        return new StringBuilder().append("[").append(tag).append("] ").append(msg).toString();
    }
    
    private static boolean isDebug() {
        try {
            // SystemProperties.getString("log.com.test.app", null);
            Process process = Runtime.getRuntime().exec("getprop log.com.test.app");
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            return "DEBUG".equalsIgnoreCase(input.readLine());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "default log level");
        return false;
    }
}
