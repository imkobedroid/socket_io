package com.longyuan.im.observer

import android.content.Context
import com.longyuan.im.observer.SocketKey.ROOMID_KEY
import com.longyuan.im.observer.SocketKey.SESSION_KEY
import com.longyuan.im.observer.SocketKey.USER_ID_SEND
import org.json.JSONObject
import java.util.*

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2020/4/1
 */
class SocketManager : Observer {
    private var isCreateRoom = false
    private var timer: Timer? = null
    private lateinit var userId: String
    private lateinit var roomId: String
    private lateinit var language: String
    private lateinit var context: Context
    private lateinit var session: String
    private var time: Long = 1000
    private var actionListener: ((String, String, String) -> Unit)? = null
    private var messageListener: ((String, String, String) -> Unit)? = null
    private var gameStartListener: ((String, String, String) -> Unit)? = null
    private var memberListener: ((String) -> Unit)? = null
    private var socketStateListener: ((Boolean) -> Unit)? = null

    companion object {
        @JvmStatic
        val instance: SocketManager by lazy { SocketManager() }.also {
            SocketConfig.instance.addObserver(it.value)
        }
    }


    fun setActionListener(actionListener: (String, String, String) -> Unit): SocketManager {
        this.actionListener = actionListener
        return this
    }


    fun setMessageListener(messageListener: (String, String, String) -> Unit): SocketManager {
        this.messageListener = messageListener
        return this
    }


    fun setGameStartListener(gameStartListener: (String, String, String) -> Unit): SocketManager {
        this.gameStartListener = gameStartListener
        return this
    }


    fun setMemberListener(memberListener: (String) -> Unit): SocketManager {
        this.memberListener = memberListener
        return this
    }


    fun setSocketStateListener(messageListener: (Boolean) -> Unit): SocketManager {
        this.socketStateListener = messageListener
        return this
    }


    /**
     * 初始化socket
     */
    fun initSocket(
        context: Context,
        host: String,
        userId: String,
        roomId: String,
        language: String,
        time: Long
    ) {
        this@SocketManager.userId = userId
        this@SocketManager.roomId = roomId
        this@SocketManager.language = language
        this@SocketManager.context = context
        this@SocketManager.time = time
        val url =
            "$host?userId=${this@SocketManager.userId}&roomId=${this@SocketManager.roomId}&language=${this@SocketManager.language}"
        SocketConfig.instance.createSocket(url)
    }


    /**
     *  创建房间
     */
    fun createRoom(
        context: Context,
        host: String,
        userId: String,
        roomId: String,
        language: String,
        time: Long,
        session: String
    ) {
        isCreateRoom = true
        this.session = session
        this.roomId=roomId
        createRoom()
        initSocket(context, host, userId, roomId, language, time)
    }


    /**
     * 加入房间
     */
    fun joinRoom(session: String, roomId: String) {
        val message = JSONObject()
        message.put(SESSION_KEY, session)
        message.put(ROOMID_KEY, roomId)
        SocketConfig.instance.sendMessage(SocketKey.JOIN_ROOM, message)
    }


    /**
     * 解散房间
     */
    fun delRoom(session: String, roomId: String) {
        val message = JSONObject()
        message.put(SESSION_KEY, session)
        message.put(ROOMID_KEY, roomId)
        SocketConfig.instance.sendMessage(SocketKey.DEL_ROOM, message)
    }

    /**
     * 离开房间
     */
    fun leaveRoom(session: String, roomId: String) {
        val message = JSONObject()
        message.put(SESSION_KEY, session)
        message.put(ROOMID_KEY, roomId)
        SocketConfig.instance.sendMessage(SocketKey.LEAVE_ROOM, message)
    }

    /**
     * 玩家准备
     */
    fun ready(session: String, roomId: String) {
        val message = JSONObject()
        message.put(SESSION_KEY, session)
        message.put(ROOMID_KEY, roomId)
        SocketConfig.instance.sendMessage(SocketKey.READY_ROOM, message)
    }

    /**
     * 取消房间
     */
    fun cancel(session: String, roomId: String) {
        val message = JSONObject()
        message.put(SESSION_KEY, session)
        message.put(ROOMID_KEY, roomId)
        SocketConfig.instance.sendMessage(SocketKey.CANCEL_ROOM, message)
    }


    /**
     * 房主开始游戏
     */
    fun start(session: String, roomId: String) {
        val message = JSONObject()
        message.put(SESSION_KEY, session)
        message.put(ROOMID_KEY, roomId)
        SocketConfig.instance.sendMessage(SocketKey.START_ROOM, message)
    }


    /**
     * 广播消息（客户端需要发送和监听）
     */
    fun broadcast(userId: String, msg: String) {
        val message = JSONObject()
        message.put("userId", userId)
        message.put("msg", msg)
        SocketConfig.instance.sendMessage(SocketKey.BROADCAST_ROOM, message)
    }


    /**
     * 获取房间全部成员
     */
    fun member(session: String, roomId: String) {
        val message = JSONObject()
        message.put(SESSION_KEY, session)
        message.put(ROOMID_KEY, roomId)
        SocketConfig.instance.sendMessage(SocketKey.MEMBER, message)
    }


    override fun memberObserver(`object`: JSONObject) {
        memberListener?.let { it(`object`.toString()) }
    }


    override fun createdObserver(`object`: JSONObject) {

    }

    override fun joinedObserver(`object`: JSONObject) {
    }

    override fun delRoomObserver(`object`: JSONObject) {
    }

    override fun leaveRoomObserver(`object`: JSONObject) {
    }

    override fun readyObserver(`object`: JSONObject) {
    }

    override fun cancelObserver(`object`: JSONObject) {
    }

    override fun startObserver(`object`: JSONObject) {
    }


    override fun connectObserver(`object`: Boolean) {
        socketStateListener?.let {
            it(`object`)
        }
        when {
            `object` -> {
                if (isCreateRoom) {
                    createRoom()
                    isCreateRoom = false
                }


                Thread(Runnable {
                    timer = Timer()
                    val task = object : TimerTask() {
                        override fun run() {
                            val message = JSONObject()
                            message.put(USER_ID_SEND, userId)
                            SocketConfig.instance.sendMessage(SocketKey.HEART_BEAT, message)
                        }
                    }
                    timer?.schedule(task, 0, time)
                }).start()
            }
            else -> {
                timer?.cancel()
            }
        }
    }


    override fun gameStartObserver(`object`: JSONObject) {
        val teamId = `object`.getString("teamId")
        val token = `object`.getString("token")
        val uniqueId = `object`.getString("uniqueId")
        messageListener?.let {
            it(teamId, token, uniqueId)
        }

    }


    override fun broadcastObserver(`object`: JSONObject) {
        var username = ""
        val userId = `object`.getString("userId")
        val message = `object`.getString("msg")
        val a = `object`.has("username")
        when {
            a -> username = `object`.getString("username")
        }
        messageListener?.let {
            it(username, userId, message)
        }
    }

    override fun notifyObserver(`object`: JSONObject) {
        var username = ""
        val userId = `object`.getString("userId")
        val action = `object`.getString("action")
        val a = `object`.has("username")
        when {
            a -> username = `object`.getString("username")
        }
        actionListener?.let {
            it(username, userId, action)
        }
    }


    private fun createRoom(){
        val message = JSONObject()
        message.put(SESSION_KEY, session)
        message.put(ROOMID_KEY, roomId)
        SocketConfig.instance.sendMessage(SocketKey.CREATE_ROOM, message)
    }
}