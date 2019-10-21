package com.jmm.nasaastronomicallook.data

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.jmm.nasaastronomicallook.data.entity.ErrorInfoEntity
import com.jmm.nasaastronomicallook.domain.exception.UnknownException
import org.funktionale.either.Either
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val DATE_REQUEST_FORMAT = "yyyy-MM-dd" // 2019-09-22

class NasaApiClient @Inject constructor(
    private val service: NasaService,
    private val gson: Gson,
    private val apiKey: String
) {

    private fun <T> callService(callback: () -> Call<T>): Either<Exception, T> {
        return try {
            val response = callback().execute()
            when {
                response.isValid() -> {
                    if (response.body() != null) {
                        Either.right(response.body()!!)
                    } else {
                        if (response.body() is Unit?)
                            Either.right(Unit as T)
                        else
                            Either.left(validateResponseError(response))
                    }
                }
                else -> Either.left(validateResponseError(response))
            }
        } catch (exception: Exception) {
            return Either.left(UnknownException())
        }
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
            //     "problem:credit-card-date-validation-error" -> CreditCardDateException(typeError.detail)
            else -> UnknownException()
        }
    }

    fun getAstronomyPictureOfTheDay() = callService {
        service.getAPOD(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern(
                    DATE_REQUEST_FORMAT
                )
            ), true, apiKey
        )
    }
}
