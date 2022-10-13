package com.durini.solucionlab10.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent:: class)
object AppModule {

    @Provides
    @Singleton

    fun provideLoggintInterceptor(): HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)


}