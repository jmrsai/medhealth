package com.jmr.medhealth.util

import com.jmr.medhealth.domain.model.DiseaseInfo

object DiseaseInfoProvider {

    private val diseaseMap = mapOf(
        "Cataract" to DiseaseInfo(
            label = "Cataract",
            description = "A cataract is a clouding of the lens in the eye which leads to a decrease in vision. It can affect one or both eyes and often develops slowly over time.",
            suggestedNextSteps = "1. Schedule a comprehensive eye exam with an ophthalmologist.\n2. Discuss surgical options if vision is significantly impaired.\n3. Use brighter lighting for reading and avoid driving at night."
        ),
        "Diabetic Retinopathy" to DiseaseInfo(
            label = "Diabetic Retinopathy",
            description = "A complication of diabetes, caused by high blood sugar levels damaging the back of the eye (retina). It can lead to blindness if left undiagnosed and untreated.",
            suggestedNextSteps = "1. Consult an ophthalmologist and your endocrinologist immediately.\n2. Strictly control blood sugar, blood pressure, and cholesterol levels.\n3. Regular follow-up eye exams are critical."
        ),
        "Normal" to DiseaseInfo(
            label = "Normal",
            description = "The analysis did not detect signs of the specific conditions the model is trained on. This does not rule out all possible eye conditions.",
            suggestedNextSteps = "Continue with regular, routine eye check-ups as recommended by your eye care professional."
        )
        // Add other diseases your model can detect here
    )

    fun getInfoForLabel(label: String): DiseaseInfo {
        return diseaseMap[label] ?: DiseaseInfo(
            label = label,
            description = "No detailed information is available for this result.",
            suggestedNextSteps = "Please consult a medical professional for an accurate diagnosis and guidance."
        )
    }
}