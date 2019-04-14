package com.vortex.secret.data.model

data class PostModel(
        var id: String? = null,
        var authorId: String? = null,
        var authorName: String? = null,
        var createdAt: String? = null,
        var body: String? = null,
        var color: Int? = null,
        var likesCount: Int = 0,
        var likes: MutableList<PostLikeModel> = mutableListOf(),
        var comments: MutableList<PostCommentModel> = mutableListOf()
)

data class PostLikeModel(
        var authorId: String? = null
)

data class PostCommentModel(
        var authorId: String? = null,
        var createdAt: String? = null,
        var comment: String? = null
)
