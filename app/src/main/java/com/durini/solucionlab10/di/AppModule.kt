package com.durini.solucionlab10.di

import android.content.Context
import androidx.room.Room
import com.durini.solucionlab10.data.local.LabDatabase
import com.durini.solucionlab10.data.local.dao.CharacterDao
import com.durini.solucionlab10.data.remote.api.RickMortyApi
import com.durini.solucionlab10.data.repository.CharacterRepository
import com.durini.solucionlab10.data.repository.CharacterRepositoryImpl
import com.durini.solucionlab10.data.util.API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor():HttpLoggingInterceptor{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    @Singleton
    fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient): RickMortyApi {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RickMortyApi::class.java)
    }

    @Provides
    @Singleton

    fun provideDataBase(context : Context): LabDatabase{
        return Room.databaseBuilder(
            context,
            LabDatabase::class.java,
            "labDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: LabDatabase) : CharacterDao {
        return database.characterDao()
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(api: RickMortyApi, dao: CharacterDao) : CharacterRepository {
        return CharacterRepositoryImpl(
            api = api,
            characterDao = dao
        )
    }

}