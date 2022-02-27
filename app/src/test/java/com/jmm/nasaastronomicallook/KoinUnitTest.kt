package com.jmm.nasaastronomicallook

import android.content.Context
import com.jmm.nasaastronomicallook.common.di.moduleList
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.test.check.checkModules

class KoinUnitTest {

    private val context = mock<Context>()

    @Test
    fun `check MVVM hierarchy`() {
        startKoin {
            androidLogger()
            androidContext(context)
            modules(moduleList)
        }.checkModules()
    }
}
