package ru.flx.hodlhomework.common.base

import org.json.JSONObject
import retrofit2.Response
import ru.flx.hodlhomework.common.exceptions.MempoolException

abstract class BaseInteractor {

    suspend fun <T>obtainResponse(block: suspend () -> Response<T>): T {
        val response = block.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            throw MempoolException(onFailRequest(errorBody))
        }
    }

    private fun onFailRequest(errorBody: String?): String {
        return try {
            val jsonPart = errorBody?.substring(errorBody.indexOf('{'))
            val json = JSONObject(jsonPart)
            val code = json.getInt("code")
            val message = json.getString("message")
            "Error $code: $message"
        } catch (e: Exception) {
            "Unknown error"
        }
    }
}