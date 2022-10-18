package com.durini.solucionlab10.ui.character_list

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.durini.solucionlab10.R
import com.durini.solucionlab10.data.local.LabDatabase
import com.durini.solucionlab10.data.local.model.Character
import com.durini.solucionlab10.data.remote.api.RetrofitInstance
import com.durini.solucionlab10.data.remote.dto.CharacterDto
import com.durini.solucionlab10.data.remote.dto.CharactersResponse
import com.durini.solucionlab10.data.remote.dto.mapToModel
import com.durini.solucionlab10.ui.KEY_EMAIL
import com.durini.solucionlab10.ui.adapters.CharacterAdapter
import com.durini.solucionlab10.ui.dataStore
import com.durini.solucionlab10.ui.removePreferencesValue
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class CharacterListFragment : Fragment(R.layout.fragment_character_list), CharacterAdapter.RecyclerViewCharactersEvents {

    private lateinit var adapter: CharacterAdapter
    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerCharacters: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var database: LabDatabase
    private val viewModel: CharacterListViewModel by viewModels()

    private val characters: MutableList<Character> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerCharacters = view.findViewById(R.id.recycler_characters)
        toolbar = view.findViewById(R.id.toolbar_characterList)
        progressBar = view.findViewById(R.id.progress_characters)
        database = Room.databaseBuilder(
            requireContext(),
            LabDatabase::class.java,
            "labDatabase"
        ).build()

        setToolbar()
        setListeners()
        getCharacters()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setToolbar() {
        val navController = findNavController()
        val appbarConfig = AppBarConfiguration(setOf(R.id.characterListFragment))

        toolbar.setupWithNavController(navController, appbarConfig)
    }

    private fun setListeners() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_item_asc -> {
                    characters.sortBy { character -> character.name }
                    adapter.notifyDataSetChanged()
                    true
                }

                R.id.menu_item_des -> {
                    characters.sortByDescending { character -> character.name }
                    adapter.notifyDataSetChanged()
                    true
                }

                R.id.menu_item_logout -> {
                    logout()
                    true
                }

                R.id.menu_item_sync -> {
                    fetchCharacters(isSync = true)
                    true
                }
                else -> true
            }
        }
    }

    private fun setObservables(){

    }

    private fun getCharacters() {
        lifecycleScope.launchWhenStarted {
            viewModel.screenState.collectLatest { state ->

            }
        }

    }

    private fun fetchCharacters(isSync: Boolean) {

    }

    private fun showCharacters(characters: List<Character>, isSync: Boolean) {
        progressBar.visibility = View.GONE
        recyclerCharacters.visibility = View.VISIBLE
        this.characters.clear()
        this.characters.addAll(characters)

        if (!isSync) {
            setupRecycler()
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecycler() {
        adapter = CharacterAdapter(this.characters, this)
        recyclerCharacters.layoutManager = LinearLayoutManager(requireContext())
        recyclerCharacters.setHasFixedSize(true)
        recyclerCharacters.adapter = adapter
    }

    private fun saveCharactersLocally(characters: List<CharacterDto>, isSync: Boolean) {
        lifecycleScope.launch {
            val charactersToStore = characters.map { characterDto -> characterDto.mapToModel() }
            database.characterDao().insertAll(charactersToStore)
            showCharacters(charactersToStore, isSync)
        }
    }

    private fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            requireContext().dataStore.removePreferencesValue(KEY_EMAIL)
            CoroutineScope(Dispatchers.Main).launch {
                requireView().findNavController().navigate(
                    CharacterListFragmentDirections.actionCharacterListFragmentToLoginFragment()
                )
            }
        }
    }

    override fun onItemClicked(character: Character) {
        val action = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailsFragment(
            character.id
        )

        requireView().findNavController().navigate(action)
    }

}