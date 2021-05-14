package com.depop.cx.drc.workflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DrcApplication

fun main(args: Array<String>) {
    runApplication<DrcApplication>(*args)
}
