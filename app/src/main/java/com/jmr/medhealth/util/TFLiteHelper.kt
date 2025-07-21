package com.jmr.medhealth.util

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class TFLiteHelper(
    private val context: Context,
    private val modelName: String = "disease_model.tflite",
    private val scoreThreshold: Float = 0.1f,
    private val maxResults: Int = 3
) {

    private var imageClassifier: ImageClassifier? = null

    init {
        setupClassifier()
    }

    private fun setupClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(scoreThreshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder().useGpu()
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            // Handle error, e.g., log it or notify the user
            e.printStackTrace()
        }
    }

    fun classifyImage(bitmap: Bitmap): List<Pair<String, Float>> {
        if (imageClassifier == null) {
            setupClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val results = imageClassifier?.classify(tensorImage)

        return results?.flatMap { classification ->
            classification.categories.map { category ->
                Pair(category.label, category.score)
            }
        } ?: emptyList()
    }

    fun close() {
        imageClassifier?.close()
        imageClassifier = null
    }
}