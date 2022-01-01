# Pomodoro scheduler
## About this project
It is a simple countdown using [pomodoro technique](https://en.wikipedia.org/wiki/Pomodoro_Technique), developed in jetpack compose and kotlin, following my own [figma design](https://www.figma.com/file/Y6oJ51KCgG7vcZNQN8ZDu0/Pomodoro).

## How it works
When you press **start**, the pomodoro will start with a duration of 25 minutes, followed by a 5 minute rest. 

You can swipe between **day** and **night** pressing the **moon** button

![imagge](.github/images/pomodoro-countdown.gif)

## Before pushing
Before push your commits, you should execute the following ``gradle commands`` to verify that nothing has broken:
```bash
./gradlew lint
./gradlew ktlintCheck
./gradlew testDebugUnitTest
```

## Features
- [x] Pomodoro counter
- [ ] Schedule pomodoros using the calendar
- [ ] Sync app with google calendar
- [x] Unit testing
- [ ] Integration testing
- [ ] CI/CD pipeline
- [ ] Playstore uploaded
