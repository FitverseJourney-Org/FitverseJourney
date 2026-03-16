package org.fitverse.fitverseJourney.di

import com.example.data.di.dataModules
import com.example.di.presentationModule
import com.example.domain.di.domainModule

val appModule = listOf(
    presentationModule,
    domainModule,
    dataModules
)