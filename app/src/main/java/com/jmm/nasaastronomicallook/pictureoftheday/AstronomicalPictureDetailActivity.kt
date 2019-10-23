package com.jmm.nasaastronomicallook.pictureoftheday

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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

    private val viewModel by lazy { createViewModel { astronomicalPictureOfTheDayViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_astronomical_picture_detail)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.errorResponse.observe(this, Observer { error ->
           error?.let {
               Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
           }
        })
    }
}