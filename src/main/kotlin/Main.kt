import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    ALux()
}

class ALux {
    val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("hello")
    }
}