package com.durini.solucionlab10.data.repository

import com.durini.solucionlab10.data.local.dao.CharacterDao
import com.durini.solucionlab10.data.local.model.Character
import com.durini.solucionlab10.data.util.DataState
import kotlinx.coroutines.flow.Flow

interface CharacterRepository{

    suspend fun getAllCharacters(): Flow<DataState<List<Character>>>
    suspend fun getCharacterById() : Flow<com.durini.solucionlab10.data.local.model.Character>
    suspend fun getCharacter(id: Int) : Flow<DataState<Character?>>
    suspend fun updateCharacter(character : Character) : Flow<DataState<Int>>
    suspend fun deleteCharacter(id: Int) : Flow<DataState<Int>>

}