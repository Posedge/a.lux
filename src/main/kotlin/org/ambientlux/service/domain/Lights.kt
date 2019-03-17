package org.ambientlux.service.domain

/**
 * A set of lights and their state.
 */
data class LightsGroup (
        val name: String,
        val lights: Map<String, Light>,
        val anyOn: Boolean
)

/**
 * Represents a configured state for the lights in a group.
 */
data class Scene (
        val id: String,
        val name: String,
        val groupId: String,
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
