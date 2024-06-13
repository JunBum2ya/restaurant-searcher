package com.midas.restaurant.common.contant

import org.springframework.http.HttpStatus

enum class ResultStatus(val code: String, val message: String, val status: HttpStatus = HttpStatus.BAD_REQUEST) {
    SUCCESS(code = "200", "success", status = HttpStatus.OK),
    UNAUTHENTICATED_USER(code = "001", message = "허용되지 않은 사용자입니다."),
    ACCESS_NOT_EXIST_ENTITY(code = "002", message = "존재 하지 않는 엔티티에 접근했습니다."),
    NOT_VALID_REQUEST(code = "003", message = "필수 파라미터가 입력되지 않았습니다."),
    DUPLICATE_UNIQUE_PROPERTY(code = "004", message = "중복된 값이 존재합니다.", status = HttpStatus.CONFLICT),
    USE_NOT_PERSIST_ENTITY(code = "005", message = "DB에 저장되지 않은 객체입니다."),
    NOT_SATISFY_PARAMETER_FORMAT(code = "006", message = "입력 데이터를 다시한번 확인해주시십시오."),
    UNKNOWN_EXCEPTION(code = "100", message = "알 수 없는 오류입니다.", status = HttpStatus.INTERNAL_SERVER_ERROR)
}