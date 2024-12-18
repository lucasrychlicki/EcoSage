package com.lucaslima.ecosage.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lucaslima.ecosage.R
import com.lucaslima.ecosage.databinding.ActivityContasBinding
import com.lucaslima.ecosage.model.Conta

class ContasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContasBinding
    private lateinit var database: DatabaseReference
    private lateinit var contasAdapter: ContasAdapter
    private lateinit var auth: FirebaseAuth
    private val contasList = mutableListOf<Conta>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("contas")

        binding.rvContas.layoutManager = LinearLayoutManager(this)
        contasAdapter = ContasAdapter(contasList) {conta ->
            val intent = Intent(this, AtualizarContaActivity::class.java).apply {
                putExtra("contaId", conta.id)
                putExtra("nome", conta.nome)
                putExtra("potencia", conta.potencia)
                putExtra("horasPorDia", conta.horasPorDia)
                putExtra("diasPorMes", conta.diasPorMes)
            }
            startActivity(intent)
        }
        binding.rvContas.adapter = contasAdapter

        carregarContas()

    }

    private fun carregarContas() {
        val usuarioId = auth.currentUser?.uid
        if (usuarioId == null) {
            Toast.makeText(this, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        database.child(usuarioId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contasList.clear()
                var consumoTotal = 0.0
                var custoTotal = 0.0

                for (data in snapshot.children) {
                    val conta = data.getValue(Conta::class.java)
                    conta?.let {
                        contasList.add(it)
                        consumoTotal += it.consumo
                        custoTotal += it.custo
                    }
                }

                contasAdapter.notifyDataSetChanged()

                binding.textCustoTotal.text = "Custo Total: R$ %.2f".format(custoTotal)
                binding.textConsumoTotal.text = "Consumo Total: %.2f kWh".format(consumoTotal)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ContasActivity, "Erro ao carregar as contas.", Toast.LENGTH_SHORT).show()
            }
        })

    }
}