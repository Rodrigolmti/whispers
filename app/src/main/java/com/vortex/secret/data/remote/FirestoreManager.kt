package com.vortex.secret.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.*
import com.vortex.secret.data.mappers.mapPostModelToFirebaseDocument
import com.vortex.secret.data.model.PostModel

const val ORDER_BY_LIKE_PARAM = "likesCount"
const val ORDER_BY_DATE_PARAM = "createdAt"
const val POST_HIGHLIGHT_LIMIT: Long = 10
const val POST_COLLECTION = "posts"

interface IFirestoreManager {

    fun updateUserProfile(userProfileChangeRequest: UserProfileChangeRequest): Task<Void>?

    fun getCurrentUser(): FirebaseUser?

    fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult>

    fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult>

    fun signOut()

    fun getPostsByDate(): Task<QuerySnapshot>

    fun getHighlightedPosts(): Task<QuerySnapshot>

    fun addPost(postModel: PostModel): Task<DocumentReference>

    fun updatePostLike(postModel: PostModel): Task<Void>

    fun updatePostComment(postModel: PostModel): Task<Void>

    fun deletePost(postModel: PostModel): Task<Void>

    fun getPostById(postId: String): Task<DocumentSnapshot>
}

class FirestoreManager : IFirestoreManager {

    override fun updateUserProfile(userProfileChangeRequest: UserProfileChangeRequest): Task<Void>? =
        FirebaseAuth.getInstance().currentUser?.updateProfile(userProfileChangeRequest)

    override fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun createUserWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

    override fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult> =
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)

    override fun signOut() = FirebaseAuth.getInstance().signOut()

    override fun getPostsByDate(): Task<QuerySnapshot> =
        FirebaseFirestore.getInstance().collection(POST_COLLECTION)
            .orderBy(ORDER_BY_DATE_PARAM, Query.Direction.DESCENDING)
            .get()

    override fun getHighlightedPosts(): Task<QuerySnapshot> =
        FirebaseFirestore.getInstance().collection(POST_COLLECTION)
            .orderBy(ORDER_BY_LIKE_PARAM, Query.Direction.DESCENDING).limit(POST_HIGHLIGHT_LIMIT)
            .get()

    override fun addPost(postModel: PostModel): Task<DocumentReference> =
        FirebaseFirestore.getInstance()
            .collection(POST_COLLECTION)
            .add(mapPostModelToFirebaseDocument(postModel))

    override fun updatePostLike(postModel: PostModel): Task<Void> =
        FirebaseFirestore.getInstance()
            .collection(POST_COLLECTION)
            .document(postModel.id ?: "")
            .set(mapPostModelToFirebaseDocument(postModel), SetOptions.merge())

    override fun updatePostComment(postModel: PostModel): Task<Void> =
        FirebaseFirestore.getInstance()
            .collection(POST_COLLECTION)
            .document(postModel.id ?: "")
            .set(mapPostModelToFirebaseDocument(postModel), SetOptions.merge())

    override fun deletePost(postModel: PostModel): Task<Void> =
        FirebaseFirestore.getInstance()
            .collection(POST_COLLECTION)
            .document(postModel.id ?: "")
            .delete()

    override fun getPostById(postId: String): Task<DocumentSnapshot> =
        FirebaseFirestore.getInstance()
            .collection(POST_COLLECTION)
            .document(postId)
            .get()
}