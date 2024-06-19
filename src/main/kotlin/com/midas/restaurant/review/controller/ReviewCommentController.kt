package com.midas.restaurant.review.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/review/{reviewId}/comment")
class ReviewCommentController {

    @PostMapping
    fun postComment(@PathVariable reviewId: Long) {

    }

}