package service

class GroupService(
    val name: String,
    val hueId: String,
    val hueLightIds: List<Int>,
    val schedule: List<Config.Keyframe>
) {

}
