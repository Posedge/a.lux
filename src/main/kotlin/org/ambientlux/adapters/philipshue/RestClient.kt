package org.ambientlux.adapters.philipshue

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "HueRestClient", url = "\${adapters.hue.url}")
interface RestClient {

    @GetMapping(value = ["/api/{apiKey}/groups"])
    fun getGroups(@PathVariable("apiKey") apiKey: String): Map<String, Group>

    @GetMapping(value = ["/api/{apiKey}/groups/{groupId}"])
    fun getGroup(@PathVariable("apiKey") apiKey: String,
                 @PathVariable("groupId") groupId: String): Group

    @GetMapping(value = ["/api/{apiKey}/lights/{lightId}"])
    fun getLight(@PathVariable("apiKey") apiKey: String,
                 @PathVariable("lightId") lightId: String): Light

    // DTO's

    data class Group (
            val name: String,
            val lights: List<String>,
            val state: GroupState
    )

    data class GroupState (
            val all_on: Boolean,
            val any_on: Boolean
    )

    data class Light (
            val name: String,
            val state: LightState
    )

    data class LightState (
            val on: Boolean,
            val bri: Int,
            val hue: Int,
            val sat: Int
    )

}
