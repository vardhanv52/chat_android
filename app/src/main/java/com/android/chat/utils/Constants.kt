package com.android.chat.utils

import java.text.SimpleDateFormat
import java.util.*

object Constants {
    val dd_mm_yyyy_dd_mm_a = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ROOT)

    const val ROLE_ADMIN = "ADMIN"
    const val ROLE_USER = "USER"

    const val keyToken = "token"
    const val keyUserDTO = "user_dto"
}