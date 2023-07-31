package com.example.aplikasigithubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.service.response.ItemsItem
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.databinding.FragmentFavoriteBinding
import com.example.aplikasigithubuser.helper.FavoriteViewModel
import com.example.aplikasigithubuser.helper.UserViewModelFactory

class FavoriteFragment : Fragment() {
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        UserViewModelFactory.getInstance(requireActivity().application)
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)

        binding.apply {
            rvFavorite.layoutManager = layoutManager
            rvFavorite.addItemDecoration(itemDecoration)
        }

        favoriteViewModel.getAllFavoriteUser().observe(viewLifecycleOwner) { listFavoriteUser ->
            val items = arrayListOf<ItemsItem>()
            listFavoriteUser.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            setListUsersData(items)
        }



    }

    private fun setListUsersData(listItemUsers: List<ItemsItem>) {
        val adapter = UserAdapter(listItemUsers) { user, view ->
            val toDetailFragment =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailUser()
            if (user.login != null) toDetailFragment.username = user.login
            view.findNavController().navigate(toDetailFragment)
        }
        binding.rvFavorite.adapter = adapter
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}