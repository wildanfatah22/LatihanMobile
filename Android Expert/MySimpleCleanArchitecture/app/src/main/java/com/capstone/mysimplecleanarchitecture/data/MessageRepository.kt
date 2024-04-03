package com.capstone.mysimplecleanarchitecture.data

import com.capstone.mysimplecleanarchitecture.domain.IMessageRepository

class MessageRepository(private val messageDataSource: IMessageDataSource) : IMessageRepository {
    override fun getWelcomeMessage(name: String) = messageDataSource.getMessageFromSource(name)
}