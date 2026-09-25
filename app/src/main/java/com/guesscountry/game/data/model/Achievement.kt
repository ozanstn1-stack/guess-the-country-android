package com.guesscountry.game.data.model

enum class AchievementId(
    val key: String,
    val title: String,
    val description: String,
    val emoji: String,
    val target: Int,
) {
    FIRST_TEN("first_ten", "İlk 10 soru", "İlk 10 sorunu tamamla", "🌍", 10),
    TEN_CORRECT("ten_correct", "10 doğru cevap", "10 doğru cevap ver", "🎯", 10),
    COMBO_FIVE("combo_five", "5 combo", "5 doğru cevap üst üste yap", "🔥", 5),
    SCORE_1000("score_1000", "1000 puan", "Toplam 1000 puana ulaş", "🏆", 1000),
    COUNTRIES_25("countries_25", "25 ülke", "25 farklı ülke keşfet", "🌎", 25),
    COUNTRIES_50("countries_50", "50 ülke", "50 farklı ülke keşfet", "🌍", 50),
    COUNTRIES_100("countries_100", "100 ülke", "100 farklı ülke keşfet", "🌐", 100),
    GAMES_TEN("games_ten", "10 oyun", "10 oyun tamamla", "👑", 10),
}
