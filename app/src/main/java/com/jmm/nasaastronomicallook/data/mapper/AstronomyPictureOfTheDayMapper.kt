package com.jmm.nasaastronomicallook.data.mapper

import com.jmm.nasaastronomicallook.data.entity.AstronomyPictureoftheDayEntity
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay

class AstronomyPictureOfTheDayMapper {

    fun mapAstronomyPictureOfTheDay(astronomyPictureoftheDay: AstronomyPictureoftheDayEntity) =
        AstronomyPictureoftheDay(
            date = astronomyPictureoftheDay.date,
            explanation = astronomyPictureoftheDay.explanation,
            hdurl = astronomyPictureoftheDay.hdurl,
            media_type = astronomyPictureoftheDay.media_type,
            service_version = astronomyPictureoftheDay.service_version,
            title = astronomyPictureoftheDay.title,
            url = astronomyPictureoftheDay.url
        )
}