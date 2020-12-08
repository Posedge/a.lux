import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("Main")

fun main(args: Array<String>) {
    ALux()
}

class ALux {
    val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("hello, connecting to hue at ${CONFIG.url}")
    }
}
