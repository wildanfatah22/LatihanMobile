package com.capstone.mysimplecleanarchitecture.di

import com.capstone.mysimplecleanarchitecture.data.IMessageDataSource
import com.capstone.mysimplecleanarchitecture.data.MessageDataSource
import com.capstone.mysimplecleanarchitecture.data.MessageRepository
import com.capstone.mysimplecleanarchitecture.domain.IMessageRepository
import com.capstone.mysimplecleanarchitecture.domain.MessageInteractor
import com.capstone.mysimplecleanarchitecture.domain.MessageUseCase

object Injection {

    fun provideUseCase(): MessageUseCase {
        val messageRepository = provideRepository()
        return MessageInteractor(messageRepository)
    }

    private fun provideRepository(): IMessageRepository {
        val messageDataSource = provideDataSource()
        return MessageRepository(messageDataSource)
    }

    private fun provideDataSource(): IMessageDataSource {
        return MessageDataSource()
    }

}