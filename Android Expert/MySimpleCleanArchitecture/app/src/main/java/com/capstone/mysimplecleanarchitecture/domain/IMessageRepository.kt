package com.capstone.mysimplecleanarchitecture.domain

interface IMessageRepository {

    fun getWelcomeMessage(name: String): MessageEntity
}