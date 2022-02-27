package com.jmm.nasaastronomicallook.pictureoftheday

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jmm.nasaastronomicallook.R
import com.jmm.nasaastronomicallook.databinding.ActivityAstronomicalPictureDetailBinding
import com.jmm.nasaastronomicallook.pictureoftheday.viewModel.AstronomicalPictureOfTheDayViewModel
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class AstronomicalPictureDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAstronomicalPictureDetailBinding
    private val viewModel: AstronomicalPictureOfTheDayViewModel by currentScope.viewModel(this)

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