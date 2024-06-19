package com.midas.restaurant.review.domain

import com.midas.restaurant.common.domain.BaseEntity
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.restaurant.domain.Restaurant
import jakarta.persistence.*

@Entity
class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "review_id") private var review: Review? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "author_id") val author: Member,
    private var parentCommentId: Long? = null,
    @Column(length = 500) private var content: String
): BaseEntity() {

    @OrderBy("createdAt ASC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCommentId", cascade = [CascadeType.ALL])
    private val childComments: MutableList<Comment> = mutableListOf()

    fun addChildComment(comment: Comment) {
        comment.parentCommentId = this.parentCommentId
        this.childComments.add(comment)
    }

    fun setReview(review: Review) {
        this.review = review
    }

}