package service

import CONFIG
import hue.HueClient
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class LightService {

    val logger = LoggerFactory.getLogger(javaClass)
    var isManagingLighting: Boolean = false;
    var lastLightState: Any? = null;
    val hue = HueClient()

    init {
        logger.info("A.lux is starting up. Configuration: $CONFIG")
        runBlocking {
//            mainLoop()
            val groups = hue.getLightGroups()
            logger.info("Found groups: $groups")
        }
    }

    fun mainLoop() {
        while (true) {
            refresh()
            Thread.sleep(CONFIG.refreshIntervalMillis.toLong())
        }
    }

    private fun refresh() {
        logger.debug("Refresh")
        if (isManagingLighting) {
            verifyLightState()
            setLightState()
        } else {
            checkAutoScene()
        }
    }

    private fun verifyLightState() {
        TODO("Not yet implemented")
    }

    private fun setLightState() {
        TODO("Not yet implemented")
    }

    /**
     * Check if the scene in the room matches the designated 'auto' scene and take over.
     */
    private fun checkAutoScene() {
        TODO("Not yet implemented")
    }
}
