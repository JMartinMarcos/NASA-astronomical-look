package com.jmm.nasaastronomicallook.archetype

sealed class Result<out E, out S> {

    data class Success<out S>(val success: S) : Result<Nothing, S>()
    data class Error<out L>(val error: L) : Result<L, Nothing>()

    val isSuccess get() = this is Success<S>
    val isError get() = this is Error<E>

    fun <R> success(b: R) = Success(b)

    fun <L> error(a: L) = Error(a)

    fun fold(
        fnL: (E) -> Any,
        fnR: (S) -> Any
    ): Any =
        when (this) {
            is Error -> fnL(error)
            is Success -> fnR(success)
        }
}

fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Result<L, R>.mapError(fn: (L) -> T): Result<T, R> {
    return when (this) {
        is  Result.Error ->  Result.Error(fn(error))
        is Result.Success -> Result.Success(success)
    }
}

fun <T, E, S> Result<E, S>.flatMap(fn: (S) -> Result<E, T>): Result<E, T> =
    when (this) {
        is  Result.Error ->  Result.Error(error)
        is Result.Success -> fn(success)
    }

fun <T, E, S> Result<E, S>.map(fn: (S) -> (T)): Result<E, T> = this.flatMap(fn.c(::success))

fun <E, S> Result<E, S>.getOrElse(value: S): S =
    when (this) {
        is  Result.Error -> value
        is Result.Success -> success
    }

fun <E, S> Result<E, S>.getOrNull(): S? =
    when (this) {
        is  Result.Error -> null
        is Result.Success -> success
    }

fun <E, S> Result<E, S>.getErrorOrNull(): E? =
    when (this) {
        is  Result.Error -> error
        is Result.Success -> null
    }
