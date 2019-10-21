package com.jmm.nasaastronomicallook.data

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.jmm.nasaastronomicallook.data.entity.ErrorInfoEntity
import com.jmm.nasaastronomicallook.domain.exception.UnknownException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.funktionale.either.Either
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val API_KEY = "DEMO_KEY"
const val DATE_REQUEST_FORMAT = "yyyy-MM-dd" // 2019-09-22

class NasaApiClient {

    private val BASE_URL = "https://api.nasa.gov/"
    private var service: NasaService
    private val gson = Gson()

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val clientBuilder = OkHttpClient.Builder()
        .addNetworkInterceptor(httpLoggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)

        clientBuilder.addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestWithUserAgent = originalRequest.newBuilder()
                .build()
            chain.proceed(requestWithUserAgent)
        }

        val typeAdapter = object : TypeAdapter<LocalDate>() {
            override fun write(out: JsonWriter?, value: LocalDate?) {
                out?.value(value.toString())
            }

            override fun read(`in`: JsonReader?): LocalDate =
                LocalDate.parse(`in`?.nextString())
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(NasaService::class.java)

    }

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

    private suspend inline fun <reified T> suspendCallService(crossinline getCall: () -> Call<T>): Either<Exception, T> =
        suspendCoroutine { continuation ->
            getCall().enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resume(Either.left(UnknownException()))
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    continuation.resume(
                        when {
                            response.isValid() && body != null -> Either.right(body)
                            response.isValid() && Unit is T -> Either.right(Unit as T)
                            else -> Either.left(validateResponseError(response))
                        }
                    )
                }
            })
        }

    private fun <T> callServiceResponse(callback: () -> Call<T>): Either<Exception, Response<T>> {
        return try {
            val response = callback().execute()
            when {
                response.isValid() -> Either.right(response)
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
        service.getAPOD(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern(DATE_REQUEST_FORMAT)), true, API_KEY)
    }
}
