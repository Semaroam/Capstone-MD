package com.dicoding.semaroam.adapter

// MyPagerAdapter.kt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.semaroam.fragment.UlasanFragment
import com.dicoding.semaroam.fragment.VersiFragment

class MyPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UlasanFragment() // UlasanFragment sekarang muncul pertama
            else -> VersiFragment() // VersiFragment sekarang muncul kedua
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}