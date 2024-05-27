package com.midas.restaurant.common.dto.response

import com.midas.restaurant.common.contant.ResultStatus

class CommonResponse<T>(val code: String, val message: String, val data: T? = null) {
    companion object {
        fun <T> of(data: T): CommonResponse<T> {
            return CommonResponse(ResultStatus.SUCCESS.code, ResultStatus.SUCCESS.message, data)
        }
    }
}