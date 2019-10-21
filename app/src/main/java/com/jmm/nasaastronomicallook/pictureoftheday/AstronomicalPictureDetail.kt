package com.jmm.nasaastronomicallook.pictureoftheday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.util.CoilUtils
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jmm.nasaastronomicallook.R
import com.jmm.nasaastronomicallook.data.repository.AstronomyPictureOfTheDayRepositoryImpl
import com.jmm.nasaastronomicallook.databinding.ActivityAstronomicalPictureDetailBinding
import com.jmm.nasaastronomicallook.pictureoftheday.interactor.GetAstronomyPictureOfTheDayInteractor
import com.jmm.nasaastronomicallook.pictureoftheday.viewModel.AstronomicalPictureOfTheDayViewModel
import com.jmm.nasaastronomicallook.pictureoftheday.viewModel.AstronomicalPictureOfTheDayViewModelFactory
import okhttp3.OkHttpClient

class AstronomicalPictureDetail : AppCompatActivity() {

    private lateinit var binding: ActivityAstronomicalPictureDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_astronomical_picture_detail)
        val viewModel = ViewModelProviders.of(
            this,
            AstronomicalPictureOfTheDayViewModelFactory(GetAstronomyPictureOfTheDayInteractor(AstronomyPictureOfTheDayRepositoryImpl()))

        )[AstronomicalPictureOfTheDayViewModel::class.java]
        configImageLoader()
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }

    private fun configImageLoader() {
        Coil.setDefaultImageLoader {
            ImageLoader(applicationContext) {
                crossfade(500)
                allowRgb565(false)
                availableMemoryPercentage(0.5)
                bitmapPoolPercentage(0.5)
                allowHardware(false)
                okHttpClient {
                    OkHttpClient.Builder()
                        .cache(CoilUtils.createDefaultCache(applicationContext))
                        .build()
                }
                componentRegistry {
                    add(GifDecoder())
                }
            }
        }
    }
}
