package com.example.healthapp.data.drug

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drugs")
data class Drug(
    @PrimaryKey val pzn: String,
    val activeIngredient: String,
    val potency: String,
    val name: String,
    val indication: String,
    val description: String,
    val dosageInstruction:  String,
    val isDivisible: String,
    val amount: Int,
)
