package com.priyanshu.floralens.classifier

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.exp

class TFLiteImageClassifier(context: Context) {

    private val interpreter: Interpreter
    private val labels: List<String>

    init {
        interpreter = Interpreter(loadModelFile(context, "plant_disease_diagnoser.tflite"),
            Interpreter.Options().apply { setNumThreads(4) })
        labels = loadLabels(context, "labels.txt")
    }

    private fun loadModelFile(context: Context, fileName: String): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd(fileName)
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabels(context: Context, fileName: String): List<String> {
        val labels = mutableListOf<String>()
        BufferedReader(InputStreamReader(context.assets.open(fileName))).use { reader ->
            var line = reader.readLine()
            while (line != null) {
                val trimmed = line.trim()
                if (trimmed.isNotEmpty()) labels.add(trimmed)
                line = reader.readLine()
            }
        }
        return labels
    }

    fun classify(bitmap: Bitmap): com.priyanshu.floralens.data.ClassificationResult {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 384, 384, true)
        val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)

        // Output shape [1, 38]
        val output = Array(1) { FloatArray(38) }
        interpreter.run(byteBuffer, output)

        val probabilities = softmax(output[0])
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val maxProb = if (maxIndex != -1) probabilities[maxIndex] else 0f

        return if (maxProb < 0.75f) {
            com.priyanshu.floralens.data.ClassificationResult(
                diseaseName = "No clear plant detected. Please try again.",
                confidence = maxProb,
                isPlantDetected = false
            )
        } else if (maxIndex != -1 && maxIndex < labels.size) {
            com.priyanshu.floralens.data.ClassificationResult(
                diseaseName = labels[maxIndex], // Let DiseaseDatabase format this later
                confidence = maxProb,
                isPlantDetected = true
            )
        } else {
            com.priyanshu.floralens.data.ClassificationResult(
                diseaseName = "Unknown Disease",
                confidence = maxProb,
                isPlantDetected = false
            )
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        // Model expects [1, 3, 384, 384] NCHW Float32
        val byteBuffer = ByteBuffer.allocateDirect(1 * 3 * 384 * 384 * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(384 * 384)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val mean = floatArrayOf(0.485f, 0.456f, 0.406f)
        val std  = floatArrayOf(0.229f, 0.224f, 0.225f)

        // Write all R channel values first (NCHW)
        for (pixel in intValues) {
            byteBuffer.putFloat(((pixel shr 16 and 0xFF) / 255.0f - mean[0]) / std[0])
        }
        // Write all G channel values
        for (pixel in intValues) {
            byteBuffer.putFloat(((pixel shr 8 and 0xFF) / 255.0f - mean[1]) / std[1])
        }
        // Write all B channel values
        for (pixel in intValues) {
            byteBuffer.putFloat(((pixel and 0xFF) / 255.0f - mean[2]) / std[2])
        }

        return byteBuffer
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val maxLogit = logits.maxOrNull() ?: 0f
        var sumExp = 0f
        val exps = FloatArray(logits.size) { exp(logits[it] - maxLogit).also { v -> sumExp += v } }
        for (i in exps.indices) exps[i] /= sumExp
        return exps
    }

    fun close() {
        interpreter.close()
    }
}

