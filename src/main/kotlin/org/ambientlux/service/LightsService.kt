package org.ambientlux.service

import org.ambientlux.adapters.LightsAdapter
import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup
import org.ambientlux.service.domain.Scene
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.*

@Service
class LightsService @Autowired constructor(
        private val lightsAdapter: LightsAdapter, private val properties: ScheduleList) {

    private val log = LoggerFactory.getLogger(javaClass)

    // Read all scenes
    private val scenes: Map<String, Scene> = lightsAdapter.getScenes()

    /**
     * Will be called periodically.
     */
    public fun onUpdate() {
        properties.forEach(this::processSchedule)
    }

    private fun processSchedule(schedule: Schedule) {
        val groupName = schedule.group
        log.info("Updating schedule for '$groupName'...")

        val group = lightsAdapter.getLightsGroup(groupName)
        if (!shouldUpdateGroup(group)) return
        updateGroup(group, schedule)
    }

    private fun shouldUpdateGroup(group: LightsGroup): Boolean {
        if (!group.anyOn) {
            log.info("Lights are off, no action performed.")
            return false
        }
        // more complex logic (iterruptions etc) can be implemented here
        return true
    }

    private fun updateGroup(group: LightsGroup, schedule: Schedule) {
        val desiredLightStatus = getDesiredLightStatus(LocalDateTime.now(), group, schedule)
        if ((desiredLightStatus != null)) {
            log.info("Updating group ${group.name} to new status...")
            desiredLightStatus.forEach { (lightId, status) -> lightsAdapter.setLight(lightId, status) }
        } else {
            log.debug("No new update for group ${group.name}")
        }
    }

    /**
     * Returns null if outside of defined light schedules.
     */
    private fun getDesiredLightStatus(
            now: LocalDateTime, group: LightsGroup, schedule: Schedule):
            Map<String, LightStatus>? {

        // TODO check day of week
        val time = now.toLocalTime()

        for (i in 0..schedule.sequence.size - 2) {
            val start = schedule.sequence[i]
            val end = schedule.sequence[i + 1]
            log.debug("Checking segment ${start.time} - ${end.time}...")

            val startTime = LocalTime.parse(start.time)
            val endTime = LocalTime.parse(end.time)
            val startScene = scenes.getValue(start.scene)
            val endScene = scenes.getValue(end.scene)
            if (startTime.isAfter(time) || endTime.isBefore(time)) {
                // TODO check and define how to handle wrap-around after midnight
                continue
            }

            val coef = getInterpolationCoef(startTime, endTime, time)
            if (end.interpolate != null && end.interpolate != "linear") {
                throw IllegalArgumentException("Only 'linear' interpolation supported for now")
            }
            return interpolate(startScene, endScene, coef)
        }
        return null
    }

    private fun getInterpolationCoef(startTime: LocalTime, endTime: LocalTime, time: LocalTime): Float {
        return 0.5F // TODO
    }

    private fun interpolate(lhs: Scene, rhs: Scene, coef: Float): Map<String, LightStatus>? {
        val allLights = lhs.lights + rhs.lights
        return allLights.entries.associate { (lightId, _) ->
            val lhsStatus = lhs.lights[lightId]?.lightStatus!!
            val rhsStatus = rhs.lights[lightId]?.lightStatus!!
            val result = LightStatus(
                    lhsStatus.on || rhsStatus.on,
                    interpolate(lhsStatus.brightness, rhsStatus.brightness, coef),
                    interpolate(lhsStatus.saturation, rhsStatus.saturation, coef),
                    interpolate(lhsStatus.hue, rhsStatus.hue, coef))
            lightId to result
        }
    }

    private fun interpolate(lhs: Int, rhs: Int, coef: Float): Int = (lhs + coef * (rhs - lhs)).toInt()

}