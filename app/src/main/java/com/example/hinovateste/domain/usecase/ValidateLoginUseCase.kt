package com.example.hinovateste.domain.usecase

class ValidateLoginUseCase {

    fun isCpfValid(cpf: String): Boolean {
        val digits = cpf.filter { it.isDigit() }

        if (digits.length != 11) return false

        if ((0..9).any { i -> digits.all { it == ('0' + i) } }) return false

        val numbers = digits.map { it.toString().toInt() }

        val dv1 = calculateDigit(numbers.subList(0, 9), 10)
        val dv2 = calculateDigit(numbers.subList(0, 9) + dv1, 11)

        return numbers[9] == dv1 && numbers[10] == dv2
    }

    private fun calculateDigit(numbers: List<Int>, weightStart: Int): Int {
        var sum = 0
        var weight = weightStart
        for (n in numbers) {
            sum += n * weight--
        }
        val result = 11 - (sum % 11)
        return if (result > 9) 0 else result
    }

    fun isPasswordValid(password: String): Boolean = password.length >= 8
}