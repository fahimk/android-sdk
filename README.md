YesGraph Android SDK is a sharing Android SDK that integrates with YesGraph. It presents user with a share sheet that can be used to share a message to user's friends to multiple sources, such as: Facebook, Twitter or user's contact book. [Read more about that on our blog.](http://blog.yesgraph.com/perfect-share-flow/)

Find detailed documentation about YesGraph on [yesgraph.com](https://docs.yesgraph.com)

## Requirements

The SDK is compatible with Android apps with Android SDK version 15 and above.

## Installation

The easiest way to integrate is with **JCenter**. Put this text into your **build.gradle** file:

```
repositories {
  jcenter()
}

dependencies {
  classpath 'com.yesgraph.android:yesgraph:1.0'
}
```

An alternate way to integrate is installing as Static Library

-Compile and build the project
-Copy the 'aar' file from the [module_home_directory]/build/outputs/yesgraph-[debug/release].aar
-Put the 'aar' file into your 'libs' folder
-Add these lines to your build.gradle file:

```
repositories {
   flatDir {
       dirs 'libs'
   }
}

dependencies {
   compile(name:'yesgraph-[debug|release]', ext:'aar')
}
```

You will also need to fill the manifest metadata for the Twitter and Facebook SDK.

```
<activity
android:name="com.facebook.FacebookActivity"
android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
android:theme="@android:style/Theme.Translucent.NoTitleBar"
android:label="@string/app_name" />

<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>

<meta-data android:name="io.fabric.ApiKey" android:value="<API_KEY_FOR_FABRIC>" />
```

Or integrate it manually.

# Example applications

There is an example application included in the repository, that display the share sheet when triggered.

## Getting Started with Example applications

Before you can use the example app, you need to configure the app with your **YesGraph client key**. Because YesGraph treats mobile devices as untrusted clients, first you need a trusted backend to generate client keys.

[Read more about connecting apps](https://docs.yesgraph.com/docs/connecting-apps#mobile-apps)
[Read more about creating client keys](https://docs.yesgraph.com/docs/create-client-keys)

1. If you haven't already, sign up for a YesGraph account (it takes seconds). Then go to YesGraph Dashboard: https://www.yesgraph.com/apps/.
2. Copy the live secret key on the bottom of the page to your trusted backend.
3. Call your trusted backend with user ID, to get the client key back (you can generate a random user ID, if user is not known, by using `Utility` class and `randomUserId` method.
4. Configure YesGraph Android SDK with received **client key** and **user ID**:

   ```
   private YesGraph yesGraphApplication;

   yesGraphApplication = (YesGraph) getApplicationContext();
   yesGraphApplication.configureWithUserId("<USER_ID>");
   yesGraphApplication.configureWithClientKey("<CLIENT KEY>");

   yesGraphApplication.setSource("Name", "+1 123 123 123", "Email");
   ```
5. Run the desired Example app.

## Tests

YesGraph Android SDK contains unit tests that can be executed in AndroidStudio.

License
======

YesGraph iOS SDK is released under **MIT** license.