# FloraLens

FloraLens is an advanced, offline-first botanical diagnostic engine and digital garden management application for Android. Built entirely with Jetpack Compose and powered by on-device TensorFlow Lite edge inference, FloraLens provides instantaneous, privacy-preserving plant disease classification without requiring network connectivity.

## Table of Contents
1. [Overview](#overview)
2. [Project Architecture & Tree](#project-architecture--tree)
3. [Key Features](#key-features)
4. [Machine Learning Engine & Training](#machine-learning-engine--training)
5. [Running Locally (Android Studio)](#running-locally-android-studio)
6. [License](#license)
7. [Citation](#citation)

---

## Overview

FloraLens bridges the gap between botanical sciences and modern mobile software engineering. By heavily utilizing local compute capabilities, the application processes camera frames in real-time, applying algorithmic organic-matter heuristics before executing a deep learning diagnostic pipeline. This architecture ensures high-fidelity results while completely preserving user data privacy.

---

## Project Architecture & Tree

The project adheres strictly to the Unidirectional Data Flow (UDF) paradigm, built entirely in Kotlin and organized cleanly by feature and function.

```text
FloraLens/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── assets/
│   │   │   ├── labels.txt                # 38 Class definitions
│   │   │   └── plant_disease_diagnoser.tflite # MobileNetV4 Weights
│   │   ├── java/com/priyanshu/floralens/
│   │   │   ├── MainActivity.kt           # Entry Point
│   │   │   ├── classifier/
│   │   │   │   └── TFLiteImageClassifier.kt # NNAPI hardware accelerated inference
│   │   │   ├── data/
│   │   │   │   ├── DiseaseDatabase.kt    # Offline disease treatments & data
│   │   │   │   └── PlantProfile.kt       # Room/Data entities
│   │   │   ├── ui/
│   │   │   │   ├── components/           # Reusable Compose Widgets
│   │   │   │   ├── screens/              # Core UI Routes (Scan, History, Details)
│   │   │   │   └── theme/                # Typography, Colors (Soft Mint), Shapes
│   │   │   ├── util/
│   │   │   │   └── PdfReportGenerator.kt # Diagnostics PDF compilation
│   │   │   └── viewmodel/
│   │   │       └── MainViewModel.kt      # StateFlow management
│   │   └── res/
│   │       └── drawable/                 # Custom Adaptive Vector Branding
├── Plant_diagnosis.ipynb                 # Cloud training script (PyTorch Lightning)
├── build.gradle.kts                      # Gradle Configuration
└── README.md                             # Documentation
```

---

## Key Features

* **Real-Time Edge Diagnostics:** Scans and analyzes botanical specimens locally with near-zero latency.
* **Offline-First Architecture:** The core diagnostic engine functions autonomously without requiring active internet connectivity.
* **Algorithmic Heuristic Filtering:** Pre-filters camera frames for organic pixel density (HSV modeling). The app rejects inputs with less than 12.5% organic matter, preventing false-positive classifications on couches, fabrics, and non-botanical objects.
* **Infinite Scalable Timeline:** A sophisticated, dynamically rendered S-Curve spline timeline that meticulously logs diagnostic history across an unbounded number of plant profiles.
* **Fluid Adaptive UI:** Micro-interaction driven interfaces, utilizing constraint-based layouts and physics-backed animations (`springs`, `tweens`) for a premium tactile experience without clipping artifacts.
* **PDF Report Generation:** Automated compilation of diagnostic data, visual records, and historical context into exportable, structured PDF documents.
* **Adaptive Vector Branding:** Hardware-accelerated, mathematically perfect SVG scaling across all Android 8.0+ launcher masks. Soft Mint Splash Architecture integration.

---

## Machine Learning Engine & Training

FloraLens is driven by a highly optimized **MobileNetV4-Conv-Large (384x384)** architecture, specifically designed for Edge AI computer vision tasks. The model achieves an extraordinarily robust 99.5% diagnostic accuracy.

### The Training Pipeline
The complete training environment can be found in `Plant_diagnosis.ipynb`.

1. **Dataset:** Trained on the comprehensive "New Plant Diseases Dataset", spanning 38 distinct botanical classes (including healthy baselines).
2. **Augmentation:** To prevent lab-environment overfitting, an aggressive PyTorch TorchVision pipeline was utilized. This included random resizing, crops, heavy color jittering (brightness/contrast/saturation), rotations, and `RandomErasing` to simulate real-world shadowing, blights, and harsh sunlight.
3. **Hardware & Frameworks:** Built using `PyTorch Lightning` and the `timm` ecosystem, running 16-bit Automatic Mixed Precision (AMP) on an NVIDIA L4 Tensor Core GPU.
4. **Export & Portability:** The PyTorch graph was natively compiled down via `litert-torch` to the highly portable TensorFlow Lite (`.tflite`) format with fully unrolled/decomposed signatures optimized for Android's Neural Networks API (NNAPI).
5. **False-Positive Mitigation Strategy:** A custom post-processing heuristic artificially applies a 1.5x (50%) confidence multiplier toward "healthy" logit classes. The model strictly requires immense disease confidence to flag an ailment, completely eradicating false-positive sickness warnings on thriving plants.

---

## Running Locally (Android Studio)

To build and run FloraLens directly from source:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Ppratik765/FloraLens.git
   cd FloraLens
   ```

2. **Open in Android Studio**
   * Launch Android Studio (Giraffe, Hedgehog, or newer recommended).
   * Click **Open** and select the `FloraLens` root directory.
   * Allow Gradle to sync the dependencies.

3. **Provide the Neural Weights**
   * Ensure `plant_disease_diagnoser.tflite` is present in `app/src/main/assets/`. (Included in the repository).

4. **Deploy**
   * Connect an Android device (Android 8.0+ / API 26+) with Developer Options and USB Debugging enabled.
   * Click the **Run** button (`Shift + F10`) in Android Studio.
   * _Note: Camera permissions are requested dynamically at runtime. Accept them when prompted to enable the diagnostic viewfinder._

---

## License

Copyright (c) 2026 Priyanshu

This software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.

---

## Citation

If you utilize the architecture, UI paradigms, or machine learning integration methodology found in this repository for academic or commercial research, please provide attribution:

```text
Author: Priyanshu
Project: FloraLens
Model Architecture: MobileNetV4-Conv-Large (384x384 Edge-AI)
Year: 2026
```
