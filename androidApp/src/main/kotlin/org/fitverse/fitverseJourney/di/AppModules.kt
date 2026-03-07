package org.fitverse.fitverseJourney.di

import com.example.data.di.dataModules
import com.example.di.presentationModule
import com.example.domain.di.domainModule
import org.fitverse.project.navigation.NavRoutes
import org.koin.dsl.module

val appModule = listOf(
    presentationModule,
    domainModule,
    dataModules
)