package com.jmr.medhealth.data.repository

import android.graphics.Bitmap
import com.jmr.medhealth.data.local.Diagnosis
import kotlinx.coroutines.flow.Flow

interface DiagnosisRepository {
    fun getDiagnosisHistory(): Flow<List<Diagnosis>>
    suspend fun saveDiagnosis(bitmap: Bitmap, result: String, confidence: Float): Result<Int>
    suspend fun syncHistory()
    suspend fun getDiagnosisById(id: Int): Diagnosis?
    suspend fun updateDiagnosisNotes(diagnosisId: Int, notes: String): Result<Unit>
} // <-- Ensure there's nothing extra after this closing brace