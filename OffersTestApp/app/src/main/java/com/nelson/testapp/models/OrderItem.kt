package com.nelson.testapp.models

import com.squareup.moshi.Json

class OrderItem(val id: String,
                val url: String?,
                val name: String,
                val description: String,
                @Json(name = "current_value") val value: String) {
    var isFavorite = false
}