package com.soponivtk.productmanagement

data class Product(
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val imagePath: String? = null // <--- เช็กชื่อตัวแปรนี้ให้ตรงกัน
)