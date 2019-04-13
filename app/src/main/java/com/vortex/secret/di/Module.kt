package com.vortex.secret.di

import com.vortex.secret.data.local.ILocalPreferences
import com.vortex.secret.data.local.LocalPreferences
import com.vortex.secret.data.remote.FirestoreManager
import com.vortex.secret.data.remote.IFirestoreManager
import com.vortex.secret.data.repository.AuthRepository
import com.vortex.secret.data.repository.IAuthRepository
import com.vortex.secret.data.repository.IPostRepository
import com.vortex.secret.data.repository.PostRepository
import com.vortex.secret.ui.SplashViewModel
import com.vortex.secret.ui.app.AppViewModel
import com.vortex.secret.ui.app.comment.PostCommentViewModel
import com.vortex.secret.ui.app.home.HomeViewModel
import com.vortex.secret.ui.app.settings.SettingsViewModel
import com.vortex.secret.ui.auth.sign_in.SignInViewModel
import com.vortex.secret.ui.auth.sign_up.SignUpViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val dataModule: Module = module {
    single { AuthRepository(get(), get()) as IAuthRepository }
    single { LocalPreferences(get()) as ILocalPreferences }
    single { PostRepository(get()) as IPostRepository }
    single { FirestoreManager() as IFirestoreManager }
}

val viewModelModule: Module = module {
    viewModel { PostCommentViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { AppViewModel(get()) }
}