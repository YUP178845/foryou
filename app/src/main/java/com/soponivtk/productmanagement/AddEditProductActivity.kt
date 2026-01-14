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
// ต้อง Import binding ของหน้านี้ให้ถูก
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

        // ตรวจสอบว่าเป็นการแก้ไขหรือเพิ่มใหม่
        productId = intent.getIntExtra("product_id", -1)
        if (productId != -1) {
            supportActionBar?.title = "แก้ไขสินค้า"
            loadProductData()
        } else {
            supportActionBar?.title = "เพิ่มสินค้า"
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

        val product = Product(
            id = if (productId != -1) productId else 0,
            name = name,
            description = description,
            price = price,
            quantity = quantity,
            imagePath = selectedImageUri?.toString()
        )

        if (productId != -1) {
            dbHelper.updateProduct(product)
            Toast.makeText(this, "แก้ไขข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show()
        } else {
            dbHelper.insertProduct(product)
            Toast.makeText(this, "เพิ่มสินค้าสำเร็จ", Toast.LENGTH_SHORT).show()
        }

        setResult(Activity.RESULT_OK)
        finish()
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