package org.ambientlux.service

import org.ambientlux.adapters.LightsAdapter
import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup
import org.ambientlux.service.domain.Scene
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.*
import javax.annotation.PostConstruct

@Service
class LightsService @Autowired constructor(
        private val lightsAdapter: LightsAdapter, private val properties: ScheduleProperties) {

    private val log = LoggerFactory.getLogger(javaClass)

    // Read all scenes
    private val scenesByName: Map<String, Scene>

    init {
        val scenes: Map<String, Scene> = lightsAdapter.fetchScenes()
        scenesByName = scenes.entries.associate { (_, scene) -> scene.name to scene }
    }

    /**
     * Will be called every minute.
     */
    @Scheduled(fixedRate = 60000)
    public fun onUpdate() {
        properties.lightSchedules.forEach(this::processSchedule)
    }

    @PostConstruct
    private fun postConstruct() {
        onUpdate()
    }

    private fun processSchedule(schedule: Schedule) {
        val groupName = schedule.group
        log.info("Updating schedule for '$groupName'...")

        val group = lightsAdapter.fetchLightsGroup(groupName)
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

            val startScene = scenesByName[start.scene]
            val endScene = scenesByName[end.scene]
            if (startScene == null || endScene == null) {
                throw IllegalArgumentException("No scene named ${end.scene} on the hue")
            }
            if (startTime.isAfter(time) || endTime.isBefore(time)) {
                // TODO check and define how to handle wrap-around after midnight
                continue
            }

            val coef = getInterpolationCoef(startTime, endTime, time)
            if (end.interpolate != null && end.interpolate != "linear") {
                throw NotImplementedError("Only 'linear' interpolation supported for now")
            }
            return interpolate(startScene, endScene, coef)
        }
        return null
    }

    private fun getInterpolationCoef(startTime: LocalTime, endTime: LocalTime, time: LocalTime): Float =
            (Duration.between(startTime, time).toSeconds().toFloat()
                    / Duration.between(startTime, endTime).toSeconds().toFloat())

    private fun interpolate(lhs: Scene, rhs: Scene, coef: Float): Map<String, LightStatus>? {
        val allLights = lhs.lightStatus.keys + rhs.lightStatus.keys
        return allLights.associate { lightId ->
            lightId to interpolate(lhs.lightStatus[lightId], rhs.lightStatus[lightId], coef)
        }
    }

    private fun interpolate(lhs: LightStatus?, rhs: LightStatus?, coef: Float): LightStatus =
            when {
                lhs == null || !lhs.on -> when {
                    rhs == null || !rhs.on -> LightStatus(false, 0, 0, 0)
                    else -> LightStatus(true, interpolate(0, rhs.brightness, coef), rhs.saturation, rhs.hue)
                }
                rhs == null || !rhs.on -> LightStatus(true, interpolate(lhs.brightness, 0, coef), lhs.saturation, lhs.hue)
                else -> LightStatus(
                        true,
                        interpolate(lhs.brightness, rhs.brightness, coef),
                        interpolate(lhs.saturation, rhs.saturation, coef),
                        interpolate(lhs.hue, rhs.hue, coef))
            }

    private fun interpolate(lhs: Int, rhs: Int, coef: Float): Int = (lhs + coef * (rhs - lhs)).toInt()

}