package ru.flx.hodlhomework.repositories.rest_api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class TextConverter private constructor() : Converter.Factory() {


    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation?>,
        methodAnnotations: Array<out Annotation?>,
        retrofit: Retrofit
    ): Converter<String, RequestBody>? {
        return if (type == String::class.java) {
            Converter { value -> RequestBody.create(okhttp3.MediaType.parse("text/plain"), value) }
        } else {
            null
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation?>,
        retrofit: Retrofit
    ): Converter<ResponseBody, String>? {
        return if (type == String::class.java) {
            Converter { value -> value.string() }
        } else {
            null
        }
    }

    companion object {
        fun create() = TextConverter()
    }
}