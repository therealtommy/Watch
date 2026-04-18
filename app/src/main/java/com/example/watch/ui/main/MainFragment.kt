package com.example.watch.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watch.R
import com.example.watch.databinding.FragmentMainBinding
import com.example.watch.ui.main.MainIntent.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(requireContext()) }
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
        observeEffect()

        // Отправляем Intent на загрузку списка
        viewModel.processIntent(LoadWatchlist)

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_add)
        }
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter { imdbID, checked ->
            // Отправляем Intent на переключение выбора
            viewModel.processIntent(ToggleSelection(imdbID))
        }
        binding.rvWatchlist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWatchlist.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                if (_binding != null) {
                    adapter.items = state.watchlist
                    adapter.selectedIds = state.selectedIds
                    binding.tvEmpty.visibility = if (state.watchlist.isEmpty()) View.VISIBLE else View.GONE

                }
            }
        }
    }

    private fun observeEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is MainEffect.ShowMessage -> {
                        android.widget.Toast.makeText(requireContext(), effect.text, android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_selected) {
            viewModel.processIntent(DeleteSelected)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}