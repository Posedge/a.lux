package org.posedge.elux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
// not version controlled properties
@PropertySource(value = ["classpath:private_config.properties"], ignoreResourceNotFound = true)
@EnableConfigurationProperties
class EluxApplication

fun main(args: Array<String>) {
	runApplication<EluxApplication>(*args)
}
