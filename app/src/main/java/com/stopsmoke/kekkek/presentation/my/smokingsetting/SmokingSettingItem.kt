package com.stopsmoke.kekkek.presentation.my.smokingsetting

data class SmokingSettingItem (
    val dailyCigarettesSmoked: Int,
    val packCigaretteCount: Int,
    val packPrice: Int
)


enum class SmokingSettingType {
    DailyCigarettesSmoked, PackCigaretteCount, PackPrice
}

fun SmokingSettingType.toFieldString(): String = when(this){
    SmokingSettingType.DailyCigarettesSmoked -> "user_config.daily_cigarettes_smoked"
    SmokingSettingType.PackCigaretteCount -> "user_config.pack_cigarette_count"
    SmokingSettingType.PackPrice -> "user_config.pack_price"
}