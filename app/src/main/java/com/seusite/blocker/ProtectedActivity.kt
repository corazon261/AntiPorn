package com.seusite.blocker

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.seusite.blocker.receivers.AdminReceiver
import com.seuapp.blocker.services.BlockerVpnService


class ProtectedActivity : AppCompatActivity() {

    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var compName: ComponentName

    // --- NOVO: Lançador para a permissão de Admin ---
    private val adminResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Admin Ativado! Iniciando VPN...", Toast.LENGTH_SHORT).show()
            // Após ativar o admin, iniciamos o processo da VPN
            iniciarVPN()
        } else {
            Toast.makeText(this, "Você PRECISA ativar o Admin para a proteção funcionar!", Toast.LENGTH_LONG).show()
        }
    }

    // --- NOVO: Lançador para a permissão de VPN ---
    private val vpnResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Permissão da VPN concedida, agora podemos iniciar o serviço
            iniciarServicoVpn()
        } else {
            Toast.makeText(this, "A permissão de VPN é necessária para o bloqueio.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Certifique-se de que o layout activity_protected.xml existe em res/layout
        // e que ele contém um botão com o id 'btnAtivarProtecao'.
        setContentView(R.layout.activity_protected)

        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, AdminReceiver::class.java)

        findViewById<Button>(R.id.btnAtivarProtecao).setOnClickListener {
            // Verificamos se o app já é admin. Se não for, pedimos a permissão.
            if (devicePolicyManager.isAdminActive(compName)) {
                iniciarVPN()
            } else {
                pedirPermissaoAdmin()
            }
        }
    }

    // --- MÉTODO ATUALIZADO ---
    private fun pedirPermissaoAdmin() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Esta permissão é necessária para impedir que o aplicativo seja desinstalado facilmente.")
        }
        // Usa o novo lançador para pedir a permissão
        adminResultLauncher.launch(intent)
    }

    // --- MÉTODO ATUALIZADO ---
    private fun iniciarVPN() {
        // Prepara a intenção para solicitar a permissão de VPN
        val intent = VpnService.prepare(this)
        if (intent != null) {
            // Se a permissão for necessária, lança a tela de solicitação do sistema
            vpnResultLauncher.launch(intent)
        } else {
            // Se a permissão já foi concedida, inicia o serviço diretamente
            iniciarServicoVpn()
        }
    }

    // --- NOVO MÉTODO AUXILIAR ---
    private fun iniciarServicoVpn() {
        val intent = Intent(this, BlockerVpnService::class.java)
        startService(intent)
        Toast.makeText(this, "Proteção TOTALMENTE Ativa", Toast.LENGTH_LONG).show()
        // Opcional: fechar esta tela após ativar tudo
        // finish()
    }

    // O método onActivityResult(..) foi substituído pelos launchers e pode ser removido.
    // Constantes como ADMIN_REQUEST_CODE e VPN_REQUEST_CODE também não são mais necessárias.
}
