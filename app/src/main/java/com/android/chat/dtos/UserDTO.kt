package com.android.chat.dtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserDTO() : Serializable {
    @SerializedName("_id")
    var id: String? = null
    var name: String? = null
    var email: String? = null

    @SerializedName("country_code")
    var countryCode: String? = null
    var mobile: String? = null
    var role: String? = null

    @SerializedName("inserted_at")
    var insertedAt: Long? = null
}