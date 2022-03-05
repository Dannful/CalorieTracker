package me.dannly.onboarding_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.dannly.onboarding_domain.use_case.FilterOutDecimalDigits
import me.dannly.onboarding_domain.use_case.ValidateNutrients

@Module
@InstallIn(ViewModelComponent::class)
object OnboardingDomainModule {

    @Provides
    @ViewModelScoped
    fun provideFilterOutDecimalDigitsUseCase(): FilterOutDecimalDigits = FilterOutDecimalDigits()

    @Provides
    @ViewModelScoped
    fun provideValidateNutrientsUseCase(): ValidateNutrients = ValidateNutrients()
}