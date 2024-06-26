package com.midas.restaurant.alarm.service

import com.midas.restaurant.common.contant.ResultStatus
import com.midas.restaurant.exception.CustomException
import com.midas.restaurant.alarm.domain.contant.AlarmType
import com.midas.restaurant.alarm.repository.EmitterRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException

@Service
class AlarmService(
    @Value("\${sse.timeout}") private val timeout: Long,
    private val emitterRepository: EmitterRepository
) {

    private val log = LoggerFactory.getLogger(AlarmService::class.java)

    fun send(username: String, alarmType: AlarmType, message: String) {
        emitterRepository.findById(username)?.let {
            try {
                it.send(SseEmitter.event().id(username).name(alarmType.code).data(message))
            } catch (e: IOException) {
                emitterRepository.delete(username)
                throw CustomException(ResultStatus.OCCUR_ALARM_ERROR)
            }
        } ?: { log.info("No emitter found for $username") }
    }

    fun connectAlarm(username: String): SseEmitter {
        val sseEmitter = SseEmitter(timeout)
        emitterRepository.save(username, sseEmitter)
        sseEmitter.onCompletion { emitterRepository.delete(username) }
        sseEmitter.onTimeout { emitterRepository.delete(username) }
        try {
            sseEmitter.send(
                SseEmitter.event()
                    .id(username)
                    .name(AlarmType.CONNECT_ALARM.code)
                    .data("connect completed")
            )
            return sseEmitter
        } catch (e: IOException) {
            sseEmitter.completeWithError(e)
            throw CustomException(ResultStatus.OCCUR_ALARM_ERROR)
        }
    }

}