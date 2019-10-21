package com.jmm.nasaastronomicallook.pictureoftheday

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jmm.nasaastronomicallook.R
import com.jmm.nasaastronomicallook.common.createViewModel
import com.jmm.nasaastronomicallook.databinding.ActivityAstronomicalPictureDetailBinding
import com.jmm.nasaastronomicallook.pictureoftheday.viewModel.AstronomicalPictureOfTheDayViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class AstronomicalPictureDetailActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityAstronomicalPictureDetailBinding

    @Inject
    lateinit var astronomicalPictureOfTheDayViewModel: AstronomicalPictureOfTheDayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_astronomical_picture_detail)
        val viewModel by lazy { createViewModel { astronomicalPictureOfTheDayViewModel } }
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
    }
}