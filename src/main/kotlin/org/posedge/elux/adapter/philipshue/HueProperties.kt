package org.posedge.elux.adapter.philipshue

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("adapters.hue")
class HueProperties {
    lateinit var apiKey: String
}
