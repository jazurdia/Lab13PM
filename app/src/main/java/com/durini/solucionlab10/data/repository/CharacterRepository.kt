package com.durini.solucionlab10.data.repository

import com.durini.solucionlab10.data.local.model.Character
import com.durini.solucionlab10.data.util.Resource

interface CharacterRepository {
    suspend fun getAllCharacters(): Resource<List<Character>>
    suspend fun deleteAllCharacters(): Resource<Unit>
    suspend fun getCharacter(id: Int): Resource<Character?>
    suspend fun updateCharacter(character: Character): Resource<Unit>
    suspend fun deleteCharacter(id: Int): Resource<Unit>
}