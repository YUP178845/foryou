package com.soponivtk.productmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.soponivtk.productmanagement.databinding.ActivityMainBinding

// ลบ import fragments.* ออก เพราะไฟล์นายวางอยู่ข้างนอก Package เดียวกันอยู่แล้ว
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ตั้งค่า Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "หน้าหลัก"

        // จัดการการกดปุ่มเมนู 5 ปุ่ม
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_home -> {
                    selectedFragment = HomeFragment()
                    supportActionBar?.title = "หน้าหลัก"
                }
                R.id.nav_products -> {
                    selectedFragment = ProductFragment()
                    supportActionBar?.title = "รายการสินค้า"
                }
                R.id.nav_add -> {
                    // ถ้ายังแดง ให้เช็คว่าสร้างไฟล์ AddProductFragment หรือยัง
                    selectedFragment = AddProductFragment()
                    supportActionBar?.title = "ลงประกาศสินค้า"
                }
                R.id.nav_notifications -> {
                    selectedFragment = NotificationFragment()
                    supportActionBar?.title = "การแจ้งเตือน"
                }
                R.id.nav_profile -> {
                    selectedFragment = AccountFragment()
                    supportActionBar?.title = "โปรไฟล์ของฉัน"
                }
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment)
            }
            true
        }

        // หน้าเริ่มต้น
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}