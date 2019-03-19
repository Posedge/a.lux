package org.ambientlux.adapters.philipshue

import org.ambientlux.adapters.LightsAdapter
import org.ambientlux.service.domain.Light
import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup
import org.ambientlux.service.domain.Scene
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class HueAdapter(
        properties: HueProperties,
        private val restClient: RestClient) : LightsAdapter {

    private val log = LoggerFactory.getLogger(javaClass)
    private val apiKey = properties.apiKey
    private val groupNameToId: Map<String, String>
            = fetchGroups().entries.associate { it.value.name to it.key }

    override fun fetchLightsGroup(groupName: String): LightsGroup {
        val groupId = groupNameToId[groupName]
                ?: throw IllegalArgumentException("Group name '$groupName' not found on the Hue!")
        return fetchGroupById(groupId)
    }

    override fun setLight(lightId: String, status: LightStatus) {
        log.debug("Setting light $lightId to $status...")
        restClient.putLightState(apiKey, lightId, convertLightStatusToHue(status, 100))
    }

    override fun fetchScenes(): Map<String, Scene> {
        // TODO filter these. there are duplicates and stuff
        log.debug("Reading list of all scenes...")
        val scenes = restClient.getScenes(apiKey)

        log.debug("Reading detail view of all scenes...")
        return scenes.entries
                .filter { (_, scene) -> scene.group != null }
                .associate { (id, _) ->
                    val scene = restClient.getScene(apiKey, id)
                    id to convertScene(id, scene)
                }
    }

    private fun fetchGroups(): Map<String, LightsGroup> {
        log.debug("Fetching groups...")
        return restClient.getGroups(apiKey).entries.associate {
            it.key to convertLightsGroup(it.value)
        }
    }

    private fun fetchGroupById(groupId: String): LightsGroup {
        log.debug("Fetching group $groupId...")
        val group = restClient.getGroup(apiKey, groupId)
        return convertLightsGroup(group)
    }

    private fun fetchLight(id: String): Light {
        log.debug("Fetching light $id...")
        val light = restClient.getLight(apiKey, id)
        return convertLight(id, light)
    }

    private fun convertLightsGroup(group: RestClient.Group): LightsGroup {
        val lights = group.lights.associate { it to fetchLight(it) }
        return LightsGroup(group.name, lights, group.state.any_on)
    }

    private fun convertLight(id: String, light: RestClient.Light): Light =
            Light(id, convertLightStatus(light.state))

    private fun convertLightStatus(light: RestClient.LightState) =
            LightStatus(light.on, light.bri, light.sat, light.hue)

    private fun convertLightStatusToHue(light: LightStatus, transitionTime: Int?) =
        RestClient.LightState(light.on, light.brightness, light.hue, light.saturation, transitionTime)

    private fun convertScene(id: String, scene: RestClient.Scene): Scene {
        val lights = scene.lightstates!!.entries.associate { (id, state) -> id to convertLightStatus(state) }
        return Scene(id, scene.name, scene.group!!, lights)
    }

}