package com.example.ice.models

// Detail Component like gluten, lactose, etc.
data class Component(
    val name: String,
    val image: String
)

// Food Ingredient depth.
data class Ingredient(
    val name: String,
    val image: String
) {
    companion object {
        var components: ArrayList<Component> = arrayListOf()
    }
}

// Foodial Depth, like Milk. Nuts. etc.
data class Foodial(
    val name: String,
    val image: String
) {
    companion object {
        var components: ArrayList<Component> = arrayListOf()
    }
}

// Filter
data class CustomFilter(
    val name: String,
    val image: String
) {
    companion object {
        var components: ArrayList<Component> = arrayListOf()
        var ingredients: ArrayList<Ingredient> = arrayListOf()
        var foodials: ArrayList<Foodial> = arrayListOf()
    }
}