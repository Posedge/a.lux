package org.ambientlux.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties
class ScheduleProperties {
    var lightSchedules: List<Schedule> = ArrayList()
}

class Schedule {
    lateinit var group: String
    var daysOfWeek: List<String> = ArrayList()
    var sequence: List<Keyframe> = ArrayList()
}

class Keyframe {
    lateinit var time: String
    lateinit var scene: String
    var interpolate: String? = null
}

