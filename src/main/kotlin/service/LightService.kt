package service

import CONFIG
import hue.Group
import hue.HueClient
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class LightService(private val hue: HueClient = HueClient()) {

    private val logger = LoggerFactory.getLogger(javaClass)
    init {
        logger.info("A.lux is starting up. Configuration: $CONFIG")
    }
    val groups: Map<String, GroupService> = findGroups()

    private fun findGroups(): Map<String, GroupService> {
        val allLightGroups: Map<String, Group> = runBlocking { hue.getLightGroups() }
        logger.debug("Found groups on the hue: $groups")

        return CONFIG.groups.associate { configGroup ->
            val name = configGroup.name
            val (hueId, hueGroup) = findHueGroupByName(allLightGroups, name)
            name to GroupService(name, hueId, hueGroup.lights)
        }
    }

    private fun findHueGroupByName(allLightGroups: Map<String, Group>, name: String): Pair<String, Group> {
        val (id, group) = allLightGroups.entries.find { (_, hueGroup) -> hueGroup.name == name }
            ?: error("Could not find group with name '$name'")
        return id to group
    }

}
