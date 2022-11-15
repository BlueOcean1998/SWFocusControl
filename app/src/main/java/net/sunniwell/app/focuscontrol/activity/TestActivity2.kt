package net.sunniwell.app.focuscontrol.activity

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import net.sunniwell.aar.focuscontrol.layout.FocusControlRecyclerView
import net.sunniwell.aar.focuscontrol.manager.FocusControlGridLayoutManager
import net.sunniwell.aar.focuscontrol.manager.FocusControlLinearLayoutManager
import net.sunniwell.aar.focuscontrol.manager.FocusControlStaggeredGridLayoutManager
import net.sunniwell.app.focuscontrol.adapter.TestAdapter1
import net.sunniwell.app.focuscontrol.base.BaseActivity
import net.sunniwell.app.focuscontrol.databinding.ActivityTest2Binding
import java.util.*
import kotlin.random.Random

/**
 * 测试活动2
 *
 * @author YSK
 * @date 2021-07-17
 */
class TestActivity2 : BaseActivity<ActivityTest2Binding>() {
    override fun bindView() = ActivityTest2Binding.inflate(layoutInflater)

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated() {
        val activity = this
        viewBinding.run {
            focusControlRv1.run {
//                isBoundaryFocus = true
                layoutManager = FocusControlLinearLayoutManager(
                    activity, RecyclerView.VERTICAL, true
                ).apply {
//                    isHoldFocusInCenter = false
                }
                adapter = TestAdapter1(randomArrayList(ArrayList())).apply {
                    onItemClick = { _, _ ->
                        if (orientation == RecyclerView.VERTICAL) {
                            orientation = RecyclerView.HORIZONTAL
                            searchFocusLeft = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusRight = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusUp = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusDown = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                        } else {
                            orientation = RecyclerView.VERTICAL
                            searchFocusLeft = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusRight = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusUp = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusDown = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                        }
                        notifyDataSetChanged()
                    }
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            randomArrayList(stringList)
                            runOnUiThread { notifyDataSetChanged() }
                        }
                    }, 10000, 10000)
                }
            }
            focusControlRv2.run {
//                isBoundaryFocus = true
                layoutManager = FocusControlGridLayoutManager(
                    activity, 2, RecyclerView.VERTICAL, true
                ).apply {
//                    isHoldFocusInCenter = false
                }
                adapter = TestAdapter1(randomArrayList(ArrayList())).apply {
                    onItemClick = { _, _ ->
                        if (orientation == RecyclerView.VERTICAL) {
                            orientation = RecyclerView.HORIZONTAL
                            searchFocusLeft = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusRight = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusUp = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusDown = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                        } else {
                            orientation = RecyclerView.VERTICAL
                            searchFocusLeft = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusRight = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusUp = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusDown = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                        }
                        notifyDataSetChanged()
                    }
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            randomArrayList(stringList)
                            runOnUiThread { notifyDataSetChanged() }
                        }
                    }, 10000, 10000)
                }
            }
            focusControlRv3.run {
//                isBoundaryFocus = true
                layoutManager = FocusControlStaggeredGridLayoutManager(
                    3, RecyclerView.VERTICAL, true
                ).apply {
//                    isHoldFocusInCenter = false
                }
                adapter = TestAdapter1(randomArrayList(ArrayList())).apply {
                    onItemClick = { _, _ ->
                        if (orientation == RecyclerView.VERTICAL) {
                            orientation = RecyclerView.HORIZONTAL
                            searchFocusLeft = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusRight = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusUp = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusDown = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                        } else {
                            orientation = RecyclerView.VERTICAL
                            searchFocusLeft = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusRight = FocusControlRecyclerView.DEF_SEARCH_FOCUS
                            searchFocusUp = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                            searchFocusDown = FocusControlRecyclerView.NULL_SEARCH_FOCUS
                        }
                        notifyDataSetChanged()
                    }
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            randomArrayList(stringList)
                            runOnUiThread { notifyDataSetChanged() }
                        }
                    }, 10000, 10000)
                }
            }
        }
        super.onViewCreated()
    }

    private val mStringListPool = ArrayList<String>().apply {
        var index = 0
        repeat(256) { add(index++.toString()) }
    }

    private fun randomArrayList(stringList: ArrayList<String>): ArrayList<String> {
        try {
            if (stringList.isEmpty()) {
                repeat(mStringListPool.size / 4) {
                    stringList.add(mStringListPool[Random.nextInt(mStringListPool.size)])
                }
            }
            repeat(Random.nextInt(stringList.size / 3)) {
                stringList.removeAt(Random.nextInt(stringList.size))
            }
            repeat(Random.nextInt(stringList.size / 3)) {
                val string = stringList.removeAt(Random.nextInt(stringList.size))
                stringList.add(Random.nextInt(stringList.size), string)
            }
            repeat(Random.nextInt(mStringListPool.size / 4)) {
                stringList.add(
                    Random.nextInt(stringList.size),
                    mStringListPool[Random.nextInt(mStringListPool.size)]
                )
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        return stringList
    }
}