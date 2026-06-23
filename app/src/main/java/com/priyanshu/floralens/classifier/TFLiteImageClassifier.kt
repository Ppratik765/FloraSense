package com.priyanshu.floralens.classifier

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
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
        if (!isLikelyPlant(bitmap)) {
            return com.priyanshu.floralens.data.ClassificationResult(
                diseaseName = "No clear plant detected. Please aim at a leaf.",
                confidence = 0f,
                isPlantDetected = false
            )
        }

        val byteBuffer = convertBitmapToByteBuffer(bitmap)

        // Output shape [1, 38]
        val output = Array(1) { FloatArray(38) }
        interpreter.run(byteBuffer, output)

        val rawOutput = output[0]
        val sum = rawOutput.sum()
        // Check if output is already softmaxed
        val isAlreadySoftmax = sum in 0.9f..1.1f && rawOutput.all { it in 0.0f..1.0f }
        val probabilities = if (isAlreadySoftmax) {
            rawOutput
        } else {
            softmax(rawOutput)
        }

        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val maxProb = if (maxIndex != -1) probabilities[maxIndex] else 0f

        return if (maxProb < 0.45f) {
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

    private fun isLikelyPlant(bitmap: Bitmap): Boolean {
        // Fast heuristic to reject non-plant objects (like laptops or mugs)
        // by checking if at least 5% of the image falls into the organic plant hue spectrum (yellow to dark green)
        val scaled = Bitmap.createScaledBitmap(bitmap, 64, 64, true)
        val pixels = IntArray(64 * 64)
        scaled.getPixels(pixels, 0, 64, 0, 0, 64, 64)

        var organicPixels = 0
        val hsv = FloatArray(3)

        for (pixel in pixels) {
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF
            Color.RGBToHSV(r, g, b, hsv)
            val hue = hsv[0]
            val sat = hsv[1]
            val v = hsv[2]

            // Strict Organic plant matter: Hue between 40 (yellow-green) and 150 (deep green)
            if (hue in 40f..150f && sat > 0.25f && v > 0.15f) {
                organicPixels++
            }
        }

        if (scaled != bitmap) scaled.recycle()

        val ratio = organicPixels.toFloat() / pixels.size
        return ratio > 0.02f // Require at least 2% organic plant pixels
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val inputTensor = interpreter.getInputTensor(0)
        val shape = inputTensor.shape()
        val isNCHW = shape.size == 4 && shape[1] == 3

        val height = shape[if (isNCHW) 2 else 1]
        val width = shape[if (isNCHW) 3 else 2]
        val channels = shape[if (isNCHW) 1 else 3]

        val byteBuffer = ByteBuffer.allocateDirect(1 * channels * height * width * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        val resized = if (bitmap.width != width || bitmap.height != height) {
            Bitmap.createScaledBitmap(bitmap, width, height, true)
        } else {
            bitmap
        }

        val intValues = IntArray(width * height)
        resized.getPixels(intValues, 0, resized.width, 0, 0, resized.width, resized.height)

        val mean = floatArrayOf(0.485f, 0.456f, 0.406f)
        val std  = floatArrayOf(0.229f, 0.224f, 0.225f)

        if (isNCHW) {
            for (pixel in intValues) {
                val r = ((pixel shr 16) and 0xFF) / 255.0f
                byteBuffer.putFloat((r - mean[0]) / std[0])
            }
            for (pixel in intValues) {
                val g = ((pixel shr 8) and 0xFF) / 255.0f
                byteBuffer.putFloat((g - mean[1]) / std[1])
            }
            for (pixel in intValues) {
                val b = (pixel and 0xFF) / 255.0f
                byteBuffer.putFloat((b - mean[2]) / std[2])
            }
        } else {
            for (pixel in intValues) {
                val r = ((pixel shr 16) and 0xFF) / 255.0f
                val g = ((pixel shr 8) and 0xFF) / 255.0f
                val b = (pixel and 0xFF) / 255.0f
                byteBuffer.putFloat((r - mean[0]) / std[0])
                byteBuffer.putFloat((g - mean[1]) / std[1])
                byteBuffer.putFloat((b - mean[2]) / std[2])
            }
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

