package com.priyanshu.floralens.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshu.floralens.classifier.TFLiteImageClassifier
import com.priyanshu.floralens.data.ClassificationResult
import com.priyanshu.floralens.data.DiseaseDatabase
import com.priyanshu.floralens.data.ScanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AppState {
    object Idle : AppState
    object Analyzing : AppState
    data class Result(val classification: ClassificationResult, val scanResult: ScanResult?) : AppState
    data class Error(val message: String) : AppState
}

class MainViewModel : ViewModel() {

    private val _appState = MutableStateFlow<AppState>(AppState.Idle)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    private val _scanHistory = MutableStateFlow<List<ScanResult>>(emptyList())
    val scanHistory: StateFlow<List<ScanResult>> = _scanHistory.asStateFlow()

    private var classifier: TFLiteImageClassifier? = null

    fun setClassifier(classifier: TFLiteImageClassifier) {
        this.classifier = classifier
    }

    fun analyzeImage(bitmap: Bitmap) {
        if (_appState.value is AppState.Analyzing) return
        val currentClassifier = classifier ?: return

        viewModelScope.launch(Dispatchers.Default) {
            _appState.value = AppState.Analyzing
            try {
                val classification = currentClassifier.classify(bitmap)
                var scanResult: ScanResult? = null
                
                if (classification.isPlantDetected) {
                    val info = DiseaseDatabase.getInfo(classification.diseaseName)
                    scanResult = ScanResult(
                        id = _scanHistory.value.size + 1,
                        diseaseName = info.displayName,
                        cause = info.cause,
                        treatment = info.treatment,
                        timestamp = System.currentTimeMillis(),
                        confidence = classification.confidence
                    )
                    
                    // Add to history
                    _scanHistory.value = listOf(scanResult) + _scanHistory.value
                }
                
                _appState.value = AppState.Result(classification, scanResult)
            } catch (e: Exception) {
                _appState.value = AppState.Error(e.message ?: "An error occurred during analysis")
            }
        }
    }

    fun dismissResult() {
        _appState.value = AppState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        classifier?.close()
    }
}

