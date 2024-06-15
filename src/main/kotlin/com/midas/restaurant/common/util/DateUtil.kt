package com.midas.restaurant.common.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class DateUtil {
    companion object {

        fun now(): LocalDateTime {
            return LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        }

        fun afterMilliseconds(milliseconds: Long): LocalDateTime {
            return now().plusNanos(milliseconds * 1_000_000)
        }

        fun convertLocalDateTimeToDate(localDateTime: LocalDateTime): Date {
            val instant = localDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant()
            return Date.from(instant)
        }

    }
}