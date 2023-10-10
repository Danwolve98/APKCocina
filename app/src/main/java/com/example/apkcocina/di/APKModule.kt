package com.example.apkcocina.di

import android.content.Context
import androidx.room.Room
import com.example.apkcocina.utils.db.APKCocinaDataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object APKModule {

    @Singleton
    @Provides
    fun providesapkCocinaDataBase(@ApplicationContext appcontext:Context) : APKCocinaDataBase{
        return Room.databaseBuilder(appcontext.applicationContext,APKCocinaDataBase::class.java,"APKCocinaDataBase").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesFirestore() :FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun providesStorage() : FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun providesAuth() : FirebaseAuth = FirebaseAuth.getInstance().also { it.setLanguageCode("es") }
}