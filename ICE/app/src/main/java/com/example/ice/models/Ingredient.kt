package com.example.ice.models

import java.io.Serializable

// Detail Component like gluten, lactose, etc.
// Type : Component, Ingredient, Food
data class Component(
    val name: String,
    val image: Int,
    val type: String
)

// Filter
data class CustomFilter(
    val name: String,
    val image: Int,
    val components: ArrayList<Component>
): Serializable {

}