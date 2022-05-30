package com.jmm.nasaastronomicallook.domain.exception


class UnknownException : Exception() {
    override val message: String
        get() = "Internal service error"
}