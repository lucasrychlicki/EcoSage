package com.lucaslima.ecosage.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lucaslima.ecosage.R
import com.lucaslima.ecosage.databinding.ActivityAdicionarContaBinding

class AdicionarContaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdicionarContaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdicionarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}