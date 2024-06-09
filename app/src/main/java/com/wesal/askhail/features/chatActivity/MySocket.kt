package com.wesal.askhail.features.chatActivity

import android.util.Log
import com.google.gson.Gson
import com.wesal.entities.models.MessagesDataModel
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport
import org.json.JSONObject

object MySocket {
    object Constants {
        internal const val tag = "Socket"
        internal const val url = "https://askhail.com.sa:8090"
        internal const val EVENT_AUTHORIZE = "open-chat-way-1"
        internal const val EVENT_DISCONNECT = "close-chat"
        internal const val sendRequest = "send-message"
    }

    private var mSocket: Socket? = null
    private fun iniIt() {
        try {
            if (mSocket == null) {
                val opts = IO.Options()
                opts.forceNew = true
                opts.reconnection = true
                opts.reconnectionDelay = 1000
                opts.timeout = 1000 * 1000
                //opts.transports = arrayOf("polling")
                mSocket = IO.socket(Constants.url)
                Log.e(Constants.tag, "INIT")
                connect()
            }
        } catch (e: Exception) {
            Log.e("${Constants.tag} init", "ex ${e.message}")
        }
    }
    private fun connect() {
        mSocket?.connect()
        check()
    }
    fun isConnected(): Boolean {
        val connected = mSocket?.connected() ?: return false
//        if (!connected)
//            check()
        return connected

    }
    private fun check() {
        mSocket?.io()
                ?.on(Manager.EVENT_TRANSPORT) { args ->
                    val transport = args[0] as Transport
                    transport.on(Transport.EVENT_ERROR) { args ->
                        val e = args[0] as Exception
//                Log.e(Constants.tag, "Transport error $e")
//                e.printStackTrace()
//                e.cause?.printStackTrace()
                    }
                }?.on(Socket.EVENT_DISCONNECT) { u ->
                    Log.e(Constants.tag, "EVENT_DISCONNECT")
                }?.on(Socket.EVENT_CONNECT) {
                    Log.e(Constants.tag, "EVENT_CONNECT")
                }?.on(Socket.EVENT_ERROR) { x ->
//            Log.e("DIRECT ERRoR", "EVENT_ERROR")
                    Log.e("DIRECT ERRoR", "${Gson().toJson(x)}")

                }

    }
    private fun disconnectRoom(token: String,chatId: Int){
        val tokenObject = JSONObject()
        tokenObject.put("api_token", token)
        tokenObject.put("chat_id", chatId)
        mSocket?.emit(Constants.EVENT_DISCONNECT, tokenObject, Ack {
            Log.e("Socket Auth", "-->$it")
        })
    }
    fun authorizeSocket(token: String, roomId: Int) {
        Log.e("TAGTAG","ON")
        iniIt()
        if (true) {
            Log.e("Is Connect", "==> ${mSocket?.connected()}")
            val tokenObject = JSONObject()
            tokenObject.put("api_token", token)
            tokenObject.put("chat_id", roomId)
            mSocket?.emit(Constants.EVENT_AUTHORIZE, tokenObject, Ack {
                Log.e("Socket Auth", "-->$it")
            })
            mSocket?.on(Constants.EVENT_AUTHORIZE) { args ->
                Log.e("Socket ON Auth", "-->$args")
                val transport = args[0] as Transport
                transport.on(Transport.EVENT_ERROR) { args ->
                    val e = args[0] as Exception
                    Log.e(Constants.tag, "Transport error $e")
                    e.printStackTrace()
                    e.cause?.printStackTrace()
                }
            }
            Log.e("Is Connect", "==> ${mSocket?.connected()}")
        }
    }
    fun disconnect(roomId: Int,token: String) {
        Log.e("TAGTAG","OFF")

        disconnectRoom(token,roomId)
         mSocket?.disconnect()
        mSocket = null
    }
    fun startObserveChat(roomId: Int, callBack: OnNewMsgArrived) {
        val isActive = mSocket?.hasListeners("send-message-$roomId") ?: false
        Log.e("startSocket", "== >$Constants.hasActiveChannel")
        if (!isActive)
//            Constants.hasActiveChannel = !Constants.hasActiveChannel
            mSocket?.on("send-message-$roomId") { args ->
                val data = args[0]
                Log.e("NEWMSG DATA", "-> $data")

                data?.let { Q ->
                    val chatObject = JSONObject(Q.toString())
                    val chatModel = Gson().fromJson(Q.toString(), MsgSocketModel::class.java)
                    chatModel?.let {
                        it.data?.let { it1 -> callBack.onNewMsg(it1) }
                    }

                }
            }
    }
    interface OnNewMsgArrived {
        fun onNewMsg(model: MessagesDataModel)
    }
    //////////////////////////////////////////////////////////////////////
    fun sendTextMessage(roomId: Int, message: String, apiToken: String, language:String) {
        val messageObject = JSONObject()
        messageObject.put("chat_id", roomId.toString())
        messageObject.put("lang", language)
        messageObject.put("message", message)
        messageObject.put("api_token", apiToken)
        Log.e("SENDING", " " + messageObject.toString())
        mSocket?.emit(Constants.sendRequest, messageObject)
    }


}