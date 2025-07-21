package com.jmr.medhealth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosis(diagnosis: Diagnosis): Long

    @Query("SELECT * FROM diagnosis_history WHERE userId = :userId ORDER BY timestamp DESC")
    fun getDiagnosisHistory(userId: String): Flow<List<Diagnosis>>

    // --- ADD THIS FUNCTION ---
    @Query("SELECT * FROM diagnosis_history WHERE id = :id")
    suspend fun getDiagnosisById(id: Int): Diagnosis?

    @Query("DELETE FROM diagnosis_history")
    suspend fun clearHistory()
}