package com.example.apkcocina.di

import android.content.Context
import androidx.room.Room
import com.example.apkcocina.utils.db.APKCocinaDataBase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Singleton
    @Provides
    fun providesapkCocinaDataBase(@ApplicationContext appcontext:Context) : APKCocinaDataBase{
        return Room.databaseBuilder(appcontext.applicationContext,APKCocinaDataBase::class.java,"APKCocinaDataBase").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesFirestore() :FirebaseFirestore = FirebaseFirestore.getInstance()

}