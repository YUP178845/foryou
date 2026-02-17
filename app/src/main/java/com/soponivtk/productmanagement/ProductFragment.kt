package com.soponivtk.productmanagement

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.soponivtk.productmanagement.databinding.FragmentProductBinding

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: ProductDbHelper
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = ProductDbHelper(requireContext())

        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())

        productAdapter = ProductAdapter(
            emptyList(),
            onItemClick = { product ->
                // ถ้ามีหน้า Detail ให้ส่งไปที่นี่ (Option)
            },
            onEditClick = { product ->
                // --- แก้ไขจุดนี้เพื่อให้ปุ่มแก้ไขใช้งานได้ ---
                val intent = Intent(requireContext(), AddEditProductActivity::class.java).apply {
                    putExtra("PRODUCT_ID", product.id)
                    putExtra("IS_EDIT_MODE", true)
                }
                startActivity(intent)
                // ---------------------------------------
            },
            onDeleteClick = { product ->
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("ลบสินค้า")
                    .setMessage("คุณแน่ใจว่าต้องการลบ ${product.name}?")
                    .setPositiveButton("ลบ") { _, _ ->
                        dbHelper.deleteProduct(product.id)
                        loadProducts()
                    }
                    .setNegativeButton("ยกเลิก", null)
                    .show()
            }
        )
        binding.recyclerViewProducts.adapter = productAdapter

        loadProducts()
    }

    // เพิ่มฟังก์ชันดึงข้อมูลใหม่ทุกครั้งที่กลับมาที่หน้านี้ (เผื่อแก้ไขเสร็จแล้วกลับมาโชว์)
    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        val products = dbHelper.getAllProducts()
        productAdapter.updateProducts(products)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}