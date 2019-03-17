package org.ambientlux.service.domain

data class LightsGroup (
        val name: String,
        val lights: Map<String, Light>,
        val anyOn: Boolean
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
