package com.seuapp.blocker.services

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

class BlockerVpnService : VpnService() {

    private var thread: Thread? = null
    private var vpnInterface: ParcelFileDescriptor? = null
    private var isRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            setupVpn()
            startTrafficHandler()
        }
        return START_STICKY
    }

    private fun setupVpn() {
        try {
            val builder = Builder()
            builder.addAddress("10.0.0.2", 32) // IP local da VPN
            builder.addRoute("0.0.0.0", 0) // Roteia todo o tráfego
            builder.setSession("Proteção Ativa")
            builder.setBlocking(true)
            vpnInterface = builder.establish()
            Log.d("VPN", "Interface estabelecida")
        } catch (e: Exception) {
            e.printStackTrace()
            isRunning = false
        }
    }

    private fun startTrafficHandler() {
        thread = Thread {
            try {
                val inputStream = FileInputStream(vpnInterface?.fileDescriptor)
                val outputStream = FileOutputStream(vpnInterface?.fileDescriptor)
                val buffer = ByteBuffer.allocate(32767)

                while (isRunning) {
                    // 1. Ler pacotes da rede
                    val length = inputStream.read(buffer.array())
                    if (length > 0) {
                        // 2. AQUI ACONTECE A MÁGICA
                        // Se for um pacote DNS para site pornô -> Descarta ou retorna IP falso
                        // Se for site normal -> Escreve no outputStream

                        // Como exemplo, estamos apenas devolvendo o tráfego (sem bloqueio real neste snippet)
                        // Para bloquear, você precisaria de uma lib como 'dnsjava' ou parsear o header UDP/IP

                        outputStream.write(buffer.array(), 0, length)
                        buffer.clear()
                    }
                }
            } catch (e: Exception) {
                Log.e("VPN", "Erro no loop", e)
            }
        }
        thread?.start()
    }

    override fun onDestroy() {
        isRunning = false
        thread?.interrupt()
        vpnInterface?.close()
        super.onDestroy()
    }
}