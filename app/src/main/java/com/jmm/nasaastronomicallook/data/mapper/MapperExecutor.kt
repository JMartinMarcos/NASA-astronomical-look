package com.jmm.nasaastronomicallook.data.mapper

import com.jmm.nasaastronomicallook.archetype.Result
import com.jmm.nasaastronomicallook.domain.exception.MapperException

class MapperExecutor {

  inline operator fun <reified T> invoke(func: (Any) -> T): Result<Exception, T> {
    return try {
      val result = func.invoke(Any())
      Result.Success(result)
    } catch (e: Exception) {
      e.printStackTrace()
      Result.Error(
        MapperException(
          "Error when mapping to " + T::class.java.simpleName,
          e.message!!
        )
      )
    }
  }
}
