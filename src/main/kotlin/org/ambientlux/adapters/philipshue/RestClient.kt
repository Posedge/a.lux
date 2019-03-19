package org.ambientlux.adapters.philipshue

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "HueRestClient", url = "\${adapters.hue.url}")
interface RestClient {

    @GetMapping(value = ["/api/{apiKey}/groups"])
    fun getGroups(@PathVariable("apiKey") apiKey: String): Map<String, Group>

    @GetMapping(value = ["/api/{apiKey}/groups/{groupId}"])
    fun getGroup(@PathVariable("apiKey") apiKey: String,
                 @PathVariable("groupId") groupId: String): Group

    @GetMapping(value = ["/api/{apiKey}/scenes"])
    fun getScenes(@PathVariable("apiKey") apiKey: String): Map<String, Scene>

    @GetMapping(value = ["/api/{apiKey}/scenes/{sceneId}"])
    fun getScene(@PathVariable("apiKey") apiKey: String,
                 @PathVariable("sceneId") sceneId: String): Scene

    @GetMapping(value = ["/api/{apiKey}/lights/{lightId}"])
    fun getLight(@PathVariable("apiKey") apiKey: String,
                 @PathVariable("lightId") lightId: String): Light

    @PutMapping(value = ["/api/{apiKey}/lights/{lightId}/state"])
    fun putLightState(@PathVariable("apiKey") apiKey: String,
                      @PathVariable("lightId") lightId: String,
                      @RequestBody body: LightState)

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

    data class Scene (
            val name: String,
            val type: String,
            val group: String?,
            val lightstates: Map<String, LightState>?
    )

    data class Light (
            val name: String,
            val state: LightState
    )

    data class LightState (
            val on: Boolean,
            val bri: Int,
            val hue: Int,
            val sat: Int,
            val transitionTime: Int?
    )

}
