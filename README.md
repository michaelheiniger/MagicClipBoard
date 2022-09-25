# MagicClipboard

Share your clipboard across all your devices !

## Status

-
Main: ![Build](https://github.com/michaelheiniger/MagicClipBoard/actions/workflows/build.yml/badge.svg?branch=main)

-

Develop![Build](https://github.com/michaelheiniger/MagicClipBoard/actions/workflows/build.yml/badge.svg?branch=develop)

Run E2E tests:

- install Android emulator (doesn't work with real devices. Would need to adapt IP address used to connect to Firebase emulator
- install firebase CLI (https://firebase.google.com/docs/cli#install_the_firebase_cli)
- cd into MagicClipBoard folder
- run firebase emulator (Auth and Realtime DB) with test data:
  firebase emulators:start --import=app/src/androidTest/firebase-export
- run tests either in Android Studio (e.g. MagicClipboardE2eTest) or using commandline:
  ./gradlew connectedDebugAndroidTest (for debug variant)

## Credits:

- Ideas/pattern taken from https://github.com/android/compose-samples/tree/main/JetNews