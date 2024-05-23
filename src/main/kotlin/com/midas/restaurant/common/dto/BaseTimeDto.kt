package com.midas.restaurant.common.dto

import com.midas.restaurant.common.domain.BaseEntity
import java.time.LocalDateTime

abstract class BaseTimeDto(val createdAt: LocalDateTime?, val updatedAt: LocalDateTime?) {
    constructor(entity: BaseEntity): this(entity.getCreatedAt(), entity.getUpdatedAt())
}