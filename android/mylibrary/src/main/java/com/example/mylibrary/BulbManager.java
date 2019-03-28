package com.example.mylibrary;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class BulbManager extends SimpleViewManager<BulbView> {
    @Override
    public String getName() {
        return "BulbView";
    }

    @Override
    protected BulbView createViewInstance(ThemedReactContext reactContext) {

        return new BulbView(reactContext);

    }

    @ReactProp(name="isOn")
    public void setBulbStatus(BulbView bulbView, Boolean isOn) {
        bulbView.setIsOn(isOn);
    }

    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put(
                        "statusChange",
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", "onStatusChange")))
                .build();
    }
}
