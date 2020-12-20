package hue

data class Light(
    val state: LightState,
) {
    data class LightState (
        val on: Boolean,
        val bri: Int,
        val hue: Int,
        val sat: Int,
        val xy: List<Float>?,
        val ct: Int?,
    )
}

/**
 * A group on the hue is a collection of lights.
 * A group can be a room, a zone, or another internal type.
 */
data class Group(
    val lights: List<Int>,
    val type: String,
)

/**
 * A scene on the hue is a configuration of one or more lights in a group.
 * There are [GroupScene]s and Light Scenes, the former are created by the user in the app.
 */
data class GenericScene(
    val lights: List<Int>,
    val name: String,
    val type: String,
    val group: String?,
)

data class GroupScene(
    val lights: List<Int>,
    val name: String,
    val group: String,
    val lightstates: Map<String, LightState>,
) {
    data class LightState (
        val on: Boolean,
        val bri: Int,
        val xy: List<Float>?,
        val ct: Int?,
    )
}