# Android Libbitcoin

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Build Terminal](#build-terminal)
- [Conclusion](#Conclusion)

## Prerequisites

The guide assumes that you are using Linux or Mac for development.

- Install [Android Studio](https://developer.android.com/studio/index.html)
- Install [Java JDK v8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Below are the Android tools needed:
- Install ANDROID_BUILD_TOOLS="26.0.3"
- Install ANDROID_COMPILE_SDK="26"
- Install Linux Android NDK = [r16b](https://dl.google.com/android/repository/android-ndk-r16b-linux-x86_64.zip)
- Install MAC Android NDK = [r16b](https://dl.google.com/android/repository/android-ndk-r16b-darwin-x86_64.zip)

## Getting Started

The build process requires several pieces of software to be installed on the
host system:

* autoconf
* automake
* cmake
* git
* libtool
* pkgconfig
* protobuf
* clang
* astyle

The following command will install the necessary dependencies on Ubuntu:

    apt-get install patch autoconf automake cmake git libtool pkg-config protobuf-compiler clang astyle

The following command will install the necessary dependencies on MAC

    brew install autoconf automake cmake git libtool pkgconfig protobuf astyle


## Build Terminal

First export the following variables.

    export ANDROID_HOME=your-android-sdk-location
    export ANDROID_NDK_HOME=your-android-sdk-location
    export PATH=$PATH:/sdk-tools/platform-tools/
    export PATH=$PATH:$ANDROID_NDK_HOME

Change to the BindingsExmpl dir

    cd ./BindingsExmpl
    
Run the following gradle commands.

  * Run the following commands to verify everything is setup
    - `./gradlew --version`
    - `./gradlew task`
  * The following are the **HEART** of the build steps. This will run the commands that will output the final .aar file.
    - `./gradlew assemble`
    - `./gradlew libbitcoinbindings:libAAR`
  

## Release For Android Apps

* Keep build artifacts Forever
  * [Pipeline Page](https://git.coinninja.net/cn/android-libbitcoin/-/jobs/2704)
  * In the artifacts page select **keep**
  * Right Click **Download** button and **copy link address**
  * This is the url that the android [app/build.gradle](https://git.coinninja.net/cn/coin-keeper-android/blob/develop/app/build.gradle) needs as a dependency.
  * Update the `android_libbitcoin_download` variable to be the new release dependency.
  
  
## CI / Build for maven deploy

These environment vars are nessisary for the deployment to nexus

```
CI_JOB_ID=19731
CN_LIB_BITCOIN_VERSION=1.0
CN_NEXUS_UID=gitlab-android
CN_NEXUS_PWD=****
```

## Conclusion 

At the end of the build process you should see a new file named libraryAAR. Is this new dir you should see libbitcoinbindings-release.aar.

