package com.example.mondaytest.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mondaytest.databinding.FragmentItemsBinding
import com.example.mondaytest.models.Article
import com.example.mondaytest.repos.Types
import com.example.mondaytest.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsFragment(private val position: Int) : Fragment() {
    val listViewModel: ItemsViewModel by viewModels()
    val profileViewModel: ProfileViewModel by activityViewModels()
    lateinit var binding: FragmentItemsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(listViewModel)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserves()
    }

    private fun initRecyclerView() {
        binding.rvItems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                    DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                    )
            )
            adapter = ItemsListAdapter(
                    emptyList(),
                    adapterInteractionCallbackImpl
            )
        }
    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }

    private fun initObserves() {
        listViewModel.showProgressBar.observe(viewLifecycleOwner, showProgressBarLiveDataObserver)
        listViewModel.refresh.observe(viewLifecycleOwner, refreshLiveDataLiveDataObserver)
        listViewModel.feedListLiveData.observe(viewLifecycleOwner, feedLiveDataObserver)
        listViewModel.combinedListLiveData.observe(viewLifecycleOwner, combinedLiveDataObserver)
    }

    private fun removeObservers() {
        listViewModel.showProgressBar.removeObserver(showProgressBarLiveDataObserver)
        listViewModel.refresh.removeObserver(refreshLiveDataLiveDataObserver)
        listViewModel.feedListLiveData.removeObserver(feedLiveDataObserver)
        listViewModel.combinedListLiveData.removeObserver(combinedLiveDataObserver)
    }

    private val adapterInteractionCallbackImpl = object:
            ItemsListAdapter.InteractionListener {
        override fun onItemClick(v: View, feed: Article?) {
            feed?.let { it ->
                it.title?.let {title -> profileViewModel.setLastSeenFeedTitle(title) }
                it.link?.let { link -> activity?.let { activity -> listViewModel.onItemClick(activity, link) } }
            }
        }
    }

    private fun refresh()
    {
        listViewModel.fetchFeed( if (position == 0) Types.CARS_ID else Types.SPORTS_ID)
    }

    private val showProgressBarLiveDataObserver = Observer<Boolean>{
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }

    private val refreshLiveDataLiveDataObserver = Observer<Boolean>{
        refresh()
    }

    private val combinedLiveDataObserver = Observer<List<Article>>{
        (binding.rvItems.adapter as ItemsListAdapter).setData(it)
    }

    private val feedLiveDataObserver = Observer<List<Article>>{
        (binding.rvItems.adapter as ItemsListAdapter).setData(it)
    }
    companion object {
        const val TIMEOUT: Long = 5000
    }
}