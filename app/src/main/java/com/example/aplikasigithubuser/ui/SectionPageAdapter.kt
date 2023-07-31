package com.example.aplikasigithubuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aplikasigithubuser.ui.ListUser

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun createFragment(position: Int): Fragment {
        val fragment = ListUser()
        fragment.arguments = Bundle().apply {
            putInt(ListUser.ARG_SECTION_NUMBER, position + 1)
            putString(ListUser.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}