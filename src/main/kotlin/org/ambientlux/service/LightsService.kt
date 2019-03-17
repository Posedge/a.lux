package org.ambientlux.service

import org.ambientlux.adapters.LightsAdapter
import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LightsService @Autowired constructor(
        val lightsAdapter: LightsAdapter, val properties: ServiceProperties) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Called periodically.
     */
    public fun onUpdate() {
        properties.forEach(this::updateSchedule)
    }

    private fun updateSchedule(scheduleProperties: ScheduleProperties) {
        val groupName = scheduleProperties.group
        log.info("Updating schedule for '$groupName'...")

        val group = lightsAdapter.getLightsGroup(groupName)
        if (!shouldUpdateGroup(group)) return
        val desiredLight = getDesiredLightStatus(scheduleProperties)
        desiredLight.forEach { (lightId, status) -> lightsAdapter.setLight(lightId, status) }
    }

    private fun shouldUpdateGroup(group: LightsGroup): Boolean {
        if (!group.anyOn) {
            log.info("Lights are off, no action performed.")
            return false
        }
        // more complex logic (iterruptions etc) can be implemented here
        return true
    }

    private fun getDesiredLightStatus(scheduleProperties: ScheduleProperties): Map<String, LightStatus> {
        TODO("not implemented")
    }

}