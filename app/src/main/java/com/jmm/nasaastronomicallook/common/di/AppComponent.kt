package com.jmm.nasaastronomicallook.common.di

import android.app.Application
import com.jmm.nasaastronomicallook.NasaAstronomicalLookApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [AndroidSupportInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    ActivityBuilder::class]
)
interface AppComponent : AndroidInjector<NasaAstronomicalLookApp> {

  override fun inject(app: NasaAstronomicalLookApp)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }
}
