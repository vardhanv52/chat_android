package com.android.chat.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface API {
    @POST("api/login")
    fun login(@Body map: HashMap<String, String>): Call<ResponseBody>

    @POST("api/logout")
    fun logout(
        @Header("Authorization") token: String, @Body map: HashMap<String, String>
    ): Call<ResponseBody>

    @POST("api/user")
    fun createUser(
        @Header("Authorization") token: String, @Body map: HashMap<String, String>
    ): Call<ResponseBody>

    @PATCH("api/user")
    fun updateUser(
        @Header("Authorization") token: String, @Body map: HashMap<String, Any>
    ): Call<ResponseBody>

    @GET("api/user/all/{page}/{size}")
    fun getUsers(
        @Header("Authorization") token: String, @Path("page") page: Int,
        @Path("size") size: Int, @QueryMap map: HashMap<String, String>
    ): Call<ResponseBody>

    @POST("api/chat/group")
    fun createGroup(
        @Header("Authorization") token: String, @Body map: HashMap<String, Any>
    ): Call<ResponseBody>

    @PATCH("api/user")
    fun updateGroup(
        @Header("Authorization") token: String, @Body map: HashMap<String, Any>
    ): Call<ResponseBody>

    @GET("api/chat/group/{page}/{size}")
    fun getGroups(
        @Header("Authorization") token: String, @Path("page") page: Int,
        @Path("size") size: Int, @QueryMap map: HashMap<String, String>
    ): Call<ResponseBody>

    @POST("api/chat/group-members")
    fun addGroupMembers(
        @Header("Authorization") token: String, @Body map: HashMap<String, Any>
    ): Call<ResponseBody>

    @DELETE("api/chat/group-members")
    fun deleteGroupMembers(
        @Header("Authorization") token: String, @Body map: HashMap<String, Any>
    ): Call<ResponseBody>

    @GET("api/chat/group-members")
    fun getGroupMembers(
        @Header("Authorization") token: String, @QueryMap map: HashMap<String, Any>
    ): Call<ResponseBody>

    @POST("api/chat/messages")
    fun postMessage(
        @Header("Authorization") token: String, @Body map: HashMap<String, String>
    ): Call<ResponseBody>

    @PATCH("api/chat/messages/like")
    fun likeMessage(
        @Header("Authorization") token: String, @Body map: HashMap<String, String>
    ): Call<ResponseBody>

    @GET("api/chat/messages")
    fun getMessages(
        @Header("Authorization") token: String, @QueryMap map: HashMap<String, String>
    ): Call<ResponseBody>
}