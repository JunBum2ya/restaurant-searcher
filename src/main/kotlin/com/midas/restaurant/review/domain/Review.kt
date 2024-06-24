package com.midas.restaurant.review.domain

import com.midas.restaurant.common.domain.BaseEntity
import com.midas.restaurant.member.domain.Member
import com.midas.restaurant.restaurant.domain.Restaurant
import jakarta.persistence.*

@Entity
class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "restaurant_id") val restaurant: Restaurant,
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "author_id") val author: Member,
    private var title: String,
    @Column(length = 10000) private var content: String,
): BaseEntity() {

    @OrderBy("createdAt DESC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = [(CascadeType.ALL)])
    private val comments: MutableList<Comment> = mutableListOf()

    fun addComment(comment: Comment) {
        comment.setReview(this)
        this.comments.add(comment)
    }

    fun getTitle(): String{
        return title
    }

    fun getContent(): String {
        return content
    }

    fun getComments(): List<Comment>{
        return this.comments
    }

    fun update(title: String? = null, content: String? = null) {
        title?.let { this.title = it }
        content?.let { this.content = it }
    }

}