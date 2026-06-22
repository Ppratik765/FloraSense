package com.priyanshu.floralens.data

data class ClassificationResult(
    val diseaseName: String,
    val confidence: Float,
    val isPlantDetected: Boolean
)

data class ScanResult(
    val id: Int = 0,
    val diseaseName: String,
    val cause: String,
    val treatment: String,
    val timestamp: Long,
    val confidence: Float
)
