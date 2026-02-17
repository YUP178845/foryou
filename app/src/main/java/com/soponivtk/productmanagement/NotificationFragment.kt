package com.soponivtk.productmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.soponivtk.productmanagement.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: ProductDbHelper
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = ProductDbHelper(requireContext())

        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(requireContext())

        // 🔥 แก้ไขจุดนี้: ส่งค่า true เข้าไปเป็นตัวที่สอง เพื่อสั่งซ่อนปุ่ม!
        productAdapter = ProductAdapter(
            products = emptyList(),
            isNotificationPage = true, // บอก Adapter ว่า "นี่คือหน้าแจ้งเตือน"
            onItemClick = { product ->
                // คลิกเพื่อดูรายละเอียดสินค้าได้ตามปกติ
            },
            onEditClick = {},   // ไม่ต้องใส่โค้ด เพราะปุ่มถูกซ่อนไปแล้ว
            onDeleteClick = {}  // ไม่ต้องใส่โค้ด เพราะปุ่มถูกซ่อนไปแล้ว
        )
        binding.recyclerViewNotifications.adapter = productAdapter

        loadLowStockProducts()
    }

    private fun loadLowStockProducts() {
        val allProducts = dbHelper.getAllProducts()
        // กรองสินค้าที่น้อยกว่า 10 ชิ้น (หรือตามที่นายตั้งเกณฑ์ไว้)
        val lowStockList = allProducts.filter { it.quantity < 10 }
        productAdapter.updateProducts(lowStockList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}