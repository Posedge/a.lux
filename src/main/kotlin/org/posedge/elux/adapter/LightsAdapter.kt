package org.posedge.elux.adapter

import org.posedge.elux.service.domain.LightStatus
import org.posedge.elux.service.domain.LightsGroup

interface LightsAdapter {
    fun getLightsGroup(groupId: String) : LightsGroup
    fun setLightStatus(lightId: String, status: LightStatus)
}