package com.stopsmoke.kekkek.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATASTORE_NAME = "kekkek_prefs"

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext appContext: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create {
        appContext.preferencesDataStoreFile(DATASTORE_NAME)
    }

}