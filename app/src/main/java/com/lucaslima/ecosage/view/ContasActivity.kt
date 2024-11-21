package com.lucaslima.ecosage.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lucaslima.ecosage.R
import com.lucaslima.ecosage.databinding.ActivityContasBinding

class ContasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContasBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}