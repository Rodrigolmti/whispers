package com.vortex.secret.data.model

data class PostModel(
        var id: String? = null,
        var authorId: String? = null,
        var createdAt: String? = null,
        var body: String? = null,
        var color: Int? = null,
        var likes: MutableList<PostLike> = mutableListOf(),
        var comments: MutableList<PostComment> = mutableListOf()
)

data class PostLike(
        var authorId: String? = null
)

data class PostComment(
        var authorId: String? = null,
        var comment: String? = null
)
