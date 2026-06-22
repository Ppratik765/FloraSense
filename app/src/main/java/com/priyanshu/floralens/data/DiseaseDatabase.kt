package com.priyanshu.floralens.data

data class DiseaseInfo(
    val displayName: String,
    val cause: String,
    val treatment: String
)

object DiseaseDatabase {
    private val database = mapOf(
        "Apple___Apple_scab" to DiseaseInfo(
            displayName = "Apple Scab",
            cause = "Caused by the fungus Venturia inaequalis. It thrives in wet, cool spring weather.",
            treatment = "Remove infected leaves and fallen fruit. Apply fungicides preventatively in early spring."
        ),
        "Apple___Black_rot" to DiseaseInfo(
            displayName = "Apple Black Rot",
            cause = "Fungal infection by Botryosphaeria obtusa, entering through wounds or dead tissue.",
            treatment = "Prune out dead or diseased wood. Dispose of mummified fruits. Use targeted fungicides."
        ),
        "Apple___Cedar_apple_rust" to DiseaseInfo(
            displayName = "Cedar Apple Rust",
            cause = "Fungal disease (Gymnosporangium juniperi-virginianae) requiring both apple and cedar trees to complete its life cycle.",
            treatment = "Remove nearby cedar hosts if possible. Apply preventative fungicides during spring."
        ),
        "Apple___healthy" to DiseaseInfo(
            displayName = "Healthy Apple",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Continue regular watering, pruning, and fertilization to maintain plant health."
        ),
        "Blueberry___healthy" to DiseaseInfo(
            displayName = "Healthy Blueberry",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Maintain acidic soil pH and ensure consistent watering."
        ),
        "Cherry_(including_sour)__Powdery_mildew" to DiseaseInfo(
            displayName = "Cherry Powdery Mildew",
            cause = "Podosphaera clandestina fungus, favored by high humidity and moderate temperatures.",
            treatment = "Improve air circulation. Apply sulfur or potassium bicarbonate based fungicides."
        ),
        "Cherry(including_sour)__healthy" to DiseaseInfo(
            displayName = "Healthy Cherry",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Continue regular monitoring and good orchard hygiene."
        ),
        "Corn(maize)__Cercospora_leaf_spot Gray_leaf_spot" to DiseaseInfo(
            displayName = "Corn Gray Leaf Spot",
            cause = "Cercospora zeae-maydis fungus, thriving in warm, humid weather with prolonged leaf wetness.",
            treatment = "Use resistant hybrids. Practice crop rotation and tillage to manage crop residue."
        ),
        "Corn(maize)_Common_rust" to DiseaseInfo(
            displayName = "Corn Common Rust",
            cause = "Puccinia sorghi fungus. Spores are spread by wind in cool, moist conditions.",
            treatment = "Plant resistant varieties. Fungicide application may be necessary in severe cases."
        ),
        "Corn(maize)__Northern_Leaf_Blight" to DiseaseInfo(
            displayName = "Northern Leaf Blight",
            cause = "Exserohilum turcicum fungus. Favored by moderate temperatures and heavy dews.",
            treatment = "Use resistant hybrids and apply fungicides if lesions appear early in the season."
        ),
        "Corn(maize)healthy" to DiseaseInfo(
            displayName = "Healthy Corn",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Maintain proper nitrogen levels and soil moisture."
        ),
        "Grape___Black_rot" to DiseaseInfo(
            displayName = "Grape Black Rot",
            cause = "Guignardia bidwellii fungus. Warm, wet weather encourages spore release and infection.",
            treatment = "Remove mummified berries. Apply fungicides starting from early bud break."
        ),
        "Grape___Esca(Black_Measles)" to DiseaseInfo(
            displayName = "Grape Esca",
            cause = "A complex of fungi entering through pruning wounds, causing wood decay and leaf striping.",
            treatment = "Prune out infected wood. Disinfect pruning tools to prevent spread."
        ),
        "Grape___Leaf_blight(Isariopsis_Leaf_Spot)" to DiseaseInfo(
            displayName = "Grape Leaf Blight",
            cause = "Pseudocercospora vitis fungus. Occurs in wet, humid conditions.",
            treatment = "Improve canopy airflow to reduce humidity. Apply appropriate fungicides."
        ),
        "Grape___healthy" to DiseaseInfo(
            displayName = "Healthy Grape",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Ensure good canopy management and balanced fertilization."
        ),
        "Orange___Haunglongbing(Citrus_greening)" to DiseaseInfo(
            displayName = "Citrus Greening",
            cause = "Candidatus Liberibacter asiaticus bacteria, spread by the Asian citrus psyllid insect.",
            treatment = "No cure exists. Remove infected trees to prevent spread. Control psyllid populations."
        ),
        "Peach___Bacterial_spot" to DiseaseInfo(
            displayName = "Peach Bacterial Spot",
            cause = "Xanthomonas campestris bacteria. Spreads via splashing rain and wind.",
            treatment = "Plant resistant varieties. Apply copper-based bactericides during dormancy."
        ),
        "Peach___healthy" to DiseaseInfo(
            displayName = "Healthy Peach",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Continue regular pruning and pest management."
        ),
        "Pepper,_bell___Bacterial_spot" to DiseaseInfo(
            displayName = "Pepper Bacterial Spot",
            cause = "Xanthomonas bacteria. Thrives in high humidity and warm weather.",
            treatment = "Use disease-free seeds. Apply copper sprays early in the infection cycle."
        ),
        "Pepper,_bell___healthy" to DiseaseInfo(
            displayName = "Healthy Pepper",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Ensure proper spacing for air circulation and avoid overhead watering."
        ),
        "Potato___Early_blight" to DiseaseInfo(
            displayName = "Potato Early Blight",
            cause = "Alternaria solani fungus. Often occurs on older leaves during warm, wet periods.",
            treatment = "Ensure adequate nitrogen. Apply preventative fungicides and practice crop rotation."
        ),
        "Potato___Late_blight" to DiseaseInfo(
            displayName = "Potato Late Blight",
            cause = "Phytophthora infestans oomycete. Spreads rapidly in cool, wet conditions.",
            treatment = "Apply specific fungicides immediately. Destroy infected foliage to protect tubers."
        ),
        "Potato___healthy" to DiseaseInfo(
            displayName = "Healthy Potato",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Maintain consistent soil moisture and monitor for pests."
        ),
        "Raspberry___healthy" to DiseaseInfo(
            displayName = "Healthy Raspberry",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Prune old canes to encourage new growth and airflow."
        ),
        "Soybean___healthy" to DiseaseInfo(
            displayName = "Healthy Soybean",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Monitor for common pests and ensure proper weed control."
        ),
        "Squash___Powdery_mildew" to DiseaseInfo(
            displayName = "Squash Powdery Mildew",
            cause = "Various fungi causing white powdery spots. Favored by high humidity, not necessarily rain.",
            treatment = "Improve air circulation. Apply neem oil, sulfur, or potassium bicarbonate."
        ),
        "Strawberry___Leaf_scorch" to DiseaseInfo(
            displayName = "Strawberry Leaf Scorch",
            cause = "Diplocarpon earlianum fungus. Splashing water spreads spores to healthy tissue.",
            treatment = "Remove infected leaves. Use drip irrigation instead of overhead watering."
        ),
        "Strawberry___healthy" to DiseaseInfo(
            displayName = "Healthy Strawberry",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Maintain mulch layer to keep fruit off the soil and retain moisture."
        ),
        "Tomato___Bacterial_spot" to DiseaseInfo(
            displayName = "Tomato Bacterial Spot",
            cause = "Xanthomonas bacteria. Spread by rain splash and contaminated tools.",
            treatment = "Use copper-based sprays. Avoid handling plants when they are wet."
        ),
        "Tomato___Early_blight" to DiseaseInfo(
            displayName = "Tomato Early Blight",
            cause = "Alternaria linariae fungus. Starts on lower leaves during warm, humid conditions.",
            treatment = "Remove lower infected leaves. Apply mulch to prevent soil splashing. Use fungicides."
        ),
        "Tomato___Late_blight" to DiseaseInfo(
            displayName = "Tomato Late Blight",
            cause = "Phytophthora infestans. Devastating in cool, wet weather.",
            treatment = "Remove and destroy infected plants immediately. Preventative fungicides are crucial."
        ),
        "Tomato___Leaf_Mold" to DiseaseInfo(
            displayName = "Tomato Leaf Mold",
            cause = "Passalora fulva fungus. Typically a problem in high humidity environments like greenhouses.",
            treatment = "Improve ventilation and reduce humidity. Prune to increase air flow."
        ),
        "Tomato___Septoria_leaf_spot" to DiseaseInfo(
            displayName = "Septoria Leaf Spot",
            cause = "Septoria lycopersici fungus. Very common in wet, humid weather.",
            treatment = "Remove infected lower leaves. Water at the base. Apply fungicidal sprays."
        ),
        "Tomato___Spider_mites Two-spotted_spider_mite" to DiseaseInfo(
            displayName = "Spider Mites",
            cause = "Tiny arachnids (Tetranychus urticae) that suck plant sap, thriving in hot, dry conditions.",
            treatment = "Spray plants with a strong stream of water. Use horticultural oils or insecticidal soap."
        ),
        "Tomato___Target_Spot" to DiseaseInfo(
            displayName = "Tomato Target Spot",
            cause = "Corynespora cassiicola fungus. Favored by high humidity and free moisture.",
            treatment = "Improve airflow through pruning. Apply appropriate fungicides."
        ),
        "Tomato___Tomato_Yellow_Leaf_Curl_Virus" to DiseaseInfo(
            displayName = "Yellow Leaf Curl Virus",
            cause = "Begomovirus transmitted by the silverleaf whitefly.",
            treatment = "Use resistant varieties. Control whitefly populations. Remove infected plants."
        ),
        "Tomato___Tomato_mosaic_virus" to DiseaseInfo(
            displayName = "Tomato Mosaic Virus",
            cause = "Highly contagious virus spread mechanically via contaminated hands, tools, or seed.",
            treatment = "No cure. Destroy infected plants. Disinfect hands and tools frequently."
        ),
        "Tomato___healthy" to DiseaseInfo(
            displayName = "Healthy Tomato",
            cause = "No disease detected. Optimal growing conditions maintained.",
            treatment = "Continue regular watering, provide support/trellising, and monitor for pests."
        )
    )

    fun getInfo(label: String): DiseaseInfo {
        return database[label] ?: DiseaseInfo(
            displayName = label.replace("_", " ").trim(),
            cause = "Unknown cause. Ensure plant is receiving adequate light, water, and nutrients.",
            treatment = "Monitor the plant closely for worsening symptoms. Consult a local nursery if needed."
        )
    }
}
