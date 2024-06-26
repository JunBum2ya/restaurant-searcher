package com.midas.restaurant.alarm.repository

import org.springframework.stereotype.Repository
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Repository
class EmitterRepository {

    private val sseMap = HashMap<String, SseEmitter>()

    fun save(id: String, emitter: SseEmitter) {
        sseMap[id] = emitter
    }

    fun findById(id: String): SseEmitter? {
        return sseMap.getOrDefault(id, null)
    }

    fun delete(id: String) {
        sseMap.remove(id)
    }

}