package org.fitverse.project.di

import org.fitverse.data.repository.di.dataModule
import org.fitverse.presentation.di.presentationModules
import org.fitverse.domain.di.domainModule
import org.koin.core.module.Module

val appModules: List<Module> = presentationModules + domainModule + dataModule