package com.soponivtk.productmanagement

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
// Import Binding ให้ตรงกับชื่อไฟล์ XML ของคุณ
import com.soponivtk.productmanagement.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var dbHelper: ProductDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ตั้งค่า View Binding
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = ProductDbHelper(this)

        // รับค่า ID สินค้าที่ส่งมาจากหน้า MainActivity
        val productId = intent.getIntExtra("product_id", -1)

        if (productId != -1) {
            loadProductDetail(productId)
        }
    }

    private fun loadProductDetail(productId: Int) {
        val product = dbHelper.getProductById(productId)
        product?.let {
            // แสดงชื่อสินค้า
            binding.textViewName.text = it.name

            // แสดงรายละเอียดสินค้า
            binding.textViewDescription.text = it.description

            // แสดงราคาสินค้า
            binding.textViewPrice.text = "ราคา: ${it.price} บาท"

            // แสดงจำนวนสินค้า
            binding.textViewQuantity.text = "จำนวนคงเหลือ: ${it.quantity} ชิ้น"

            // แสดงรูปภาพสินค้าด้วย Glide
            if (!it.imagePath.isNullOrEmpty()) {
                Glide.with(this)
                    .load(it.imagePath)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(binding.imageViewProduct)
            } else {
                binding.imageViewProduct.setImageResource(R.drawable.ic_image_placeholder)
            }
        }
    }
}