package org.fitverse.project.plugins

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.common.util.concurrent.MoreExecutors
import com.google.firebase.cloud.FirestoreClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirestoreService {
    private val db get() = FirestoreClient.getFirestore()

    suspend fun <T : Any> set(
        collection: String,
        documentId: String,
        data: T,
    ): Result<Unit> = runCatching {
        db.collection(collection)
            .document(documentId)
            .set(data)
            .await()
        Unit
    }

    suspend fun <T> get(
        collection: String,
        documentId: String,
        type: Class<T>,
    ): Result<T?> = runCatching {
        db.collection(collection)
            .document(documentId)
            .get()
            .await()
            .toObject(type)
    }

    suspend fun delete(
        collection: String,
        documentId: String,
    ): Result<Unit> = runCatching {
        db.collection(collection)
            .document(documentId)
            .delete()
            .await()
        Unit
    }

    suspend fun <T> getAll(
        collection: String,
        type: Class<T>,
    ): Result<List<Pair<String, T>>> = runCatching {
        db.collection(collection)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                val obj = doc.toObject(type) ?: return@mapNotNull null
                Pair(doc.id, obj)
            }
    }
}

suspend fun <T> ApiFuture<T>.await(): T = suspendCancellableCoroutine { cont ->
    ApiFutures.addCallback(
        this,
        object : ApiFutureCallback<T> {
            override fun onSuccess(result: T) { cont.resume(result) }
            override fun onFailure(t: Throwable) { cont.resumeWithException(t) }
        },
        MoreExecutors.directExecutor()
    )
}