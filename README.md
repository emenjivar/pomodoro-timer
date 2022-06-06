# Pomodoro timer 
[![Android Build](https://github.com/emenjivar/pomodoro-scheduler/actions/workflows/android_build.yml/badge.svg)](https://github.com/emenjivar/pomodoro-scheduler/actions/workflows/android_build.yml)
<a href="https://ktlint.github.io/"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint"></a>
[![Made With Love](https://img.shields.io/badge/Made%20With-Love-orange.svg)](https://github.com/chetanraj/awesome-github-badges)


<a href='https://play.google.com/store/apps/details?id=com.emenjivar.pomodoro' target="_blank"><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height='80px'/></a>

It's a simple anti procrastination app that helps you to focus and get your tasks done.

# Table of contents
- [How it works?](#how-it-works)
- [Android tech stack](#android-tech-stack)
- [Would you like to contribute?](#would-you-like-to-contribute)
- [Extra resources](#extra-resources)

## How it works?
The app follow the [Pomodoro Technique](https://en.wikipedia.org/wiki/Pomodoro_Technique), breaking the time into two intervals, 25 min to work and 5 min to rest.

![preview](.github/images/preview.gif)

When you minimize the app, you can still controll the countdown from your device's notifications.

![notification preview](.github/images/notification.gif)

You can set the time of the intervals, enable sounds and vibration, keep screen on and change the color theme.

![settings](.github/images/settings.png)

## Android tech stack
- Written in kotlin
- [Jetpack compose](https://developer.android.com/jetpack/compose)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [Preference DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- [Koin](https://insert-koin.io/)
- Clean architecture
- [Ktlint](https://github.com/pinterest/ktlint), [lint](https://developer.android.com/studio/write/lint) and [detekt](https://github.com/detekt/detekt)

## Would you like to contribute?
Feel free to open an **issue** and tell me about your idea or create a **pull request**, just make sure to read before the [CONTRIBUTING](CONTRIBUTING.md) guide.

## Extra resources
- [Figma design](https://www.figma.com/file/Y6oJ51KCgG7vcZNQN8ZDu0/Pomodoro)
