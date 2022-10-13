package com.durini.solucionlab10.data.repository

import com.durini.solucionlab10.data.local.dao.CharacterDao
import com.durini.solucionlab10.data.util.DataState
import com.durini.solucionlab10.data.local.model.Character
import com.durini.solucionlab10.data.remote.api.RickMortyApi
import com.durini.solucionlab10.data.remote.dto.mapToModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CharacterRepositoryImpl (

    private val api : RickMortyApi,
    private val characterDao : CharacterDao

        ): CharacterRepository {
    override suspend fun getAllCharacters(): Flow<DataState<List<Character>>> = flow {

        emit(DataState.Loading)
        val localCharacters = characterDao.getCharacters()
        if( localCharacters.isEmpty()) {
           try {
               val remoteCharacters = api.getCharacters().results
               val charactersToStore = remoteCharacters.map {dto -> dto.mapToModel()}
               characterDao.insertAll(charactersToStore)
               emit(DataState.Success(charactersToStore))

           } catch (e: Exception){
               emit(DataState.Error(e))
           }

        } else {
            emit(DataState.Success(localCharacters))
        }

    }

    override suspend fun getCharacterById(): Flow<Character> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacter(id: Int): Flow<DataState<Character?>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCharacter(character: Character): Flow<DataState<Int>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCharacter(id: Int): Flow<DataState<Int>> {
        TODO("Not yet implemented")
    }
}