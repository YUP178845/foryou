package com.soponivtk.productmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.soponivtk.productmanagement.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // เปลี่ยนจาก "ออกจากระบบ" เป็น "ปิดโปรแกรม" เพื่อให้เข้ากับแอปที่ไม่มีหน้า Login
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("ปิดแอปพลิเคชัน")
                .setMessage("คุณต้องการปิดแอปพลิเคชันใช่หรือไม่?")
                .setPositiveButton("ใช่") { _, _ ->
                    // คำสั่งปิด Activity ทั้งหมดและออกจากแอปจริง ๆ
                    activity?.finishAffinity()
                }
                .setNegativeButton("ยกเลิก", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}