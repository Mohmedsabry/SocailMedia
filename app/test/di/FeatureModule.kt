package test/.di

import test/.repo.testRepository
import test/.repo.testRepositoryImpl
import test/.usecase.testUseCase
import test/.mapper.testMapper
import test/.remote.testRemoteService
import test/.remote.testRemoteServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object FeatureModule {
    @Provides
    fun providetestUseCase(
        repository: testRepository
    ): testUseCase {
        return testUseCase(repository)
    }

    @Provides
    fun providetestRepository(
        remoteService: testRemoteService,
        mapper: testMapper
    ): testRepository {
        return testRepositoryImpl(remoteService, mapper)
    }

    @Provides
    fun providetestMapper(): testMapper {
        return testMapper()
    }
}