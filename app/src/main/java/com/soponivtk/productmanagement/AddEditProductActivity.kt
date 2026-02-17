package com.soponivtk.productmanagement

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.soponivtk.productmanagement.databinding.ActivityAddEditProductBinding

class AddEditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditProductBinding
    private lateinit var dbHelper: ProductDbHelper
    private var productId: Int = -1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = ProductDbHelper(this)

        // ตรวจสอบค่าที่ส่งมาจาก Fragment (แก้ให้ตรงกับ PRODUCT_ID)
        productId = intent.getIntExtra("PRODUCT_ID", -1)

        if (productId != -1) {
            // ถ้ามี ID แสดงว่าเป็นโหมดแก้ไข
            binding.buttonSave.text = "อัปเดตข้อมูลสินค้า"
            loadProductData()
        } else {
            // ถ้าไม่มี ID แสดงว่าเป็นโหมดเพิ่มใหม่
            binding.buttonSave.text = "บันทึกสินค้าใหม่"
        }

        binding.buttonSelectImage.setOnClickListener {
            openGallery()
        }

        binding.buttonSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private fun loadProductData() {
        val product = dbHelper.getProductById(productId)
        product?.let {
            binding.editTextName.setText(it.name)
            binding.editTextDescription.setText(it.description)
            binding.editTextPrice.setText(it.price.toString())
            binding.editTextQuantity.setText(it.quantity.toString())
            if (!it.imagePath.isNullOrEmpty()) {
                selectedImageUri = Uri.parse(it.imagePath)
                Glide.with(this).load(selectedImageUri).into(binding.imageViewProduct)
            }
        }
    }

    private fun saveProduct() {
        val name = binding.editTextName.text.toString()
        val description = binding.editTextDescription.text.toString()
        val price = binding.editTextPrice.text.toString().toDoubleOrNull() ?: 0.0
        val quantity = binding.editTextQuantity.text.toString().toIntOrNull() ?: 0

        if (name.isEmpty()) {
            Toast.makeText(this, "กรุณากรอกชื่อสินค้า", Toast.LENGTH_SHORT).show()
            return
        }

        // สร้าง Object Product โดยใช้ ID เดิมถ้าเป็นการแก้ไข
        val product = Product(
            id = if (productId != -1) productId else 0,
            name = name,
            description = description,
            price = price,
            quantity = quantity,
            imagePath = selectedImageUri?.toString()
        )

        if (productId != -1) {
            // คำสั่งอัปเดตข้อมูลเดิม
            val rows = dbHelper.updateProduct(product)
            if (rows > 0) {
                Toast.makeText(this, "อัปเดตข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show()
            }
        } else {
            // คำสั่งเพิ่มข้อมูลใหม่
            dbHelper.insertProduct(product)
            Toast.makeText(this, "เพิ่มสินค้าสำเร็จ", Toast.LENGTH_SHORT).show()
        }

        finish() // ปิดหน้านี้เพื่อกลับไปหน้าหลัก
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            Glide.with(this).load(selectedImageUri).into(binding.imageViewProduct)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}