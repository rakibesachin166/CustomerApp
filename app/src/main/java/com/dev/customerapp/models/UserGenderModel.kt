package com.dev.customerapp.models


enum class UserGenderModel(val genderCode: Int) {
    MALE(1),
    FEMALE(2),
    TRANSGENDER(3);

    companion object {

        fun fromGenderCode(genderCode: Int): UserGenderModel {
            return entries.find { it.genderCode == genderCode }
                ?: throw IllegalArgumentException("Invalid code: $genderCode")
        }
    }


}
