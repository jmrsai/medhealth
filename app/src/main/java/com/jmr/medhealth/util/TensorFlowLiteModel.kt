package com.jmr.medhealth.util

import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TensorFlowLiteModel(
    private val modelFile: MappedByteBuffer
) {
    private val tflite: Interpreter? = Interpreter(modelFile)

    fun runInference(inputData: ByteBuffer): FloatArray {
        val outputTensor = tflite?.getOutputTensor(0)
        val outputShape = outputTensor?.shape() ?: intArrayOf(1, 10)
        val outputSize = outputShape.fold(1) { acc, size -> acc * size }
        val outputData = ByteBuffer.allocateDirect(outputSize * 4) // Float is 4 bytes
            .order(ByteOrder.nativeOrder())

        tflite?.run(inputData, outputData)

        outputData.rewind()
        val result = FloatArray(outputSize)
        outputData.asFloatBuffer().get(result)

        return result
    }

    fun close() {
        tflite?.close()
    }

    companion object {
        fun loadModelFile(filePath: String): MappedByteBuffer {
            val fileInputStream = FileInputStream(filePath)
            val fileChannel = fileInputStream.channel
            val startOffset = fileChannel.position()
            val declaredLength = fileChannel.size()
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }
    }
}
