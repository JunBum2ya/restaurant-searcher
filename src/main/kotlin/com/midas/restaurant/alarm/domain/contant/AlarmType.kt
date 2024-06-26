package com.midas.restaurant.alarm.domain.contant

enum class AlarmType(val code: String) {
    CONNECT_ALARM(code = "00"), POST_REVIEW(code = "01"), POST_COMMENT(code = "02"), CLICK_LIKE(code = "03")
}