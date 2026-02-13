package com.example.lab3

import com.google.gson.annotations.SerializedName

class NationalPark {
    @SerializedName("fullName")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("states")
    var location: String? = null

    @SerializedName("images")
    var images: List<Image>? = null

    val imageUrl: String? get() = images?.firstOrNull()?.url

    class Image {
        @SerializedName("url")
        var url: String? = null
    }
}
