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
import com.lucaslima.ecosage.databinding.ActivityAdicionarContaBinding
import com.lucaslima.ecosage.model.Conta
import java.util.UUID

class AdicionarContaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdicionarContaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdicionarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("contas")

        binding.btnSalvarConta.setOnClickListener {
            val usuarioId = auth.currentUser?.uid
            if (usuarioId == null) {
                Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val nome = binding.editNomeAparelho.text.toString()
            val potencia = binding.editPotenciaAparelho.text.toString().toDoubleOrNull()
            val horasPorDia = binding.editHorasAparelho.text.toString().toDoubleOrNull()
            val diasPorMes = binding.editDiasAparelho.text.toString().toIntOrNull()

            if (nome.isBlank() || potencia == null || horasPorDia == null || diasPorMes == null) {
                Toast.makeText(this, "Por favor, preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cálculo do consumo
            val consumo = (potencia * horasPorDia * diasPorMes) / 1000
            val custo = consumo * 0.80

            // Criação da conta
            val novaContaId = database.child(usuarioId).push().key
            val conta = Conta(
                id = novaContaId ?: "",
                nome = nome,
                potencia = potencia,
                horasPorDia = horasPorDia,
                diasPorMes = diasPorMes,
                consumo = consumo,
                custo = custo
            )

            // Salvando no Firebase
            database.child(usuarioId).child(conta.id).setValue(conta)
                .addOnSuccessListener {
                    Toast.makeText(this, "Conta adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao adicionar a conta.", Toast.LENGTH_SHORT).show()
                }
        }

    }
}