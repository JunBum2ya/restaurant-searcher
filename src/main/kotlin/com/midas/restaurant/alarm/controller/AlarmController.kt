package com.midas.restaurant.alarm.controller

import com.midas.restaurant.alarm.service.AlarmService
import com.midas.restaurant.member.dto.MemberDetails
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v1/alarm")
class AlarmController(private val alarmService: AlarmService) {

    private val log = LoggerFactory.getLogger(AlarmController::class.java)

    @GetMapping("/subscribe")
    fun subscribe(authentication: Authentication): SseEmitter {
        log.info("subscribe: {}", authentication.name)
        return alarmService.connectAlarm(authentication.name)
    }

}