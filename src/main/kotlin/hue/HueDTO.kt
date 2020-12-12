package hue

data class Light(
    val state: LightState,
) {
    data class LightState (
        val on: Boolean,
        val bri: Int,
        val hue: Int,
        val sat: Int,
    )
}

/**
 * A group on the hue is a collection of lights. Among other things, this can be a room or a zone.
 */
data class Group(
    val lights: List<Int>,
    val type: String,
)

/**
 * A scene on the hue is a configuration of one or more lights.
 */
data class Scene(
    val lights: List<Int>,
    val name: String,
    val type: String,
    val group: String?,
)