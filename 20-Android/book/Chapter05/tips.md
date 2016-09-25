Tips
====


```java
String testStr = null;
try {
    JSONObject obj = new JSONObject(testStr);
} catch (JSONException e) {
    e.printStackTrace();
}

09-25 17:59:16.452 23162 23162 D AndroidRuntime: Shutting down VM
09-25 17:59:16.452 23162 23162 E AndroidRuntime: FATAL EXCEPTION: main
09-25 17:59:16.452 23162 23162 E AndroidRuntime: Process: com.example.just4u, PID: 23162
09-25 17:59:16.452 23162 23162 E AndroidRuntime: java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.just4u/com.example.just4u.MainActivity}: java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.length()' on a null object reference
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2787)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2854)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.app.ActivityThread.-wrap12(ActivityThread.java)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1562)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.os.Handler.dispatchMessage(Handler.java:102)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.os.Looper.loop(Looper.java:199)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.app.ActivityThread.main(ActivityThread.java:6506)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at java.lang.reflect.Method.invoke(Native Method)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:941)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:831)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.length()' on a null object reference
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at org.json.JSONTokener.nextCleanInternal(JSONTokener.java:116)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at org.json.JSONTokener.nextValue(JSONTokener.java:94)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at org.json.JSONObject.<init>(JSONObject.java:156)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at org.json.JSONObject.<init>(JSONObject.java:173)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at com.example.just4u.MainActivity.onCreate(MainActivity.java:25)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.app.Activity.performCreate(Activity.java:6787)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1123)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2740)


try {
    JSONObject obj = new JSONObject("");
    Log.i("test json", obj.toString());
} catch (JSONException e) {
    e.printStackTrace();
}
09-25 18:13:38.357 24798 24798 W System.err: org.json.JSONException: End of input at character 0 of 
09-25 18:13:38.357 24798 24798 W System.err: 	at org.json.JSONTokener.syntaxError(JSONTokener.java:449)
09-25 18:13:38.357 24798 24798 W System.err: 	at org.json.JSONTokener.nextValue(JSONTokener.java:97)
09-25 18:13:38.357 24798 24798 W System.err: 	at org.json.JSONObject.<init>(JSONObject.java:156)
09-25 18:13:38.357 24798 24798 W System.err: 	at org.json.JSONObject.<init>(JSONObject.java:173)
09-25 18:13:38.357 24798 24798 W System.err: 	at com.example.just4u.MainActivity.onCreate(MainActivity.java:25)
09-25 18:13:38.357 24798 24798 W System.err: 	at android.app.Activity.performCreate(Activity.java:6787)
09-25 18:13:38.358 24798 24798 W System.err: 	at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1123)
09-25 18:13:38.358 24798 24798 W System.err: 	at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2740)
09-25 18:13:38.358 24798 24798 W System.err: 	at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2854)
09-25 18:13:38.358 24798 24798 W System.err: 	at android.app.ActivityThread.-wrap12(ActivityThread.java)
09-25 18:13:38.358 24798 24798 W System.err: 	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1562)
09-25 18:13:38.358 24798 24798 W System.err: 	at android.os.Handler.dispatchMessage(Handler.java:102)
09-25 18:13:38.358 24798 24798 W System.err: 	at android.os.Looper.loop(Looper.java:199)
09-25 18:13:38.358 24798 24798 W System.err: 	at android.app.ActivityThread.main(ActivityThread.java:6506)
```
