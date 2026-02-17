package com.soponivtk.productmanagement

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.soponivtk.productmanagement.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: ProductDbHelper
    private var selectedImageUri: Uri? = null

    // ระบบเปิดเลือกรูปภาพจากเครื่อง
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            binding.imageViewProduct.setImageURI(uri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = ProductDbHelper(requireContext())

        // ตั้งค่าปุ่มเลือกรูป
        binding.buttonSelectImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // ตั้งค่าปุ่มบันทึก
        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val desc = binding.editTextDescription.text.toString()
            val price = binding.editTextPrice.text.toString().toDoubleOrNull() ?: 0.0
            val qty = binding.editTextQuantity.text.toString().toIntOrNull() ?: 0
            val imagePath = selectedImageUri?.toString() ?: ""

            if (name.isNotEmpty()) {
                val success = dbHelper.insertProduct(Product(
                    name = name,
                    description = desc,
                    price = price,
                    quantity = qty,
                    imagePath = imagePath
                ))

                if (success > -1) {
                    Toast.makeText(context, "บันทึกสินค้าสำเร็จ!", Toast.LENGTH_SHORT).show()
                    // สลับไปหน้ารายการสินค้าทันที
                    val nav = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
                    nav?.selectedItemId = R.id.nav_products
                }
            } else {
                Toast.makeText(context, "กรุณากรอกชื่อสินค้า", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}