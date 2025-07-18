package com.example.shopaine.model.repository.comment

import com.example.shopaine.model.data.Ads
import com.example.shopaine.model.data.Comment
import com.example.shopaine.model.data.Product

interface CommentRepository {

    suspend fun getAllComments(productId: String ) : List<Comment>
    suspend fun addNewComment(productId: String , text :String , isSuccess: (String)-> Unit)

}