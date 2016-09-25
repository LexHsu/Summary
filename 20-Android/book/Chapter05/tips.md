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

09-25 17:59:16.452 23162 23162 E AndroidRuntime: Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.length()' on a null object reference
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at org.json.JSONTokener.nextCleanInternal(JSONTokener.java:116)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at org.json.JSONTokener.nextValue(JSONTokener.java:94)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at org.json.JSONObject.<init>(JSONObject.java:156)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at org.json.JSONObject.<init>(JSONObject.java:173)
09-25 17:59:16.452 23162 23162 E AndroidRuntime: 	at com.example.just4u.MainActivity.onCreate(MainActivity.java:25)

try {
    JSONObject obj = new JSONObject("");
} catch (JSONException e) {
    e.printStackTrace();
}
09-25 18:13:38.357 24798 24798 W System.err: org.json.JSONException: End of input at character 0 of 
09-25 18:13:38.357 24798 24798 W System.err: 	at org.json.JSONTokener.syntaxError(JSONTokener.java:449)
09-25 18:13:38.357 24798 24798 W System.err: 	at org.json.JSONTokener.nextValue(JSONTokener.java:97)
09-25 18:13:38.357 24798 24798 W System.err: 	at org.json.JSONObject.<init>(JSONObject.java:156)
09-25 18:13:38.357 24798 24798 W System.err: 	at org.json.JSONObject.<init>(JSONObject.java:173)
09-25 18:13:38.357 24798 24798 W System.err: 	at com.example.just4u.MainActivity.onCreate(MainActivity.java:25)
```
