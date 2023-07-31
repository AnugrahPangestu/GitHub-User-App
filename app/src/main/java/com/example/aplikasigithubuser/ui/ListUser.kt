package com.example.aplikasigithubuser.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.service.response.ItemsItem
import com.example.aplikasigithubuser.databinding.FragmentListUserBinding
import com.example.aplikasigithubuser.helper.MainViewModel

class ListUser : Fragment() {

    private val MainViewModel: MainViewModel by viewModels()

    private var _binding: FragmentListUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListUserBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_SECTION_NUMBER)
        val username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(activity)
        binding.rvListUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvListUsers.addItemDecoration(itemDecoration)

        if (username != null && !MainViewModel.listUser.isInitialized) {
            if (position == 1) {
                MainViewModel.getFollowers(username)
            } else {
                MainViewModel.getFollowing(username)
            }
        }

        MainViewModel.listUser.observe(viewLifecycleOwner) { listUser ->
            if (listUser != null) {
                setListUsersData(listUser)
            }
        }

        MainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }


    private fun setListUsersData(listItemUsers: List<ItemsItem>) {

        val adapter = UserAdapter(listItemUsers) { user, view ->
            val toSelfFragment = DetailUserDirections.actionDetailUserSelf()
            if (user.login != null) toSelfFragment.username = user.login
            view.findNavController().navigate(toSelfFragment)

        }
        binding.rvListUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarList.visibility = View.VISIBLE
        } else {
            binding.progressBarList.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"

    }
}