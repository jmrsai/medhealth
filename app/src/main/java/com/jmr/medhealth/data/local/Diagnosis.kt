package com.jmr.medhealth.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnosis_history")
data class Diagnosis(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var documentId: String? = null,
    val userId: String,
    val imageUrl: String,
    val result: String,
    val confidence: Float,
    val timestamp: Long,
    val notes: String? = null,
    // --- NEW FIELDS for Enhanced AI Diagnostics ---
    val description: String? = null,
    val suggestedNextSteps: String? = null
) {
    // Updated no-argument constructor for Firestore
    constructor() : this(0, null, "", "", "", 0f, 0L, null, null, null)
}