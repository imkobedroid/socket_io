package com.longyuan.im

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.longyuan.im.observer.SocketKey.ROOMID
import com.longyuan.im.observer.SocketKey.SESSION
import com.longyuan.im.observer.SocketKey.USERID
import com.longyuan.im.observer.SocketManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSocket()
        initClick()
    }


    private fun initClick() {
        create.setOnClickListener {
            SocketManager.instance.createRoom(
                this,
                "http://172.16.210.48:18081",
                "5e7af6efba46ab313864bc68",
                ROOMID,
                "zh-CN",
                1000,
                SESSION
            )

        }
        join.setOnClickListener { SocketManager.instance.joinRoom(SESSION, ROOMID) }
        delRoom.setOnClickListener { SocketManager.instance.delRoom(SESSION, ROOMID) }
        leaveRoom.setOnClickListener { SocketManager.instance.leaveRoom(SESSION, ROOMID) }
        ready.setOnClickListener { SocketManager.instance.ready(SESSION, ROOMID) }
        cancel.setOnClickListener { SocketManager.instance.cancel(SESSION, ROOMID) }
        start.setOnClickListener { SocketManager.instance.start(SESSION, ROOMID) }
        broadcast.setOnClickListener { SocketManager.instance.broadcast(USERID, "我发送的文本消息") }
    }

    private fun initSocket() {
        SocketManager.instance.apply {
            setActionListener { _, _, s2 ->
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        s2,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            setMessageListener { _, _, s2 ->
                runOnUiThread {
                    content.text = s2
                    Toast.makeText(this@MainActivity, "收到消息:$s2", Toast.LENGTH_SHORT).show()

                }
            }
            setMemberListener {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "获取成员成功", Toast.LENGTH_SHORT).show()
                }
            }
            setSocketStateListener {
                runOnUiThread {
                    if (it) Toast.makeText(
                        this@MainActivity,
                        "链接服务器成功",
                        Toast.LENGTH_SHORT
                    ).show() else Toast.makeText(
                        this@MainActivity,
                        "链接服务器失败",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            setGameStartListener { _, _, _ ->
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "游戏开始", Toast.LENGTH_SHORT).show()
                }
            }
        }
        initClient()
    }


    private fun initClient() {
        SocketManager.instance.initSocket(
            this,
            "http://172.16.210.48:18081",
            "5e7af6efba46ab313864bc68",
            ROOMID,
            "zh-CN",
            1000
        )
    }

}


