package com.soponivtk.productmanagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soponivtk.productmanagement.databinding.ItemProductBinding
import com.soponivtk.productmanagement.databinding.ItemNotificationBinding // ต้อง import ตัวใหม่นี้ด้วย

class ProductAdapter(
    private var products: List<Product>,
    private val isNotificationPage: Boolean = false, // เพิ่มตัวแปรเช็คหน้า (ค่าเริ่มต้นเป็น false)
    private val onItemClick: (Product) -> Unit,
    private val onEditClick: (Product) -> Unit = {}, // ใส่ค่าเริ่มต้นไว้เผื่อหน้าแจ้งเตือนไม่ต้องใช้
    private val onDeleteClick: (Product) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() { // เปลี่ยนเป็น ViewHolder กลาง

    // ViewHolder สำหรับหน้าสินค้าปกติ (มีปุ่ม)
    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            commonBind(product, binding.imageViewProduct, binding.textViewName, binding.textViewPrice, binding.textViewQuantity)

            binding.root.setOnClickListener { onItemClick(product) }
            binding.buttonEdit.setOnClickListener { onEditClick(product) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(product) }
        }
    }

    // ViewHolder สำหรับหน้าแจ้งเตือน (ไม่มีปุ่ม - ใช้ Layout item_notification)
    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            commonBind(product, binding.imageViewProduct, binding.textViewName, binding.textViewPrice, binding.textViewQuantity)

            binding.root.setOnClickListener { onItemClick(product) }
            // หน้านี้ไม่มีปุ่มแก้ไข/ลบแล้ว ตามที่อาจารย์บอก
        }
    }

    // ฟังก์ชันช่วยลดโค้ดซ้ำซ้อนในการเซตค่าพื้นฐาน
    private fun commonBind(product: Product, img: android.widget.ImageView, name: android.widget.TextView, price: android.widget.TextView, qty: android.widget.TextView) {
        if (!product.imagePath.isNullOrEmpty()) {
            Glide.with(img.context).load(product.imagePath).placeholder(R.drawable.ic_image_placeholder).into(img)
        } else {
            img.setImageResource(R.drawable.ic_image_placeholder)
        }
        name.text = product.name
        price.text = "ราคา: ${product.price} บาท"
        qty.text = "จำนวน: ${product.quantity} ชิ้น"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (isNotificationPage) {
            // เรียกใช้ไฟล์ใหม่ที่นายสร้าง (item_notification.xml)
            val binding = ItemNotificationBinding.inflate(inflater, parent, false)
            NotificationViewHolder(binding)
        } else {
            // เรียกใช้ไฟล์เดิม (item_product.xml)
            val binding = ItemProductBinding.inflate(inflater, parent, false)
            ProductViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = products[position]
        if (holder is ProductViewHolder) holder.bind(product)
        else if (holder is NotificationViewHolder) holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}