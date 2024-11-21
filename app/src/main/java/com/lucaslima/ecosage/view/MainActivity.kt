package com.lucaslima.ecosage.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lucaslima.ecosage.R
import com.lucaslima.ecosage.databinding.ActivityCadastroBinding
import com.lucaslima.ecosage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdicionarConta.setOnClickListener {
            val intent = Intent(this, AdicionarContaActivity::class.java)
            startActivity(intent)
        }

        binding.btnVisualizarConta.setOnClickListener {
            val intent = Intent(this, ContasActivity::class.java)
            startActivity(intent)
        }
    }

}