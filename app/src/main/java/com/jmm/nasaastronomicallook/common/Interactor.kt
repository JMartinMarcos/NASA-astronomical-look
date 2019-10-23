package com.jmm.nasaastronomicallook.common

import org.funktionale.either.Either

abstract class Interactor<TReq, TRes> {
    abstract suspend fun execute(request: TReq): Either<Throwable, TRes>
    suspend operator fun invoke(request: TReq) = execute(request)
}