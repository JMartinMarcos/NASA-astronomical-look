package com.jmm.nasaastronomicallook.domain

data class AstronomyPictureoftheDay(
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)