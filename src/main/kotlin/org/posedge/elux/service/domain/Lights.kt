package org.posedge.elux.service.domain

data class LightsGroup (
        val id: String,
        val lights: Map<String, Light>
)

data class Light (
        val id: String,
        val lightStatus: LightStatus
)

data class LightStatus (
        val on: Boolean,
        val brightness: Int,
        val saturation: Int,
        val hue: Int
)
