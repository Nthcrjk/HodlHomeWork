package ru.flx.hodlhomework.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.flx.hodlhomework.repositories.bitcoinj.BitcoinjRepository
import ru.flx.hodlhomework.repositories.rest_api.ApiService
import ru.flx.hodlhomework.ui.home.HomeInteractor

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideHomeInteractor(bitcoinjRepository: BitcoinjRepository, restApi: ApiService): HomeInteractor {
        return HomeInteractor(bitcoinjRepository, restApi)
    }
}