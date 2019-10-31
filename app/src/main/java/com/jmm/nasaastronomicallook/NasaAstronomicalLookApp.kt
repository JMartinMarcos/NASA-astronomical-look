package com.jmm.nasaastronomicallook

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.util.CoilUtils
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jmm.nasaastronomicallook.common.di.appModule
import com.jmm.nasaastronomicallook.common.di.networkModule
import com.jmm.nasaastronomicallook.pictureoftheday.di.pictureOfTheDayModule
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NasaAstronomicalLookApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        configImageLoader()
        startKoin{
            androidLogger()
            androidContext(this@NasaAstronomicalLookApp)
            modules(listOf(appModule, networkModule, pictureOfTheDayModule))
        }
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
