package com.example.rpn_calculator.di

import android.content.Context
import com.example.rpn_calculator.data.CalculatorRepositoryImpl
import com.example.rpn_calculator.domain.CalculateUseCase
import com.example.rpn_calculator.domain.RpnCalculator
import com.example.rpn_calculator.domain.repository.CalculatorRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRpnCalculator(@ApplicationContext context: Context): RpnCalculator {
        return RpnCalculator(context)
    }

    @Provides
    @Singleton
    fun provideCalculatorRepository(
        rpnCalculator: RpnCalculator,
        @ApplicationContext context: Context
    ): CalculatorRepository {
        return CalculatorRepositoryImpl(rpnCalculator, context)
    }

    @Provides
    @Singleton
    fun provideCalculateUseCase(repository: CalculatorRepository): CalculateUseCase {
        return CalculateUseCase(repository)
    }
}
