package com.vortex.secret.data.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.vortex.secret.data.model.PostModel
import java.util.*

fun mapPostModelToFirebaseDocument(postModel: PostModel): HashMap<String, Any> {
    val hashMap = HashMap<String, Any>()
    postModel.authorId?.let { hashMap["authorId"] = it }
    postModel.authorName?.let { hashMap["authorName"] = it }
    postModel.body?.let { hashMap["body"] = it }
    postModel.createdAt?.let { hashMap["createdAt"] = it }
    postModel.color?.let { hashMap["color"] = it }
    hashMap["likesCount"] = postModel.likesCount
    hashMap["likes"] = postModel.likes
    hashMap["comments"] = postModel.comments
    return hashMap
}

fun mapFirebaseDocumentToPostMutableList(result: QuerySnapshot): MutableList<PostModel> {
    return try {
        result.map { documentSnapshot ->
            val post = documentSnapshot.toObject(PostModel::class.java)
            post.id = documentSnapshot.id
            post
        }.toMutableList()
    } catch (error: Exception) {
        error.printStackTrace()
        mutableListOf()
    }
}

fun mapFirebaseDocumentToPostModel(result: DocumentSnapshot): PostModel? {
    return try {
        val post = result.toObject(PostModel::class.java)
        post?.id = result.id
        post
    } catch (error: Exception) {
        error.printStackTrace()
        null
    }
}