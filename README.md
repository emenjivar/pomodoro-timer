# Pomodoro scheduler 
[![Android Build](https://github.com/emenjivar/pomodoro-scheduler/actions/workflows/android_build.yml/badge.svg)](https://github.com/emenjivar/pomodoro-scheduler/actions/workflows/android_build.yml)

It is a simple countdown using [pomodoro technique](https://en.wikipedia.org/wiki/Pomodoro_Technique), developed in jetpack compose and kotlin, following my own [figma design](https://www.figma.com/file/Y6oJ51KCgG7vcZNQN8ZDu0/Pomodoro).

## How it works
Start a standar 25 minutes pomodoro using the play button, when the countdown is finished,  5 minutes break will automatically start.

![imagge](.github/images/pomodoro-countdown.gif)

## Before pushing
Before push your commits, you should execute the following ``gradle commands`` to verify that nothing has broken:
```shell
./gradlew lint
./gradlew ktlintCheck
./gradlew testDebugUnitTest
```

Or simply execute the next bash script which contains all the steps from the pipeline:
```shell
./before_push.sh
```

## Features
- [x] Pomodoro counter
- [ ] Schedule pomodoros using the calendar
- [ ] Sync app with google calendar
- [x] Unit testing
- [ ] Integration testing
- [ ] CI/CD pipeline
- [ ] Playstore uploaded
