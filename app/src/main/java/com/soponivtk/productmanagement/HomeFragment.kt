package com.soponivtk.productmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.soponivtk.productmanagement.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: ProductDbHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = ProductDbHelper(requireContext())
        updateDashboard()
    }

    private fun updateDashboard() {
        val products = dbHelper.getAllProducts()

        // คำนวณจำนวนสินค้า
        val totalCount = products.size
        binding.tvTotalProducts.text = "$totalCount รายการ"

        // คำนวณมูลค่ารวม (ราคา x จำนวน)
        var totalValue = 0.0
        for (product in products) {
            totalValue += (product.price * product.quantity)
        }
        binding.tvTotalValue.text = String.format("%.2f บาท", totalValue)
    }

    override fun onResume() {
        super.onResume()
        updateDashboard() // อัปเดตเลขทุกครั้งที่เปิดหน้านี้
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}