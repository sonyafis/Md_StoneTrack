package com.fisun.md_stonetrack.domain.usecase

data class ValidationResult(
    val isValid: Boolean,
    val errors: Map<String, String?>
)

class ValidateRegistrationFieldsUseCase {
    operator fun invoke(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ): ValidationResult {
        val errors = mutableMapOf<String, String?>()
        var isValid = true

        if (username.isBlank()) {
            errors["username"] = "Введите логин"
            isValid = false
        }

        if (email.isBlank()) {
            errors["email"] = "Введите email"
            isValid = false
        } else if (!email.contains("@")) {
            errors["email"] = "Введите корректный email"
            isValid = false
        }

        if (password.isBlank()) {
            errors["password"] = "Введите пароль"
            isValid = false
        } else {
            if (password.length < 8) {
                errors["password"] = "Пароль должен содержать минимум 8 символов"
                isValid = false
            } else if (!password.any { it.isDigit() }) {
                errors["password"] = "Пароль должен содержать хотя бы одну цифру"
                isValid = false
            } else if (!password.any { it.isUpperCase() }) {
                errors["password"] = "Пароль должен содержать хотя бы одну заглавную букву"
                isValid = false
            } else if (!password.any { it.isLowerCase() }) {
                errors["password"] = "Пароль должен содержать хотя бы одну строчную букву"
                isValid = false
            } else if (password.any { it.isWhitespace() }) {
                errors["password"] = "Пароль не должен содержать пробелов"
                isValid = false
            }
        }

        if (confirmPassword.isBlank()) {
            errors["confirmPassword"] = "Подтвердите пароль"
            isValid = false
        } else if (confirmPassword != password) {
            errors["confirmPassword"] = "Пароли не совпадают"
            isValid = false
        }

        if (firstName.isBlank()) {
            errors["firstName"] = "Введите имя"
            isValid = false
        }

        if (lastName.isBlank()) {
            errors["lastName"] = "Введите фамилию"
            isValid = false
        }

        if (phoneNumber.isBlank()) {
            errors["phoneNumber"] = "Введите номер телефона"
            isValid = false
        } else {
            val digitsOnly = phoneNumber.replace(Regex("[^0-9]"), "")

            if (digitsOnly.length !in 10..12) {
                errors["phoneNumber"] = "Номер должен содержать 10-12 цифр"
                isValid = false
            } else if (!digitsOnly.startsWith("7") && !digitsOnly.startsWith("8") && digitsOnly.length == 11) {
                errors["phoneNumber"] = "Введите корректный российский номер"
                isValid = false
            } else if (digitsOnly.length < 11) {
                errors["phoneNumber"] = "Слишком короткий номер"
                isValid = false
            }
        }

        return ValidationResult(isValid, errors)
    }
}
