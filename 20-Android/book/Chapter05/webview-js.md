Android本地方法与JS方法交互
===

### 一、约定Java本地接口

```java
/**
 * SuppressLint("SetJavaScriptEnabled")解决高版本JS无法调用Android内的方法
 */
@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WebView myWebView = (WebView) findViewById(R.id.myWebView);
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new JsInteration(), "control");
        myWebView.setWebChromeClient(new WebChromeClient() {});
        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                testMethod(myWebView);
            }
            
        });
        myWebView.loadUrl("file:///android_asset/js_java_interaction.html");
    }

    private void testMethod(WebView webView) {
        String call = "javascript:sayHello()";
        
        call = "javascript:alertMessage(\"" + "content" + "\")";
        
        call = "javascript:toastMessage(\"" + "content" + "\")";
        
        call = "javascript:sumToJava(1,2)";
        webView.loadUrl(call);
        
    }

    public class JsInteration {
        
        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
        
        @JavascriptInterface
        public void onSumResult(int result) {
            Log.i(TAG, "onSumResult result=" + result);
        }
    }

}
```

### 二、网页代码及JS接口

```javascript
<html>
<script type="text/javascript">
    function sayHello() {
        alert("Hello")
    }

    function alertMessage(message) {
        alert(message)
    }

    function toastMessage(message) {
        window.control.toastMessage(message)
    }

    function sumToJava(number1, number2){
       window.control.onSumResult(number1 + number2)
    }
</script>
Java-Javascript Interaction In Android
</html>
```
### 三、在manifest中加入网络权限

```
<uses-permission android:name="android.permission.INTERNET" />
```

### 四、JS调用Java

调用格式为：

```
window.jsInterfaceName.methodName(parameterValues) 
```

在MainActivity中，约定注入接口名称(jsInterfaceName）为control。

```javascript
function toastMessage(message) {
  window.control.toastMessage(message)
}

function sumToJava(number1, number2){
   window.control.onSumResult(number1 + number2)
}
```

### 五、Java调用JS

webView调用js的基本格式为：
```
webView.loadUrl(“javascript:methodName(parameterValues)”);
或
webView.loadUrl(url);
```

- 调用js无参无返回值函数

```java
String call = "javascript:sayHello()";
webView.loadUrl(call);
```

- 调用js有参无返回值函数

```java
String call = "javascript:alertMessage(\"" + "content" + "\")";
webView.loadUrl(call);
```

注意对于字符串作为参数值需要进行转义双引号。

- 调用js有参数有返回值的函数

Android在4.4之前并没有提供直接调用js函数并获取值的方法，所以在此之前，常用的思路是 java调用js方法，js方法执行完毕，再次调用java代码将值返回。

```
1.Java调用js代码

String call = "javascript:sumToJava(1,2)";
webView.loadUrl(call);

2.js函数处理，并将结果通过调用java方法返回

function sumToJava(number1, number2){
       window.control.onSumResult(number1 + number2)
}

3.Java在回调方法中获取js函数返回值

@JavascriptInterface
public void onSumResult(int result) {
  Log.i(LOGTAG, "onSumResult result=" + result);
}
```


Android 4.4之后使用evaluateJavascript即可。这里展示一个简单的交互示例 具有返回值的js方法

```
function getGreetings() {
      return 1;
}


java通过evaluateJavascript方法调用

private void testEvaluateJavascript(WebView webView) {
  webView.evaluateJavascript("getGreetings()", new ValueCallback<String>() {

  @Override
  public void onReceiveValue(String value) {
      Log.i(LOGTAG, "onReceiveValue value=" + value);
  }});
}

输出结果
I/MainActivity( 1432): onReceiveValue value=1
```

上面限定了结果返回结果为String，对于简单的类型会尝试转换成字符串返回，对于复杂的数据类型，建议以字符串形式的json返回。
evaluateJavascript方法必须在UI线程（主线程）调用，因此onReceiveValue也执行在主线程。


### 六、问题汇总

- Alert无法弹出

```
未设置WebChromeClient
myWebView.setWebChromeClient(new WebChromeClient() {});
```

1. setWebViewClient主要处理解析，渲染网页等浏览器核心功能。
2. setWebChromeClient主要用于辅助WebView处理Javascript的对话框(onJsAlert)，网站图标(onReceivedIcon)，网站title(onReceivedTitle)，加载进度(onProgressChanged)等。

- Uncaught ReferenceError: functionName is not defined

网页的js代码没有加载完成，就调用了js方法。解决方法是在网页加载完成之后调用js方法

```java
myWebView.setWebViewClient(new WebViewClient() {

  @Override
  public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
      //在这里执行你想调用的js函数
  }
  
});
```

- Uncaught TypeError: Object [object Object] has no method

安全限制问题

如果只在4.2版本以上的机器出问题，那么就是系统处于安全限制的问题了。Android文档这样说的

Caution: If you’ve set your targetSdkVersion to 17 or higher, you must add the @JavascriptInterface annotation to any method that you want available your web page code (the method must also be public). If you do not provide the annotation, then the method will not accessible by your web page when running on Android 4.2 or higher.

如果你的程序目标平台是17或者是更高，你必须要在暴露给网页可调用的方法（这个方法必须是公开的）加上@JavascriptInterface注释。如果你不这样做的话，在4.2以以后的平台上，网页无法访问到你的方法。

解决方法：将targetSdkVersion设置成17或更高，引入@JavascriptInterface注释

- 代码混淆问题

如果在没有混淆的版本运行正常，在混淆后的版本的代码运行错误，并提示Uncaught TypeError: Object [object Object] has no method，那就是没有做混淆例外处理。 处理如下：

```
keepattributes *Annotation*
keepattributes JavascriptInterface
-keep class com.example.javajsinteractiondemo$JsInteration {
    *;
}
```

- All WebView methods must be called on the same thread

过滤日志曾发现过这个问题。

```
E/StrictMode( 1546): java.lang.Throwable: A WebView method was called on thread 'JavaBridge'. All WebView methods must be called on the same thread. (Expected Looper Looper (main, tid 1) {528712d4} called on Looper (JavaBridge, tid 121) {52b6678c}, FYI main Looper is Looper (main, tid 1) {528712d4})
E/StrictMode( 1546):   at android.webkit.WebView.checkThread(WebView.java:2063)
E/StrictMode( 1546):   at android.webkit.WebView.loadUrl(WebView.java:794)
E/StrictMode( 1546):   at com.xxx.xxxx.xxxx.xxxx.xxxxxxx$JavaScriptInterface.onCanGoBackResult(xxxx.java:96)
E/StrictMode( 1546):   at com.android.org.chromium.base.SystemMessageHandler.nativeDoRunLoopOnce(Native Method)
E/StrictMode( 1546):   at com.android.org.chromium.base.SystemMessageHandler.handleMessage(SystemMessageHandler.java:27)
E/StrictMode( 1546):   at android.os.Handler.dispatchMessage(Handler.java:102)
E/StrictMode( 1546):   at android.os.Looper.loop(Looper.java:136)
E/StrictMode( 1546):   at android.os.HandlerThread.run(HandlerThread.java:61)
```

在js调用后的Java回调线程并不是主线程。如打印日志可验证

```
ThreadInfo=Thread[WebViewCoreThread,5,main]
```

解决上述的异常，将webview操作放在主线程中即可。

```
webView.post(new Runnable() {
    @Override
    public void run() {
        webView.loadUrl(YOUR_URL).
    }
});
```


