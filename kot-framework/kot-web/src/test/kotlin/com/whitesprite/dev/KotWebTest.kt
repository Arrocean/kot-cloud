package com.whitesprite.dev

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.kotest.core.spec.style.StringSpec

@MicronautTest
class KotWebTest(private val application: EmbeddedApplication<*>) : StringSpec({

    "test the server is running" {
        assert(application.isRunning)
    }
})
