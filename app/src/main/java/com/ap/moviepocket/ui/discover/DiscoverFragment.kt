package com.ap.moviepocket.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ap.moviepocket.databinding.FragmentDiscoverBinding
import com.ap.moviepocket.domain.data
import com.ap.moviepocket.domain.succeeded
import com.ap.moviepocket.util.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.subscribe
import timber.log.Timber

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    private val discoverViewModel: DiscoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDiscoverBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = discoverViewModel
        }

        val adapter = MovieListAdapter()
        binding.recyclerViewDiscover.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeUi(adapter: MovieListAdapter) {
        launchAndRepeatWithViewLifecycle {
            discoverViewModel.movieList.collect {
                if (it.succeeded) adapter.submitList(it.data)
            }
        }
    }
}