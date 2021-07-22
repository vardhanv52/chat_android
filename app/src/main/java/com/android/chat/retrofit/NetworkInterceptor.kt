package com.android.chat.retrofit

import com.android.chat.R
import com.android.chat.utils.Chat.Companion.context
import com.android.chat.utils.Helper
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

class NetworkInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            if (Helper.isNetworkAvailable())
                return chain.proceed(chain.request().newBuilder().build())
        } catch(ex: Exception) {
            ex.printStackTrace()
        }
        val json = JSONObject()
        json.put("status", false)
        json.put("msg", context.getString(R.string.no_internet))
        return Response.Builder()
            .code(221)
            .protocol(Protocol.HTTP_2)
            .message("Sample response")
            .body(json.toString().toResponseBody("text/plain".toMediaTypeOrNull()))
            .request(chain.request())
            .build()
    }
}