package com.capstone.mysimplecleanarchitecture.domain

interface MessageUseCase {
    fun getMessage(name: String) : MessageEntity
}