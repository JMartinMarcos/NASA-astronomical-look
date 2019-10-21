package com.jmm.nasaastronomicallook.common

import org.funktionale.either.Either

abstract class Interactor<TReq, TRes> {

    abstract suspend fun execute(request: TReq): Either<Throwable, TRes>

    suspend operator fun invoke(request: TReq, onError: (Throwable) -> Unit = {}, onSuccess: (TRes) -> Unit = {}) {
            execute(request).fold(onError, onSuccess)
    }
}