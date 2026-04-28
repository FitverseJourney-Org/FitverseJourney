package org.fitverse.project.di

import com.example.di.dataModule
import com.example.di.presentationModules
import com.example.domain.di.domainModule
import org.koin.core.module.Module

val appModules: List<Module> = presentationModules + domainModule + dataModule