package com.android.chat.utils

import com.android.chat.interfaces.IListActions

abstract class ListActions : IListActions {
    override fun loadMoreItems() {}

    override fun onItemClicked(pos: Int) {}

    override fun onEditClicked(pos: Int) {}

    override fun onLikeClicked(pos: Int) {}
}