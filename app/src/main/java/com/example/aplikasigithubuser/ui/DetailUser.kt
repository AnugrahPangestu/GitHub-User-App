package com.example.aplikasigithubuser.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.helper.DetailViewModel
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.SectionsPagerAdapter
import com.example.aplikasigithubuser.database.FavoriteUser
import com.example.aplikasigithubuser.databinding.FragmentDetailUserBinding
import com.example.aplikasigithubuser.helper.UserViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : Fragment() {

    private var _binding:FragmentDetailUserBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel by viewModels<DetailViewModel> {
        UserViewModelFactory.getInstance(requireActivity().application)
    }

    private val args: DetailUserArgs by navArgs()
    private var favoriteUser: FavoriteUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!detailViewModel.user.isInitialized) detailViewModel.getDetailUser(args.username)
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
                menu.findItem(R.id.search).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.favorite -> {
                        val toFavoriteFragment =
                            DetailUserDirections.actionDetailUserToFavoriteFragment()
                        findNavController().navigate(toFavoriteFragment)
                        true
                    }
                    R.id.setting -> {
                        val toSettingActivity =
                            DetailUserDirections.actionDetailUserToSettingActivity()
                        findNavController().navigate(toSettingActivity)
                        true
                    }
                    else -> true
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity() as AppCompatActivity)
        sectionsPagerAdapter.username = args.username
        binding.apply {
            contentDetail.viewPager.adapter = sectionsPagerAdapter
            fabDetail.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        detailViewModel.user.observe(viewLifecycleOwner) { user ->
            val follow = listOf(user?.followers, user?.following)

            if (user?.login != null) {
                favoriteUser = FavoriteUser(user.login, user.avatarUrl)
                detailViewModel.getFavoriteUserByUsername(user.login)
                    .observe(viewLifecycleOwner) { favorite ->
                        binding.fabDetail.apply {
                            if (favorite == null) {
                                setImageResource(R.drawable.ic_baseline_favorite_border_24)
                                setOnClickListener {
                                    detailViewModel.insert(favoriteUser as FavoriteUser)
                                }
                            } else {
                                setImageResource(R.drawable.ic_baseline_favorite_24)
                                setOnClickListener {
                                    detailViewModel.delete(favoriteUser as FavoriteUser)
                                }
                            }
                        }
                    }
            }

            binding.contentDetail.apply {
                Glide.with(view).load(user?.avatarUrl)
                    .circleCrop()
                    .into(ivDetailUser)
                if (user?.name == null) {
                    tvUsername.text =
                        user?.login
                } else {
                    tvUsername.text = user.name
                }

                tvName.text = user?.login

                TabLayoutMediator(tabs, viewPager) { tab, position ->
                    tab.text = "${follow[position]} ${resources.getString(TAB_TITLES[position])}"
                }.attach()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.contentDetail.apply {
            if (isLoading) {
                progressBarDetail.visibility = View.VISIBLE
            } else {
                progressBarDetail.visibility = View.GONE
            }
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}