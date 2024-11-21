package com.lucaslima.ecosage.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val contasList = mutableListOf<Conta>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
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

        database = FirebaseDatabase.getInstance().getReference("contas")

        // Mudanças no Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contasList.clear()
                for (contaSnapshot in snapshot.children) {
                    val conta = contaSnapshot.getValue(Conta::class.java)
                    if (conta != null){
                        contasList.add(conta)
                    }
                }
                contasAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ContasActivity, "Erro ao carregar as contas.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}