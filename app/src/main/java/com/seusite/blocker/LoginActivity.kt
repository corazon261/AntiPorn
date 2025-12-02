package com.seusite.blocker
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seusite.blocker.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSouTutor.setOnClickListener {
            val intent = Intent(this, TutorActivity::class.java)
            startActivity(intent)
        }

        binding.btnSouAfetado.setOnClickListener {
            val intent = Intent(this, ProtectedActivity::class.java)
            startActivity(intent)
        }
    }
}