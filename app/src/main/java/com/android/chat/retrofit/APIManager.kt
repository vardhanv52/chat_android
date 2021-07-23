package com.android.chat.retrofit

import com.android.chat.utils.Chat.Companion.api
import com.android.chat.utils.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object APIManager {

    suspend fun login(email: String, password: String): JSONObject {
        val map = HashMap<String, String>()
        map["email"] = email
        map["password"] = password
        map["fcm_token"] = "sample-token"
        return withContext(Dispatchers.IO) {
            val resp = api?.login(map)?.execute()
            return@withContext if (resp?.isSuccessful == true) {
                val json = JSONObject(resp.body()!!.string())
                json.put("token", resp.headers()["token"])
                json
            } else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun logout(): JSONObject {
        val map = HashMap<String, String>()
        map["fcm-token"] = "sample-token"
        return withContext(Dispatchers.IO) {
            val resp = api?.logout(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun createUser(
        name: String, email: String, mobile: String, role: String, password: String
    ): JSONObject {
        val map = HashMap<String, String>()
        map["name"] = name
        map["email"] = email
        map["mobile"] = mobile
        map["role"] = role
        map["password"] = password
        return withContext(Dispatchers.IO) {
            val resp = api?.createUser(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun updateUser(map: HashMap<String, Any>): JSONObject {
        return withContext(Dispatchers.IO) {
            val resp = api?.updateUser(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun getUsers(pageNo: Int, size: Int, query: String): JSONObject {
        val map = HashMap<String, String>()
        map["search"] = query
        return withContext(Dispatchers.IO) {
            val resp = api?.getUsers(Helper.getHeaderAuthorization(), pageNo, size, map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun createGroup(name: String, members: ArrayList<String>): JSONObject {
        val map = HashMap<String, Any>()
        map["name"] = name
        map["members"] = members
        return withContext(Dispatchers.IO) {
            val resp = api?.createGroup(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun updateGroup(id: String, name: String, isActive: Boolean): JSONObject {
        val map = HashMap<String, Any>()
        map["group_id"] = id
        map["name"] = name
        map["is_active"] = isActive
        return withContext(Dispatchers.IO) {
            val resp = api?.updateGroup(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun getGroups(pageNo: Int, size: Int, query: String): JSONObject {
        val map = HashMap<String, String>()
        map["search"] = query
        return withContext(Dispatchers.IO) {
            val resp = api?.getGroups(Helper.getHeaderAuthorization(), pageNo, size, map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun addGroupMembers(id: String, members: ArrayList<String>): JSONObject {
        val map = HashMap<String, Any>()
        map["group_id"] = id
        map["members"] = members
        return withContext(Dispatchers.IO) {
            val resp = api?.getGroupMembers(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun removeGroupMembers(id: String, members: ArrayList<String>): JSONObject {
        val map = HashMap<String, Any>()
        map["group_id"] = id
        map["members"] = members
        return withContext(Dispatchers.IO) {
            val resp = api?.deleteGroupMembers(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun getGroupMembers(id: String): JSONObject {
        val map = HashMap<String, Any>()
        map["group_id"] = id
        return withContext(Dispatchers.IO) {
            val resp = api?.getGroupMembers(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun postMessage(id: String, msg: String): JSONObject {
        val map = HashMap<String, String>()
        map["group_id"] = id
        map["message"] = msg
        return withContext(Dispatchers.IO) {
            val resp = api?.postMessage(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun like(id: String): JSONObject {
        val map = HashMap<String, String>()
        map["message_id"] = id
        return withContext(Dispatchers.IO) {
            val resp = api?.likeMessage(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }

    suspend fun getMessages(id: String): JSONObject {
        val map = HashMap<String, String>()
        map["group_id"] = id
        return withContext(Dispatchers.IO) {
            val resp = api?.getMessages(Helper.getHeaderAuthorization(), map)?.execute()
            return@withContext if (resp?.isSuccessful == true)
                JSONObject(resp.body()!!.string())
            else
                JSONObject(resp?.errorBody()!!.string())
        }
    }
}