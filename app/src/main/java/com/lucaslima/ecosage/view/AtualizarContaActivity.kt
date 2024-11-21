package com.lucaslima.ecosage.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.lucaslima.ecosage.R
import com.lucaslima.ecosage.databinding.ActivityAtualizarContaBinding

class AtualizarContaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAtualizarContaBinding
    private lateinit var contaId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtualizarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras != null) {
            contaId = extras.getString("contaId").toString()
            binding.editNomeAparelhoAtualizado.setText(extras.getString("nome"))
            binding.editPotenciaAparelhoAtualizado.setText(extras.getDouble("potencia").toString())
            binding.editHorasAparelhoAtualizado.setText(extras.getDouble("horasPorDia").toString())
            binding.editDiasAparelhoAtualizado.setText(extras.getInt("diasPorMes").toString())
        }

        binding.btnAtualizarConta.setOnClickListener {
            val nome = binding.editNomeAparelhoAtualizado.text.toString()
            val potencia = binding.editPotenciaAparelhoAtualizado.text.toString().toDoubleOrNull()
            val horasPorDia = binding.editHorasAparelhoAtualizado.text.toString().toDoubleOrNull()
            val diasPorMes = binding.editDiasAparelhoAtualizado.text.toString().toIntOrNull()

            if (nome.isBlank() || potencia == null || horasPorDia == null || diasPorMes == null) {
                Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val consumo = (potencia * horasPorDia * diasPorMes) / 1000
            val custo = consumo * 0.80

            val database = FirebaseDatabase
                .getInstance()
                .getReference("contas")
                .child(contaId)

            val contaAtualizada = mapOf(
                "nome" to nome,
                "potencia" to potencia,
                "horasPorDia" to horasPorDia,
                "diasPorMes" to diasPorMes,
                "consumo" to consumo,
                "custo" to custo
            )

            database.updateChildren(contaAtualizada)
                .addOnSuccessListener {
                    Toast.makeText(this, "Conta atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao atualizar a conta.", Toast.LENGTH_SHORT).show()
                }

        }
    }
}