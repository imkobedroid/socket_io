package com.longyuan.im.observer

import com.longyuan.im.observer.SocketKey.BROADCAST_ROOM
import com.longyuan.im.observer.SocketKey.CANCEL_ROOM
import com.longyuan.im.observer.SocketKey.CONNECT
import com.longyuan.im.observer.SocketKey.CREATE_ROOM
import com.longyuan.im.observer.SocketKey.DEL_ROOM
import com.longyuan.im.observer.SocketKey.DISCONNECT
import com.longyuan.im.observer.SocketKey.GAMESTART
import com.longyuan.im.observer.SocketKey.JOIN_ROOM
import com.longyuan.im.observer.SocketKey.LEAVE_ROOM
import com.longyuan.im.observer.SocketKey.MEMBER
import com.longyuan.im.observer.SocketKey.NOTIFY_ROOM
import com.longyuan.im.observer.SocketKey.READY_ROOM
import com.longyuan.im.observer.SocketKey.START_ROOM
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-12-20
 */
class SocketConfig : Observable() {

    private var client: Socket? = null

    companion object {
        val instance by lazy { SocketConfig() }
    }


    fun createSocket(host: String) {
        if (client == null) {
            val client by lazy {
                IO.socket(host, option()).apply {
                    on(CREATE_ROOM, createRoomListener)
                    on(JOIN_ROOM, joinRoomListener)
                    on(DEL_ROOM, delRoomListener)
                    on(LEAVE_ROOM, leaveRoomListener)
                    on(READY_ROOM, readyListener)
                    on(CANCEL_ROOM, cancelListener)
                    on(START_ROOM, startListener)
                    on(BROADCAST_ROOM, broadcastListener)
                    on(NOTIFY_ROOM, notifyListener)
                    on(CONNECT, connectListener)
                    on(DISCONNECT, disconnectListener)
                    on(GAMESTART, gameStartListener)
                    on(MEMBER, memberListener)
                    connect()
                }
            }
            this.client = client
        } else {
            if (!client!!.connected()) {
                client!!.connect()
            }
        }
    }


    /**
     * 获取socket对象
     */
    fun getSocketClient(): Socket? {
        client?.let { return if (it.connected()) it else null }
        return null
    }


    private val readyListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.readyObserver(data)

    }


    private val cancelListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.cancelObserver(data)
    }


    private val notifyListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.notifyObserver(data)
    }


    private val connectListener = Emitter.Listener {
        super.socketStateObserver(true)
    }


    private val disconnectListener = Emitter.Listener {
        super.socketStateObserver(false)
    }


    private val gameStartListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.gameStartObserver(data)
    }


    private val memberListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.memberObserver(data)
    }


    private val startListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.startObserver(data)
    }


    private val createRoomListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.createdObserver(data)
    }


    private val joinRoomListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.joinedObserver(data)
    }


    private val broadcastListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.broadcastObserver(data)
    }


    private val delRoomListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.delRoomObserver(data)
    }


    private val leaveRoomListener = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        super.leaveRoomObserver(data)
    }


    fun sendMessage(event: String, message: JSONObject) {
        client?.emit(event, message)
    }
}