package com.jmm.nasaastronomicallook

import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.util.CoilUtils
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jmm.nasaastronomicallook.common.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import okhttp3.OkHttpClient


class NasaAstronomicalLookApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        configImageLoader()
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

    override fun applicationInjector(): AndroidInjector<NasaAstronomicalLookApp> {
        val component = DaggerAppComponent.builder().application(this).build()
        component.inject(this)
        return component
    }

}
