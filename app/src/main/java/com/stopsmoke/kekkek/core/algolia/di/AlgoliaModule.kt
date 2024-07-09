package com.stopsmoke.kekkek.core.algolia.di

import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.stopsmoke.kekkek.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AlgoliaModule {
    @Provides
    fun provideClientSearch(): ClientSearch = ClientSearch(
        applicationID = ApplicationID(BuildConfig.ALGOLIA_APPLICATION_ID),
        apiKey = APIKey(BuildConfig.ALGOLIA_SEARCH_API_KEY),
    )
}