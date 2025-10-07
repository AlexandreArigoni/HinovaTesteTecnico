package com.example.hinovateste.data.local

import com.example.hinovateste.data.model.User

object MockUserDataSource {
    val user = User(
        id = "3555",
        nome = "Alexandre Teste",
        codigoMobile = "555",
        cpf = "034.048.610-43",
        email = "alexandre.teste@hinovamobile.com.br",
        situacao = "ATIVO",
        telefone = "31-9999-5551"
    )
}