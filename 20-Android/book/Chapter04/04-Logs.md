Scripts Tips
===

### Android Logs
```
@echo off
: rd /s /q "./android_logs"

set filename=android_logs_%date:~0,4%%date:~5,2%%date:~8,2%_%time:~0,2%%time:~3,2%%time:~6,2%
echo %filename%
adb pull /data/log/android_logs/ ./%filename%/
```


```
@echo off

adb remount
adb shell rm -rf /data/log/android_logs/*
```

### App Data

```
: rd /s /q "./AppData"

set filename=AppName_%date:~0,4%%date:~5,2%%date:~8,2%_%time:~0,2%%time:~3,2%%time:~6,2%

adb remount
adb pull /data/data/com.android.app/ ./%filename%/
```

### Running Log

```
rd /s /q "./log.log"
adb logcat > ./log.log
```

### Dropbox

```
: @echo off
: rd /s /q "./dropbox"

set filename=dropbox_%date:~0,4%%date:~5,2%%date:~8,2%_%time:~0,2%%time:~3,2%%time:~6,2%
echo %filename%

adb remount
adb pull /data/system/dropbox/ ./%filename%/
```

```
adb remount
adb shell rm -rf /data/system/dropbox/*
```
