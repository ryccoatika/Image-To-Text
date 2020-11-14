package com.ryccoatika.imagetotext.di

import com.ryccoatika.imagetotext.home.HomeView
import com.ryccoatika.imagetotext.home.HomeViewModel
import com.ryccoatika.imagetotext.textscanneddetail.TextScannedDetailView
import com.ryccoatika.imagetotext.textscanneddetail.TextScannedDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (homeView: HomeView) -> HomeViewModel(get(), homeView) }
    viewModel { (textScannedDetailView: TextScannedDetailView) -> TextScannedDetailViewModel(get(), textScannedDetailView) }
}