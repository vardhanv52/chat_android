package com.android.chat.managers

import android.os.Handler
import android.os.Looper
import com.android.chat.utils.Helper
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import java.net.URI

class SocketManager(private val roomId: String, actions: ISocket) {
    private val socketURL = "ws://15.207.143.29:5002"
    // private val socketURL = "ws://192.168.43.101:5002"
    private var socket: Socket? = null
    private val options = IO.Options.builder()
        .setForceNew(true)
        .setReconnection(true)
        .setReconnectionDelay(2000)
        .setReconnectionDelayMax(60000)
        .setReconnectionAttempts(100)
        .setTimeout(10000)
        .setTransports(arrayOf(WebSocket.NAME))
        .build()

    init {
        try {
            socket = IO.socket(URI.create(socketURL), options)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        Helper.log("Connecting")
        socket?.on(Socket.EVENT_CONNECT) {
            Helper.log("Connected")
        }
        socket?.on(Socket.EVENT_DISCONNECT) {
            Helper.log("Disconnected")
        }
        socket?.on(Socket.EVENT_CONNECT_ERROR) {
            Helper.log("Connect Error - ${it[0]}")
        }
        socket?.on(roomId) {
            Handler(Looper.getMainLooper()).post {
                val data = it[0] as JSONObject?
                if (data != null)
                    actions.receivedData(data)
            }
        }
        socket?.connect()
    }

    fun postMessage(data: JSONObject) {
        socket?.send(data)
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.off(roomId)
    }

    interface ISocket {
        fun receivedData(json: JSONObject)
    }
}