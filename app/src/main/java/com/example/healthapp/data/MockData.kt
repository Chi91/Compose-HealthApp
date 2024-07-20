package com.example.healthapp.data

import androidx.annotation.DrawableRes
import com.example.healthapp.R


data class InfoCardTopic(val title: Int, @DrawableRes val image: Int, val description: Int)

val infoCardTopicLists = listOf(
    InfoCardTopic(
        title = R.string.InfoCardTopicSport,
        image = R.drawable.sport,
        description = R.string.text_placeholder
    ),
    InfoCardTopic(
        title = R.string.InfoCardTopicFood,
        image = R.drawable.ernaehrung,
        description = R.string.text_placeholder
    ),
    InfoCardTopic(
        title = R.string.InfoCardTopicTherapy,
        image = R.drawable.therapie,
        description = R.string.text_placeholder
    ),
    InfoCardTopic(
        title = R.string.InfoCardTopicSymptome,
        image = R.drawable.symptome,
        description = R.string.text_placeholder
    ),
    InfoCardTopic(
        title = R.string.InfoCardTopicPlants,
        image = R.drawable.heilpflanzen,
        description = R.string.text_placeholder
    ),
    InfoCardTopic(
        title = R.string.InfoCardTopicInstruction,
        image = R.drawable.inhalanda1,
        description = R.string.text_placeholder
    ),
)

val chipTopicList = listOf("Asthma", "Blutdruck", "Rheuma", "Schlafstörung")

