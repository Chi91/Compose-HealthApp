package com.example.healthapp.data.ownDrug

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ownDrugs")
data class OwnDrug(
    @PrimaryKey
    val pzn: String,
    val activeIngredient: String,
    val potency: String,
    val name: String,
    val indication: String,
    val description: String,
    val divisible: String,
    val dosageInstruction: String,
    val notes: String,
    val morning: String,
    val noon: String,
    val evening: String,
    val night: String,
    var checkedMorning: Boolean,
    var checkedNoon: Boolean,
    var checkedEvening: Boolean,
    var checkedNight: Boolean,
    var amount: Int,
)




