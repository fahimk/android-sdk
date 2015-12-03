./gradlew assembleDebug
adb shell pm clear com.yesgraph.android.sample
adb -d install -r ./sample/build/outputs/apk/sample-debug.apk
adb shell am start -n com.yesgraph.android.sample/com.yesgraph.android.sample.MainActivity