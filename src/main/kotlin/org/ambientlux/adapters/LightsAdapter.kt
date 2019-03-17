package org.ambientlux.adapters

import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup
import org.ambientlux.service.domain.Scene

interface LightsAdapter {
    fun getLightsGroup(groupName: String): LightsGroup
    fun setLight(lightId: String, status: LightStatus)
    fun getScenes(): Map<String, Scene>
}