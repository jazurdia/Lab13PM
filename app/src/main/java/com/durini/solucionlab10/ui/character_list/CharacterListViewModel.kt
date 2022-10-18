package com.durini.solucionlab10.ui.character_list

import androidx.lifecycle.ViewModel
import com.durini.solucionlab10.data.repository.CharacterRepository
import com.durini.solucionlab10.data.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class CharacterListViewModel @Inject constructor  (

    private val repository : CharacterRepository

) : ViewModel() {

    private val _screenState : MutableStateFlow<ListUiState> = MutableStateFlow(ListUiState.Empty)
    val screenState : StateFlow<ListUiState> = _screenState

    sealed interface ListUiState{
        object Empty : ListUiState
        object Loading : ListUiState
        data class Success(val data: List<com.durini.solucionlab10.data.local.model.Character>) : ListUiState
        data class Error(val message: String) : ListUiState
    }

    suspend fun getCharacterList() {
        val response = repository.getAllCharacters()

        when(response){
            is Resource.Error -> {
                // Emitir estado de error con mensaje.
                _screenState.value = ListUiState.Error(message = response.message ?: "")
            }
            is Resource.Success -> {
                // 1. Validar si la lista esta vacía
                // Si está vacia retornar estado empty
                // Si no está vacia retornar estado success

                if(response.data.isNullOrEmpty()){
                    _screenState.value = ListUiState.Empty
                } else {
                    _screenState.value = ListUiState.Success(data = response.data)
                }

            }
        }
    }

}
