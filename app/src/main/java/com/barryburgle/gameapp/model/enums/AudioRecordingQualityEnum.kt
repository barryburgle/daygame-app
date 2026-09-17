package com.barryburgle.gameapp.model.enums

enum class AudioRecordingQualityEnum(
    val description: String,
    val bitrate: Int,
    val sampleRate: Int,
    val value: Int
) {
    LOWEST("lowest", 24_000, 16_000, 0),
    LOW("low", 48_000, 16_000, 1), // Ideal for Speech-to-Text
    MEDIUM("medium", 96_000, 22_005, 2),
    HIGH("high", 128_000, 44_100, 3),
    VERY_HIGH("very_high", 256_000, 48_000, 4);

    companion object {
        fun getDefaultValue(): String = LOW.description

        fun fromKey(value: String?): AudioRecordingQualityEnum {
            return entries.find { it.description == value } ?: LOW
        }

        fun fromInt(value: Int): AudioRecordingQualityEnum {
            return entries.getOrNull(value) ?: LOW
        }

        fun getRange(): IntRange = 0..entries.lastIndex

        fun getLabels(): List<String> {
            return entries.map { entry ->
                entry.description.split("_")
                    .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
            }
        }
    }
}