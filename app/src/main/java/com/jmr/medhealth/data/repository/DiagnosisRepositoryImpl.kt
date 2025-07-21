package com.jmr.medhealth.data.repository

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.jmr.medhealth.data.local.Diagnosis
import com.jmr.medhealth.data.local.DiagnosisDao
import com.jmr.medhealth.util.DiseaseInfoProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

// Note: Ensure this class is provided via Hilt AppModule
class DiagnosisRepositoryImpl @Inject constructor(
    private val diagnosisDao: DiagnosisDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : DiagnosisRepository {

    private val userId: String
        get() = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    override fun getDiagnosisHistory(): Flow<List<Diagnosis>> {
        return diagnosisDao.getDiagnosisHistory(userId)
    }

    override suspend fun saveDiagnosis(bitmap: Bitmap, result: String, confidence: Float): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val imageUrl = uploadImageToStorage(bitmap)
            val diseaseInfo = DiseaseInfoProvider.getInfoForLabel(result)

            val diagnosis = Diagnosis(
                userId = userId,
                imageUrl = imageUrl,
                result = result,
                confidence = confidence,
                timestamp = System.currentTimeMillis(),
                description = diseaseInfo.description,
                suggestedNextSteps = diseaseInfo.suggestedNextSteps
            )

            val document = firestore.collection("users").document(userId)
                .collection("history").document()
            diagnosis.documentId = document.id
            document.set(diagnosis).await()

            val newId = diagnosisDao.insertDiagnosis(diagnosis)
            Result.success(newId.toInt())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncHistory() = withContext(Dispatchers.IO) {
        val querySnapshot = firestore.collection("users").document(userId)
            .collection("history")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()

        val remoteHistory = querySnapshot.toObjects(Diagnosis::class.java)
        diagnosisDao.clearHistory()
        remoteHistory.forEach { diagnosis ->
            diagnosisDao.insertDiagnosis(diagnosis)
        }
    }

    override suspend fun getDiagnosisById(id: Int): Diagnosis? = withContext(Dispatchers.IO) {
        return@withContext diagnosisDao.getDiagnosisById(id)
    }

    override suspend fun updateDiagnosisNotes(diagnosisId: Int, notes: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val diagnosis = diagnosisDao.getDiagnosisById(diagnosisId)
            requireNotNull(diagnosis) { "Diagnosis not found locally" }
            requireNotNull(diagnosis.documentId) { "Diagnosis not synced to Firestore" }

            firestore.collection("users").document(userId)
                .collection("history").document(diagnosis.documentId!!)
                .update("notes", notes).await()

            diagnosisDao.insertDiagnosis(diagnosis.copy(notes = notes))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadImageToStorage(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val data = baos.toByteArray()

        val storageRef = storage.reference
            .child("images/${userId}/${UUID.randomUUID()}.jpg")

        val uploadTask = storageRef.putBytes(data).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }
}