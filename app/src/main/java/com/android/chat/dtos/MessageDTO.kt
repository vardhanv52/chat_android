package com.android.chat.dtos

import com.google.gson.annotations.SerializedName

class MessageDTO {
    @SerializedName("_id")
    var id: String? = null
    var message: String? = null

    @SerializedName("is_liked")
    var isLiked: Boolean? = null

    @SerializedName("author_id")
    var authorId: String? = null
    var user: UserDTO? = null

    @SerializedName("inserted_at")
    var insertedAt: Long? = null
}