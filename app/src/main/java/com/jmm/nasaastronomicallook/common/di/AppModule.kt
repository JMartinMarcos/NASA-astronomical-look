package com.jmm.nasaastronomicallook.common.di

import android.app.Application
import android.content.Context
import com.jmm.nasaastronomicallook.data.mapper.MapperExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

  @Provides
  @Singleton
  fun provideContext(application: Application): Context {
    return application
  }

  @Provides
  @Singleton
  fun provideMapperExecutor() = MapperExecutor()

}
