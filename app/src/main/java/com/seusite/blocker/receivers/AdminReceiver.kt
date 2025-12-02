package com.seusite.blocker.receivers

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class AdminReceiver : DeviceAdminReceiver() {

    // Quando o usuário tenta desativar o admin nas configurações
    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        notificarTutor("Tentativa de remover permissão de admin!")
        return "ATENÇÃO: Seu tutor será notificado imediatamente se você continuar."
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        notificarTutor("URGENTE: A proteção de administrador foi removida!")
        Toast.makeText(context, "Proteção Desativada", Toast.LENGTH_SHORT).show()
    }

    private fun notificarTutor(mensagem: String) {
        // Envia alerta para o Firestore
        val db = FirebaseFirestore.getInstance()
        val log = hashMapOf(
            "evento" to mensagem,
            "data" to Date(),
            "tipo" to "ALERTA_CRITICO"
        )
        // Idealmente, pegue o ID do usuário atual de SharedPreferences
        db.collection("logs_seguranca").add(log)
    }
}