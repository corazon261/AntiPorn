package com.seusite.blocker.receivers

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent

class MyDeviceAdminReceiver : DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        // O app agora é administrador. O usuário não pode desinstalar facilmente.
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        // Isso aparece quando o usuário tenta desativar o admin nas configurações
        return "Atenção: Desativar isso notificará seu Tutor imediatamente."
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        // CUIDADO: Aqui o usuário conseguiu desativar.
        // Enviar alerta de emergência para o Tutor via API/Firebase aqui.
    }
}