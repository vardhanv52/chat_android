package com.android.chat.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.android.chat.retrofit.API
import com.android.chat.retrofit.WebServiceAccess

class Chat: Application() {

    override fun onCreate() {
        super.onCreate()
        val webServiceAccess = WebServiceAccess()
        context = this
        if (api == null)
            api = webServiceAccess.getApi()
    }

    companion object {
        var api: API? = null
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        val tag: String = Chat::class.java.simpleName
    }
}