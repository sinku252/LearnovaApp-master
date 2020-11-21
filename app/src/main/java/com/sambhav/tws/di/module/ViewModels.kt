package com.sambhav.tws.di.module

import com.sambhav.tws.ui.home.classes.viewModel.LiveViewModel
import com.sambhav.tws.ui.home.doubt.viewModel.DoubtViewModel
import com.sambhav.tws.ui.home.exam.viewModel.ExamViewModel
import com.sambhav.tws.ui.home.home.viewModel.HomeViewModel
import com.sambhav.tws.ui.home.notes.viewModel.NotesViewModel
import com.sambhav.tws.ui.home.videos.viewModel.VideoViewModel
import com.sambhav.tws.ui.login.LoginDataViewModel
import com.sambhav.tws.ui.home.viewModel.HomeDataViewModel
import com.sambhav.tws.ui.payemnt.viewModel.PaymentViewModel
import com.sambhav.tws.ui.profile.activities.viewModel.ProfileViewModel
import com.sambhav.tws.ui.schedule.viewModel.ChapterViewModel
import com.sambhav.tws.ui.schedule.viewModel.ScheduleViewModel
import com.sambhav.tws.ui.signUp.viewModel.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val myViewModel = module {
    viewModel {
        HomeDataViewModel(get(),get())
    }

    viewModel {
        LoginDataViewModel(get())
    }

    viewModel {
        VideoViewModel(get(),get())
    }

    viewModel {
        NotesViewModel(get(),get())
    }

    viewModel {
        ScheduleViewModel(get(),get())
    }
    viewModel {
        ProfileViewModel(get(),get())
    }
    viewModel {
        ChapterViewModel(get(),get())
    }

    viewModel {
        DoubtViewModel(get(),get())
    }

    viewModel {
        HomeViewModel(get(),get())
    }

    viewModel {
        LiveViewModel(get(),get())
    }

    viewModel {
        PaymentViewModel(get(),get())
    }

    viewModel {
        SignUpViewModel(get(),get())
    }

    viewModel {
        ExamViewModel(get(),get())
    }

}