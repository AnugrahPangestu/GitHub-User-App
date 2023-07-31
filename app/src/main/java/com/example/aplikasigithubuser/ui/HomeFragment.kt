package com.example.aplikasigithubuser.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.service.response.ItemsItem
import com.example.aplikasigithubuser.helper.MainViewModel
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.databinding.FragmentHomeBinding


class HomeFragment : Fragment(){

    private val MainViewModel: MainViewModel by viewModels()
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!MainViewModel.listUser.isInitialized) MainViewModel.getUser()

        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)

                val searchManager =
                    activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = menu.findItem(R.id.search).actionView as SearchView

                searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
                searchView.queryHint = resources.getString(R.string.search_hint)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String): Boolean {
                        MainViewModel.getSearchUser(query)
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.favorite -> {
                        val toFavoriteFragment =
                            HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
                        findNavController().navigate(toFavoriteFragment)
                        true
                    }
                    R.id.setting -> {
                        val toSettingActivity =
                            HomeFragmentDirections.actionHomeFragmentToSettingActivity()
                        findNavController().navigate(toSettingActivity)
                        true
                    }
                    else -> true
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val layoutManager = LinearLayoutManager(activity)
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.apply {
            rvList.layoutManager = layoutManager
            rvList.addItemDecoration(itemDecoration)
        }

        MainViewModel.listUser.observe(viewLifecycleOwner) { listUsers ->
            if (listUsers != null) {
                setListUsersData(listUsers)
            }
        }

        MainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setListUsersData(listItemUsers: List<ItemsItem>) {
        val adapter = UserAdapter(listItemUsers) { user, view ->
            val toDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailUser()
            if (user.login != null) toDetailFragment.username = user.login
            view.findNavController().navigate(toDetailFragment)
        }
        binding.rvList.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


