package com.longyuan.im.observer

import org.json.JSONObject


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-12-20
 * 观察者设计模式构建消息转发机制
 */
interface Observer {
    fun createdObserver(`object`: JSONObject)
    fun joinedObserver(`object`: JSONObject)
    fun delRoomObserver(`object`: JSONObject)
    fun leaveRoomObserver(`object`: JSONObject)
    fun readyObserver(`object`: JSONObject)
    fun cancelObserver(`object`: JSONObject)
    fun startObserver(`object`: JSONObject)
    fun broadcastObserver(`object`: JSONObject)
    fun notifyObserver(`object`: JSONObject)
    fun connectObserver(`object`: Boolean)
    fun gameStartObserver(`object`: JSONObject)
    fun memberObserver(`object`: JSONObject)
}