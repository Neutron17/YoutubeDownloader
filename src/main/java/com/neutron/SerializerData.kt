package com.neutron

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ColorSerializer(val main: String, val history: String, val settings: String) {
    init {
        require(main.isNotEmpty()) { "Main cannot be empty" }
        require(history.isNotEmpty()) { "History cannot be empty" }
        require(settings.isNotEmpty()) { "Settings cannot be empty" }
    }
}

@Serializable
data class SerializerData(val colors: ColorSerializer?, @Contextual val language: Lang)