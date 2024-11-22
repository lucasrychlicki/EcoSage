package com.lucaslima.ecosage.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lucaslima.ecosage.R
import com.lucaslima.ecosage.databinding.ActivityAtualizarContaBinding
import com.lucaslima.ecosage.model.Conta

class AtualizarContaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAtualizarContaBinding
    private lateinit var database: DatabaseReference
    private var usuarioId: String? = null
    private var contaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtualizarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("contas")
        usuarioId = FirebaseAuth.getInstance().currentUser?.uid
        contaId = intent.getStringExtra("contaId")

        if (usuarioId != null && contaId != null) {
            // Carregar dados existentes da conta
            carregarDataConta()

            // Atualizar conta
            binding.btnAtualizarConta.setOnClickListener {
                atualizarConta()
            }
        } else {
            Toast.makeText(this, "Erro ao carregar dados para atualização.", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun atualizarConta() {
        val usuarioId = FirebaseAuth.getInstance().currentUser?.uid
        if (usuarioId == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        val nome = binding.editNomeAparelhoAtualizado.text.toString()
        val potencia = binding.editPotenciaAparelhoAtualizado.text.toString().toDoubleOrNull() ?: 0.0
        val horasPorDia = binding.editHorasAparelhoAtualizado.text.toString().toDoubleOrNull() ?: 0.0
        val diasPorMes = binding.editDiasAparelhoAtualizado.text.toString().toIntOrNull() ?: 0

        if (nome.isBlank() || potencia <= 0 || horasPorDia <= 0 || diasPorMes <= 0) {
            Toast.makeText(this, "Por favor, preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
            return
        }

        val consumo = (potencia * horasPorDia * diasPorMes) / 1000
        val custo = consumo * 0.80

        val contaAtualizada = Conta(
            id = contaId ?: return,
            nome = nome,
            potencia = potencia,
            horasPorDia = horasPorDia,
            diasPorMes = diasPorMes,
            consumo = consumo,
            custo = custo
        )

        database.child(usuarioId).child(contaId!!).setValue(contaAtualizada)
            .addOnSuccessListener {
                Toast.makeText(this, "Conta atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao atualizar a conta: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun carregarDataConta() {
        database.child(usuarioId!!).child(contaId!!).get().addOnSuccessListener { snapshot ->
            val conta = snapshot.getValue(Conta::class.java)
            if (conta != null) {
                binding.editNomeAparelhoAtualizado.setText(conta.nome)
                binding.editPotenciaAparelhoAtualizado.setText(conta.potencia.toString())
                binding.editHorasAparelhoAtualizado.setText(conta.horasPorDia.toString())
                binding.editDiasAparelhoAtualizado.setText(conta.diasPorMes.toString())
            }
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Erro ao carregar dados: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }
}