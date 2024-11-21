package com.lucaslima.ecosage.model

data class Conta(
    val id: String = "",
    val nome: String = "",
    val potencia: Double = 0.0,  //Watts
    val horasPorDia: Double = 0.0,
    val diasPorMes: Int = 0,
    val consumo: Double = 0.0, //kWh
    val custo: Double = 0.0 //Reais
)
