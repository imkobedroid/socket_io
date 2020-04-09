package com.longyuan.im.observer

import android.annotation.SuppressLint

import java.security.cert.X509Certificate

import javax.net.ssl.X509TrustManager

/**
 * SSL全部信任
 *
 * @author Dsh
 */
class TrustAllCerts : X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        return arrayOfNulls(0)
    }
}