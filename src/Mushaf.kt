package com.example

data class Mushaf(
    val name: String,
    val pages: Int,
    val arabicName: String,
    val subName: String,
    val url: String,
    val hasInfo: Boolean,
    val infoPages: Int,
    val mushafSize: Double,
    val infoSize: Double
)

data class HeaderItem(
    val header: String,
    val mushafs: List<Mushaf>
)

