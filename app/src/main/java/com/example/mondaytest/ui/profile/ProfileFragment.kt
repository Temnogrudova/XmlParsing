package com.example.mondaytest.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.mondaytest.R
import com.example.mondaytest.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*


@AndroidEntryPoint
open class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserves()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name.text = getString(R.string.name, "Ekaterina Temnogrudova")
        val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
        binding.dateAndTime.text = getString(R.string.date_and_time, currentDateTimeString)
        binding.openFeed.setOnClickListener {
            view.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFeedFragment())
        }
    }

    private fun initObserves() {
        profileViewModel.lastSeenFeedTitle.observe(this, lastSeenFeedTitleObserver)
    }

    private val lastSeenFeedTitleObserver = Observer<String>{
        binding.lastOpenedFeed.text = it
    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }

    private fun removeObservers() {
        profileViewModel.lastSeenFeedTitle.removeObserver(lastSeenFeedTitleObserver)
    }
}