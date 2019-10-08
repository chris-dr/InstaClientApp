package com.drevnitskaya.instaclientapp.data.source.local

import com.drevnitskaya.instaclientapp.data.entities.DataResponse
import com.drevnitskaya.instaclientapp.data.entities.FeedItem

interface FeedLocalDataSource {
    suspend fun loadFeed(): DataResponse<List<FeedItem>>
}