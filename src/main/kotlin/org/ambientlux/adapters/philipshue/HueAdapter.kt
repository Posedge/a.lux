package org.ambientlux.adapters.philipshue

import org.ambientlux.adapters.LightsAdapter
import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup
import org.springframework.stereotype.Component

@Component
class HueAdapter constructor(properties: HueProperties, restClient: RestClient) : LightsAdapter {

    init {
        // TODO figure out how to do logging
        println("Hue apiKey: ${properties.apiKey}")

        val groups = restClient.getGroups(properties.apiKey)
        println("Groups: $groups")
    }

    override fun getLightsGroup(groupId: String): LightsGroup {
        TODO("not implemented")
    }

    override fun setLightStatus(lightId: String, status: LightStatus) {
        TODO("not implemented")
    }
}