package com.jmm.nasaastronomicallook.data.mapper

import com.jmm.nasaastronomicallook.domain.exception.MapperException
import org.funktionale.either.Either

class MapperExecutor {

  inline operator fun <reified T> invoke(func: (Any) -> T): Either<Exception, T> {
    return try {
      val result = func.invoke(Any())
      Either.right(result)
    } catch (e: Exception) {
      e.printStackTrace()
      Either.left(
        MapperException(
          "Error when mapping to " + T::class.java.simpleName,
          e.message!!
        )
      )
    }
  }
}
