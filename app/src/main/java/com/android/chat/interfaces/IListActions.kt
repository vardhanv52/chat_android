package com.android.chat.interfaces

interface IListActions {
    fun loadMoreItems()
    fun onItemClicked(pos: Int)
    fun onEditClicked(pos: Int)
    fun onLikeClicked(pos: Int)
}