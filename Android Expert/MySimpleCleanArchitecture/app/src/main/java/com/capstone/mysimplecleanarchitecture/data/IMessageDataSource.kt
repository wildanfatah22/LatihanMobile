package com.capstone.mysimplecleanarchitecture.data

import com.capstone.mysimplecleanarchitecture.domain.MessageEntity

interface IMessageDataSource {
    fun getMessageFromSource(name : String): MessageEntity
}