package me.dannly.onboarding_domain.use_case

class FilterOutDecimalDigits {

    operator fun invoke(text: String) = text.filter { it.isDigit() || it == '.' }
}