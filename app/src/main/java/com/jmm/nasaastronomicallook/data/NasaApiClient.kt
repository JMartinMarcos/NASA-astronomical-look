package com.jmm.nasaastronomicallook.data

import com.google.gson.Gson
import com.jmm.nasaastronomicallook.archetype.Result
import com.jmm.nasaastronomicallook.common.defaultFormat
import com.jmm.nasaastronomicallook.data.entity.ErrorInfoEntity
import com.jmm.nasaastronomicallook.domain.exception.UnknownException
import org.threeten.bp.LocalDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NasaApiClient(
    private val service: NasaService,
    private val gson: Gson,
    private val apiKey: String
) {

    private suspend inline fun <reified T> suspendCallService(crossinline getCall: () -> Call<T>): Result<Exception, T> =
        suspendCoroutine { continuation ->
            getCall().enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resume(Result.Error(UnknownException()))
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    continuation.resume(
                        when {
                            response.isValid() && body != null -> Result.Success(body)
                            response.isValid() && Unit is T -> Result.Success(Unit as T)
                            else -> Result.Error(validateResponseError(response))
                        }
                    )
                }
            })
        }

    private fun <T> Response<T>.isValid(): Boolean {
        return this.code() == 200 || this.code() == 204
    }

    private fun <T> validateResponseError(error: Response<T>): Exception {
        val errorBody = error.errorBody()
        return if (errorBody != null && errorBody.toString().isNotEmpty()) {
            val typeError = gson.fromJson(errorBody.charStream(), ErrorInfoEntity::class.java)

            parseProblem(typeError)
        } else UnknownException()
    }

    private fun parseProblem(typeError: ErrorInfoEntity): Exception {
        return when (typeError.type) {
            else -> UnknownException()
        }
    }

    suspend fun getAstronomyPictureOfTheDay() = suspendCallService {
        service.getAPOD(LocalDate.now().defaultFormat(), true, apiKey)
    }
}
