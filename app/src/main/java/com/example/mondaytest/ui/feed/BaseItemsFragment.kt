package com.example.mondaytest.ui.feed

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.mondaytest.ui.profile.ProfileViewModel

abstract class BaseItemsFragment: Fragment() {
    protected val listViewModel: ItemsViewModel by viewModels()
    protected val profileViewModel: ProfileViewModel by activityViewModels()
    protected lateinit var binding: FragmentItemsBinding

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
        listViewModel.showProgressBar.observe(viewLifecycleOwner, showProgressBarLiveDataObserver)
        listViewModel.refresh.observe(viewLifecycleOwner, refreshLiveDataLiveDataObserver)

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

    abstract fun refresh()

    private val showProgressBarLiveDataObserver = Observer<Boolean>{
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }

    private val refreshLiveDataLiveDataObserver = Observer<Boolean>{
        refresh()
    }
    companion object {
        const val TIMEOUT: Long = 5000
    }
}