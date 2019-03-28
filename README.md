# NativeToReactNative
It has migration of Native to react native app and bridging of UI components and methods.

**Getting Started**

1) clone repo by git clone 
2) Navigate to root dir of project and run npm install
3) Run server by running react-native start
4) If adb device is running then run react-native run-android or run directly through android studio.

**How the example is build**
 - Step 1- To migrate your existing android project to react native first create a subfolder inside the project folder with name "android" and copy everything from your android project to that folder.
 - Step 2- Create package.json and add below code to it
 
 {
  "name": "MyReactNativeApp",
  "version": "0.0.1",
  "private": true,
  "scripts": {
    "start": "yarn react-native start"
  }
}

Note- make sure you have yarn packager manager installed in your system.
 
 - Step 3- Add react native to your project "yarn add react-native --save". Now after running this command it will show you a warning saying there is peer dependency of react@X.Y, Where X.Y is the react version required.
 - Step 4- Next, Add react to your project "Yarn add react@X.Y --save"
 - Step 5- Now a add react-native to your app's build.gradle, like this
 dependencies {
    implementation 'com.android.support:appcompat-v7:27.1.1'
    ...
    implementation "com.facebook.react:react-native:+" // From node_modules (As facebook don't keep on updating maven repository so there could be a chance that last updated react-native version will come in place of "+", so to resolve that issue, replace + with the version of your react-native library)
 }
 - Step 6- Add maven repository to your project build.gradle the one you find in android/build.gradle
 allprojects {
    repositories {
        maven {
            // All of React Native (JS, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android" //make sure that the path is correct if not then you will face error saying failed to resolve - com.facebook.react:react-native:X.Y
        }
        ...
    }
    ...
}
 - Step 7- Add permission to android.manifest file. Below are the required permissions - 
 <uses-permission android:name="android.permission.INTERNET" />
 - step 8- To integrate developer's menu you have to add activity to the android.manifest file like this - 
 <activity android:name="com.facebook.react.devsupport.DevSettingsActivity" />
 </application>
 - Step 9- Create a file at the root dir of the project and name it as index.js and below code.
 import React from 'react';
import {AppRegistry, StyleSheet, Text, View} from 'react-native';

class HelloWorld extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>Hello, World</Text>
      </View>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('MyReactNativeApp', () => HelloWorld);

- Step 10- In the above code we register a component with name 'MyReactNativeApp' and which has to be same as activity we create in android.

- Step 11- To integrate error screen which has be at the top of all other activity, so for that add permission to manifest file as follows - <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>

- Step 12 - Create an activity file which will extend Activity implements DefaultHardwareBackBtnHandler. In create function of the activity check is there permission to add error screen above all other activity and send the result to the react instance manager from onActivityResult function. 

//code to check permission to show error screen

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                    }
        }
  }      

//To send result to react instance manager

@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted
                }
            }
        }
        mReactInstanceManager.onActivityResult( requestCode, resultCode, data );
    }
    
    
- Step 13 - In this step we will make our activity to run react code and make it as a main content view.

//Code to make this activity as main content view and run react code in this activity

public class MyReactActivity extends Activity implements DefaultHardwareBackBtnHandler {

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    private final int OVERLAY_PERMISSION_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
//                .setCurrentActivity(this)
                .setBundleAssetName("index.android.bundle")
//                .setJSMainModulePath("index")
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
        // The string here (e.g. "MyReactNativeApp") has to match
        // the string in AppRegistry.registerComponent() in index.js
        mReactRootView.startReactApplication(mReactInstanceManager, "MyReactNativeApp", null);

        setContentView(mReactRootView);
    }

@Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }
}

Note - if you are using android studio add the missing import files by alt + enter

- Step 14- Need to add theme of the activity to the manifest file because many react components are dependent on the below theme 

//theme code
<activity
  android:name=".MyReactActivity"
  android:label="@string/app_name"
  android:theme="@style/Theme.AppCompat.Light.NoActionBar">
</activity>

- Step 15- Pass lifecycle callback to the react instance manager and react root view.

//pass callback
 @Override
    protected void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onResume(this, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onDestroy();
        }
    }
    
- Step 16 - Add back button handler. This will give control to javascript to handle on back button press.

@Override
 public void onBackPressed() {
    if (mReactInstanceManager != null) {
        mReactInstanceManager.onBackPressed();
    } else {
        super.onBackPressed();
    }
}

- Step 17 - Hook up the dev menu by below code

//Hooked dev menu, now when user press ctrl+m then dev menu will appear.
@Override
public boolean onKeyUp(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
        mReactInstanceManager.showDevOptionsDialog();
        return true;
    }
    return super.onKeyUp(keyCode, event);
}
 
----------

**Author**

    Anoop Singh (codesingh)
    Email: anoop100singh@gmail.com
    Stack Overflow: codesingh(username)
    
----------    

**License**
    
Apache-2.0
