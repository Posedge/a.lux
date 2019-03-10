package org.ambientlux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@PropertySource(value = ["classpath:private_config.properties"], ignoreResourceNotFound = true)
@EnableConfigurationProperties
@EnableFeignClients
class AluxApplication

fun main(args: Array<String>) {
	runApplication<AluxApplication>(*args)
}
