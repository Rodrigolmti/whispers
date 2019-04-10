package com.vortex.secret.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.vortex.secret.data.mappers.mapPostModelToFirebaseDocument
import com.vortex.secret.data.model.PostModel

const val ORDER_BY_PARAM = "createdAt"
const val POST_COLLECTION = "posts"

interface IFirestoreManager {

    fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult>

    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult>

    fun getPostsByDate(): Task<QuerySnapshot>

    fun addPost(postModel: PostModel): Task<DocumentReference>

    fun updatePostLike(postModel: PostModel): Task<Void>

    fun deletePost(postModel: PostModel): Task<Void>
}

class FirestoreManager : IFirestoreManager {

    override fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    }

    override fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }

    override fun getPostsByDate(): Task<QuerySnapshot> {
        return FirebaseFirestore.getInstance().collection(POST_COLLECTION)
            .orderBy(ORDER_BY_PARAM, Query.Direction.DESCENDING)
            .get()
    }

    override fun addPost(postModel: PostModel): Task<DocumentReference> {
        return FirebaseFirestore.getInstance()
            .collection(POST_COLLECTION)
            .add(mapPostModelToFirebaseDocument(postModel))
    }

    override fun updatePostLike(postModel: PostModel): Task<Void> {
        return FirebaseFirestore.getInstance()
            .collection(POST_COLLECTION)
            .document(postModel.id ?: "")
            .set(mapPostModelToFirebaseDocument(postModel))
    }

    override fun deletePost(postModel: PostModel): Task<Void> {
        return FirebaseFirestore.getInstance()
            .collection(POST_COLLECTION)
            .document(postModel.id ?: "")
            .delete()
    }
}