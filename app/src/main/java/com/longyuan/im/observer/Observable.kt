package com.longyuan.im.observer

import org.json.JSONObject
import java.util.*


/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2019-12-20
 * 观察者设计模式构建消息转发机制
 */
abstract class Observable {


    /**
     * 观察者的集合
     */
    private val vector = Vector<Observer>()


    /**
     * 增加一个观察者
     */
    fun addObserver(observer: Observer) {
        vector.add(observer)
    }


    /**
     * 删除一个观察者
     */
    fun deleteObserver(observer: Observer) {
        vector.remove(observer)
    }


    fun createdObserver(book: JSONObject) {
        for (observer in vector) {
            observer.createdObserver(book)
        }
    }


    fun joinedObserver(book: JSONObject) {
        for (observer in vector) {
            observer.joinedObserver(book)
        }
    }


    fun delRoomObserver(book: JSONObject) {
        for (observer in vector) {
            observer.delRoomObserver(book)
        }
    }


    fun leaveRoomObserver(book: JSONObject) {
        for (observer in vector) {
            observer.leaveRoomObserver(book)
        }
    }

    fun readyObserver(book: JSONObject) {
        for (observer in vector) {
            observer.readyObserver(book)
        }
    }

    fun cancelObserver(book: JSONObject) {
        for (observer in vector) {
            observer.cancelObserver(book)
        }
    }


    fun startObserver(book: JSONObject) {
        for (observer in vector) {
            observer.startObserver(book)
        }
    }


    fun gameStartObserver(book: JSONObject) {
        for (observer in vector) {
            observer.gameStartObserver(book)
        }
    }

    fun memberObserver(book: JSONObject) {
        for (observer in vector) {
            observer.memberObserver(book)
        }
    }


    fun broadcastObserver(book: JSONObject) {
        for (observer in vector) {
            observer.broadcastObserver(book)
        }
    }


    fun notifyObserver(s: JSONObject) {
        for (observer in vector) {
            observer.notifyObserver(s)
        }
    }


    fun socketStateObserver(s: Boolean) {
        for (observer in vector) {
            observer.connectObserver(s)
        }
    }

}