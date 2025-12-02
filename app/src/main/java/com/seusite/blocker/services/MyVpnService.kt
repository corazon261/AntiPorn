package com.seusite.blocker.services

import android.content.Intent
import android.net.VpnService

class MyVpnService : VpnService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}