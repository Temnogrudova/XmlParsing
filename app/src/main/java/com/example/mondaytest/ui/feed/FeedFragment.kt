package com.example.mondaytest.ui.feed


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mondaytest.R
import com.example.mondaytest.databinding.FragmentFeedBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment: Fragment() {
    private lateinit var binding: FragmentFeedBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initTabsLayout()
    }

    private fun initViewPager() = with(binding) {
        pager.adapter = FeedTabsAdapter(childFragmentManager, lifecycle)
        pager.isUserInputEnabled = false
    }

    private fun initTabsLayout() = with(binding) {
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getText(R.string.tab_cars)
                    tab.setContentDescription(R.string.desc_tab_cars)
                }
                1 -> {
                    tab.text = getText(R.string.tab_sport_culture)
                    tab.setContentDescription(R.string.desc_tab_sport_culture)
                }
            }
        }.attach()
    }

}