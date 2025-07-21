package com.jmr.medhealth.util

import android.graphics.Bitmap
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class ImageProcessor {

    companion object {
        init {
            if (!OpenCVLoader.initLocal()) {
                // Handle initialization error
            }
        }
    }

    fun processEyeImage(image: Bitmap): Bitmap {
        val mat = Mat()
        Utils.bitmapToMat(image, mat)

        // Convert to grayscale
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)

        // Apply a blur to reduce noise
        Imgproc.GaussianBlur(mat, mat, Size(5.0, 5.0), 0.0)

        // Edge detection
        val edges = Mat()
        Imgproc.Canny(mat, edges, 100.0, 200.0)

        // Convert back to bitmap
        val resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(edges, resultBitmap)

        return resultBitmap
    }
}