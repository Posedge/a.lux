package org.ambientlux.adapters

import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup

interface LightsAdapter {
    fun getLightsGroup(groupId: String) : LightsGroup
    fun setLightStatus(lightId: String, status: LightStatus)
}