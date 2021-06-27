package com.example.mondaytest.ui.feed

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.mondaytest.models.Article
import com.example.mondaytest.repos.RepositoryImpl.Companion.CULTURE_ID
import com.example.mondaytest.repos.RepositoryImpl.Companion.SPORTS_ID
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CombinedItemsItemsFragment: BaseItemsFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserves()
    }

    private fun initObserves() {
        listViewModel.sportLiveData.observe(viewLifecycleOwner, sportLiveDataObserver)
        listViewModel.cultureLiveData.observe(viewLifecycleOwner, cultureLiveDataObserver)
        listViewModel.errorLiveData.observe(viewLifecycleOwner, errorLiveDataObserver)

    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }

    private fun removeObservers() {
        listViewModel.sportLiveData.removeObserver(sportLiveDataObserver)
        listViewModel.cultureLiveData.removeObserver(cultureLiveDataObserver)
        listViewModel.errorLiveData.removeObserver(errorLiveDataObserver)
    }

    override fun refresh() {
        super.refresh()
        listViewModel.fetchFeed(SPORTS_ID)
    }

    private val sportLiveDataObserver = Observer<List<Article>>{
        this.items.clear()
        this.items.addAll(it)
        listViewModel.fetchFeed(CULTURE_ID)
    }

    private val cultureLiveDataObserver = Observer<List<Article>>{
        this.items.addAll(it)
        binding.rvItems.adapter?.notifyDataSetChanged()
        binding.progressBar.visibility = View.GONE
    }

    private val errorLiveDataObserver = Observer<String>{
        if (it == SPORTS_ID)
            listViewModel.fetchFeed(CULTURE_ID)
        else
            binding.progressBar.visibility = View.GONE
    }
}