package com.example.mylibrary;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.ViewManager;

import android.app.Activity;

import java.util.List;

import javax.annotation.Nonnull;

public class Bulb extends ReactContextBaseJavaModule implements ReactPackage {
    private static Boolean isOn = false;

    public Bulb(ReactApplicationContext reactContext) {
        super(reactContext);
    }

//    public Bulb(Activity reactContext) {
//        super(reactContext);
//    }
//    private Application application
//
//    public Bulb(ReactApplicationContext reactContext) {
//        super(reactContext);
//    }

//    public Bulb(@Nonnull MyReactActivity reactContext) {
//        super(reactContext);
//    }
//
//    public Bulb(com.example.anoopsingh.nativereactnative.MyReactActivity myReactActivity) {
//        super();
//    }
//
//    public Bulb(com.example.anoopsingh.nativereactnative.MyReactActivity myReactActivity) {
//        super();
//    }

    @ReactMethod
    public void getStatus(
            Callback successCallback) {
        successCallback.invoke(null, isOn);

    }

    @ReactMethod
    public void turnOn() {
        isOn = true;
        System.out.println("Bulb is turn ON");
    }
    @ReactMethod
    public void turnOff() {
        isOn = false;
        System.out.println("Bulb is turn OFF");
    }

    @Override
    public String getName() {
        return "Bulb";
    }

    @Nonnull
    @Override
    public List<NativeModule> createNativeModules(@Nonnull ReactApplicationContext reactContext) {
        return null;
    }

    @Nonnull
    @Override
    public List<ViewManager> createViewManagers(@Nonnull ReactApplicationContext reactContext) {
        return null;
    }
}
