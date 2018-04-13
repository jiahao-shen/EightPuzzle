package com.sam.eightpuzzle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: ItemAdapter
    private var itemList = ArrayList<ItemModel>()
    private lateinit var specialItem: ItemModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.addLogAdapter(AndroidLogAdapter())

        layoutManager = GridLayoutManager(this, 3)

        for (i in 0..8) {
            itemList.add(ItemModel(i))
        }

        adapter = ItemAdapter(itemList)
        imageRecyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        imageRecyclerView.adapter = adapter

        if (isSolved()) {
            Log.i("isSolved", "true")
        } else {
            Log.i("isSolved", "false")
        }

        resetButton.setOnClickListener {
            do {
                itemList.shuffle()
            } while (!isSolved())
            adapter.notifyDataSetChanged()
        }

        recoverButton.setOnClickListener {
            val time1 = System.currentTimeMillis()
            val sp = SolvePuzzle(itemList)
            val path = sp.solve()
            val time2 = System.currentTimeMillis()
            Logger.i((time2 - time1).toString())
            specialItem = itemList[sp.sx * sp.size + sp.sy]
            resetButton.isClickable = false
            recoverButton.isClickable = false
            resetButton.bootstrapBrand = DefaultBootstrapBrand.REGULAR
            recoverButton.bootstrapBrand = DefaultBootstrapBrand.REGULAR
            Thread(Runnable {
                for (i in 0 until sp.pathLength) {
                    Thread.sleep(200)
                    runOnUiThread {
                        when (path[i]) {
                            0 -> Collections.swap(itemList, itemList.indexOf(specialItem) - sp.size, itemList.indexOf(specialItem))
                            1 -> Collections.swap(itemList, itemList.indexOf(specialItem) - 1, itemList.indexOf(specialItem))
                            2 -> Collections.swap(itemList, itemList.indexOf(specialItem) + 1, itemList.indexOf(specialItem))
                            3 -> Collections.swap(itemList, itemList.indexOf(specialItem) + sp.size, itemList.indexOf(specialItem))
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
                runOnUiThread {
                    resetButton.isClickable = true
                    recoverButton.isClickable = true
                    resetButton.bootstrapBrand = DefaultBootstrapBrand.PRIMARY
                    recoverButton.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                }
            }).start()

        }
    }


    private fun isSolved(): Boolean {
        var cnt = 0
        for (i in itemList.indices) {
            for (j in i + 1 until itemList.size) {
                if (itemList[i].number > itemList[j].number && itemList[i].number != itemList.size - 1 && itemList[j].number != itemList.size - 1)
                    cnt++
            }
        }
        return cnt % 2 == 0

    }
}
