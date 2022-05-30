package com.jmm.nasaastronomicallook.common

import com.jmm.nasaastronomicallook.archetype.Result

interface Interactor<TReq, TRes> {
    suspend operator fun invoke(request: TReq): Result<Exception, TRes>
}