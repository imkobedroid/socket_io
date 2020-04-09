package com.longyuan.im.observer

import io.socket.client.IO
import okhttp3.OkHttpClient
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager

/**
 * @author Dsh  imkobedroid@gmail.com
 * @date 2020/3/25
 */


fun getSslSocketFactory(): SSLSocketFactory {
    var ssfFactory: SSLSocketFactory? = null

    try {
        val sc = SSLContext.getInstance("TLS")
        sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())

        ssfFactory = sc.socketFactory
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ssfFactory!!
}


fun option(): IO.Options {
    val okHttpClient by lazy {
        OkHttpClient.Builder()
                .hostnameVerifier { _, _ -> true }
                .sslSocketFactory(getSslSocketFactory(), TrustAllCerts())
                .build()
    }
    IO.setDefaultOkHttpWebSocketFactory(okHttpClient)
    IO.setDefaultOkHttpCallFactory(okHttpClient)
    val option by lazy {
        IO.Options().apply {
            callFactory = okHttpClient
            webSocketFactory = okHttpClient
            reconnection = false   //关闭自动重连
        }
    }
    return option
}
