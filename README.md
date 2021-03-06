# a.lux
Ambient Lux - f.lux for your smart light bulbs

a.lux is a simple application that adjusts your Philips Hue smart light bulbs to the time of day.
In particular, it allows you to avoid bright lights and cold colors in the evening, for healthier sleep.


### Planned features

- The user should set up 'schedules' by picking days and times at which they'd like a certain
configuration of the light bulbs (called a scene on the Hue).
The script should then interpolate linearly between them, like keyframes in an animation.
Outside the schedule, or when they are not turned on in the first place, it will not touch the lights.

- Interference by other apps/light switches etc. should be detected, and this should disable the script
for a while

- Add a REST API, to integrate with other smart home stuff

- Maybe add a feature to sync with sunrise and sunset, like f.lux does


### Build & Run

Build with gradle: `./gradlew build` and run on the JVM: `java -jar build/libs/*.jar`.


### FAQ

- Q: But f.lux already syncs with Philips Hue?

    A: I know. This is more of a run-24/7-on-your-raspberry-pi kind of thing.

- Q: Why Kotlin?

    A: I'd like to learn the language, also I love the modern featues, conciseness, and expressiveness :)
    

### License

MIT
