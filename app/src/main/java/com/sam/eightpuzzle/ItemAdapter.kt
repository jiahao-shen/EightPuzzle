package com.sam.eightpuzzle

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.orhanobut.logger.Logger

/**
 * Created by sam on 2018/4/3.
 */
class ItemAdapter(data: ArrayList<ItemModel>) :
    BaseQuickAdapter<ItemModel, BaseViewHolder>(R.layout.item_view, data) {

    @SuppressLint("ResourceAsColor")
    override fun convert(helper: BaseViewHolder, item: ItemModel) {
        helper.setText(R.id.itemTextView, "${item.number}")
        if (item.number == data.size - 1) {
            helper.setText(R.id.itemTextView, "")
        }
    }


}