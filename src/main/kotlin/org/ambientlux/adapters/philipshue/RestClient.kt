package org.ambientlux.adapters.philipshue

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "HueRestClient", url = "\${adapters.hue.url}")
interface RestClient {

    @GetMapping(value = ["/api/{apiKey}/groups"])
    fun getGroups(@PathVariable("apiKey") apiKey: String): Map<String, LightsGroup>

}


class LightsGroup {
    lateinit var name: String
    lateinit var lights: List<String>
}