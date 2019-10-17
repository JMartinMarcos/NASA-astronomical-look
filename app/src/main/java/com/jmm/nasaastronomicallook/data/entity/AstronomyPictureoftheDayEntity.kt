package com.jmm.nasaastronomicallook.data.entity

data class AstronomyPictureoftheDayEntity(
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)