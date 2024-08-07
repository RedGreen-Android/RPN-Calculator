package com.example.rpn_calculator.di

import com.example.rpn_calculator.data.CalculatorRepositoryImpl
import com.example.rpn_calculator.domain.CalculateUseCase
import com.example.rpn_calculator.domain.RpnCalculator
import com.example.rpn_calculator.domain.repository.CalculatorRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRpnCalculator(): RpnCalculator {
        return RpnCalculator()
    }

    @Provides
    @Singleton
    fun provideCalculatorRepository(rpnCalculator: RpnCalculator): CalculatorRepository {
        return CalculatorRepositoryImpl(rpnCalculator)
    }

    @Provides
    @Singleton
    fun provideCalculateUseCase(repository: CalculatorRepository): CalculateUseCase {
        return CalculateUseCase(repository)
    }
}
