package com.dev.customerapp.models


enum class UserMarriageModel(val marriageCode: Int) {
    MARRIED(1),
    UNMARRIED(2),
    WIDOW(3),
    DIVORCED(4);

    companion object {

        fun fromGenderCode(marriageCode: Int): UserMarriageModel {
            return entries.find { it.marriageCode == marriageCode }
                ?: throw IllegalArgumentException("Invalid code: $marriageCode")
        }
    }
}
