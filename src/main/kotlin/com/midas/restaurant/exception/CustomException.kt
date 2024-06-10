package com.midas.restaurant.exception

import com.midas.restaurant.common.contant.ResultStatus

class CustomException(
    val code: String,
    override val message: String,
    val status: ResultStatus
) : RuntimeException(message) {
    constructor(status: ResultStatus) : this(code = status.code, message = status.message, status = status)
    constructor(status: ResultStatus, message: String) : this(code = status.code, message = message, status = status)
}