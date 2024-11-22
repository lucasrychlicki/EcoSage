package com.lucaslima.ecosage.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.lucaslima.ecosage.databinding.ItemContasBinding
import com.lucaslima.ecosage.model.Conta

class ContasAdapter(
    private val contas: List<Conta>,
    private val onEdit: (Conta) -> Unit
) : RecyclerView.Adapter<ContasAdapter.ContasViewHolder>(){

    class ContasViewHolder(private val binding: ItemContasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(conta: Conta, onEdit: (Conta) -> Unit) {
            binding.textContasNome.text = conta.nome
            binding.textContasEnergia.text = "Consumo: ${conta.consumo} kWh"
            binding.textContasPreco.text = "Custo: R$ ${conta.custo}"

            binding.btnEditContas.setOnClickListener {
                onEdit(conta)
            }

            binding.btnDeletarContas.setOnClickListener {
                val usuarioId = FirebaseAuth.getInstance().currentUser?.uid
                if (usuarioId == null) {
                    Toast.makeText(binding.root.context, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val database = FirebaseDatabase.getInstance().getReference("contas")
                database.child(usuarioId).child(conta.id).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(binding.root.context, "Conta excluída com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(binding.root.context, "Erro ao excluir a conta.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContasViewHolder {
        val binding = ItemContasBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContasViewHolder(binding)
    }

    override fun getItemCount() = contas.size

    override fun onBindViewHolder(holder: ContasViewHolder, position: Int) {
        holder.bind(contas[position], onEdit)
    }


}