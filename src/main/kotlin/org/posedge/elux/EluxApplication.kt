package org.posedge.elux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EluxApplication

fun main(args: Array<String>) {
	runApplication<EluxApplication>(*args)
}
