package com.example.productviewerapp.model

data class Product(
    val id: Int,
    val title: String,
    val price: Float,
    val category: String,
    val image: String,
    val rating: Rating
)