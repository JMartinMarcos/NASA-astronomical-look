package com.jmm.nasaastronomicallook.data.mapper

import com.jmm.nasaastronomicallook.data.entity.AstronomyPictureoftheDayEntity
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay

class AstronomyPictureOfTheDayMapper {

    fun mapAstronomyPictureOfTheDay(astronomyPictureOfTheDay: AstronomyPictureoftheDayEntity) =
        with(astronomyPictureOfTheDay) {
            AstronomyPictureoftheDay(
                date = date,
                explanation = explanation,
                hdurl = hdurl,
                media_type = media_type,
                service_version = service_version,
                title = title,
                url = url
            )
        }
}