package org.ambientlux.adapters.philipshue

import org.ambientlux.adapters.LightsAdapter
import org.ambientlux.service.domain.Light
import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class HueAdapter constructor(val properties: HueProperties, val restClient: RestClient) : LightsAdapter {

    private val log = LoggerFactory.getLogger(javaClass)
    private val groupNameToId: Map<String, String>

    init {
        val groups = fetchGroups()
        groupNameToId = groups.entries.associate { it.value.name to it.key }
    }

    private fun fetchGroups(): Map<String, LightsGroup> {
        log.debug("Fetching groups...")
        return restClient.getGroups(properties.apiKey).entries.associate {
            it.key to convertLightsGroup(it.value)
        }
    }

    private fun fetchGroup(groupId: String): LightsGroup {
        log.debug("Fetching group $groupId...")
        val group = restClient.getGroup(properties.apiKey, groupId)
        return convertLightsGroup(group)
    }

    private fun fetchLight(id: String): Light {
        log.debug("Fetching light $id...")
        val light = restClient.getLight(properties.apiKey, id)
        return convertLight(id, light)
    }

    private fun convertLightsGroup(group: RestClient.Group): LightsGroup {
        val lights = group.lights.associate { it to fetchLight(it) }
        return LightsGroup(group.name, lights, group.state.any_on)
    }

    private fun convertLight(id: String, light: RestClient.Light): Light {
        val status = LightStatus(light.state.on, light.state.bri, light.state.sat, light.state.hue)
        return Light(id, status)
    }

    override fun getLightsGroup(groupName: String): LightsGroup {
        val groupId = groupNameToId[groupName]
                ?: throw IllegalArgumentException("Group name '$groupName' not found on the Hue!")
        return fetchGroup(groupId)
    }

    override fun setLight(lightId: String, status: LightStatus) {
        TODO("not implemented")
    }

}