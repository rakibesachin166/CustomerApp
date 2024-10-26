package com.dev.customerapp.models;

enum class UserTypes(val code: Int) {
    ADMIN(1),
    STATE_OFFICER(2),
    DIVISIONAL_OFFICER(3),
    DISTRICT_OFFICER(4),
    BLOCK_OFFICER(5);

    companion object {

        fun fromCode(code: Int): UserTypes {
            return entries.find { it.code == code }
                ?: throw IllegalArgumentException("Invalid code: $code")
        }
    }
}
