package com.wesal.askhail.features.chatActivity;
import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import kotlin.jvm.Synchronized;

import java.net.URISyntaxException

object SocketHandler {
    private var mSocket: Socket? = null
    @Synchronized
    fun setSocket() {
            try {
                Log.e("setSocket:","SocketInit");
                val opts = IO.Options()
                opts.forceNew = true
                opts.reconnection = true
                opts.reconnectionDelay = 1000
                opts.timeout = 1000 * 1000
                opts.transports = arrayOf(WebSocket.NAME)
                opts.transports = arrayOf("polling")
                mSocket = IO.socket("https://chat.askhail.com:8090",opts)
                //mSocket = IO.socket("http://46.151.214.37:3000")
                check()

            } catch (e: URISyntaxException) {
                Log.e("ConnectError:","err:"+e.localizedMessage);
            }
    }
    @Synchronized
    fun getSocket(): Socket? {
        return if(mSocket==null){
            setSocket()
            mSocket
        }
        else {
            mSocket
        }
    }
    private fun check() {
        mSocket?.io()
            ?.on(Manager.EVENT_TRANSPORT) { args ->
                val transport = args[0] as Transport
                transport.on(Transport.EVENT_ERROR) { args ->
                    val e = args[0] as Exception
                }
            }?.on(Socket.EVENT_DISCONNECT) { u ->
                Log.e(MySocket.Constants.tag, "EVENT_DISCONNECT")
            }?.on(Socket.EVENT_CONNECT) {
                Log.e(MySocket.Constants.tag, "EVENT_CONNECT")
            }?.on(Socket.EVENT_CONNECT_ERROR) { x ->
                Log.e("DIRECT ERRoR", "${Gson().toJson(x)}")

            }

    }
    @SuppressLint("LogNotTimber")
    @Synchronized
    fun establishConnection() {
        if (!getSocket()?.connected()!!){
            mSocket?.connect()
            mSocket?.open()
            Log.e("TryToConnect", "==> ${mSocket?.connected()}")
        }
        Log.e("IsConnect", "==> ${mSocket?.connected()}")
    }
    fun sendTextMessage() {
        Log.e("SENDING", " ")
        getSocket()?.emit("send-message", "Hiiiiiiii.")
    }
    @Synchronized
    fun closeConnection() {
        mSocket?.disconnect()
    }
}