package com.android.chat.dtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GroupDTO: Serializable {
    @SerializedName("_id")
    var id: String? = null
    var name: String? = null
    @SerializedName("members_count")
    var membersCount: Int? = null

    @SerializedName("owner_id")
    var ownerId: String? = null

    @SerializedName("inserted_at")
    var insertedAt: Long? = null
}