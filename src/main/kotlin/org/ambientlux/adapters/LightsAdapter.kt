package org.ambientlux.adapters

import org.ambientlux.service.domain.LightStatus
import org.ambientlux.service.domain.LightsGroup

interface LightsAdapter {
    fun getLightsGroup(groupName: String) : LightsGroup
    fun setLight(lightId: String, status: LightStatus)
}