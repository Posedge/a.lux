package org.ambientlux.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("light_schedules")
class ServiceProperties: ArrayList<ScheduleProperties>()

class ScheduleProperties {
    lateinit var group: String
    var daysOfWeek: List<String> = ArrayList()
    var sequence: List<SequenceStepProperties> = ArrayList()
}

class SequenceStepProperties {
    lateinit var time: String
    lateinit var scene: String
    var interpolate: String? = null
}

