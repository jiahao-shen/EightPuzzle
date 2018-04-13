package com.sam.eightpuzzle

class SolvePuzzle(lists: ArrayList<ItemModel>) {

    val size = 3

    private var puzzle: Array<IntArray> = Array(size) { IntArray(size) }

    var sx = -1

    var sy = -1

    private val move = arrayOf(intArrayOf(-1, 0), intArrayOf(0, -1), intArrayOf(0, 1), intArrayOf(1, 0))    //上左右下

    private var path = IntArray(100)

    var pathLength = 0

    private var limit = 0

    private var flag = false

    init {
        for ((index, value) in lists.withIndex()) {
            if (value.number == size * size - 1) {
                sx = index / size
                sy = index % size
            }
            puzzle[index / size][index % size] = value.number
        }

    }

    private fun getManhattanValue(): Int {
        var value = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                val number = puzzle[i][j]
                value += Math.abs(i - number / size) + Math.abs(j - number % size)
            }
        }
        return value
    }

    private fun dfs(sx: Int, sy: Int, len: Int, preMove: Int) {
        if (flag)
            return

        val dis = getManhattanValue()

        if (len == limit) {
            if (dis == 0) {
                flag = true
                pathLength = len
            }
            return
        } else if (len < limit) {
            if (dis == 0) {
                flag = true
                pathLength = len
                return
            }
        }

        for (i in 0..3) {
            if (i + preMove == 3 && len > 0)
                continue
            val nx = sx + move[i][0]
            val ny = sy + move[i][1]
            if (nx in 0..(size - 1) && ny in 0..(size - 1)) {
                puzzle.swap(sx, sy, nx, ny)
                val value = getManhattanValue()
                if (value + len <= limit && !flag) {
                    path[len] = i
                    dfs(nx, ny, len + 1, i)
                }
                puzzle.swap(sx, sy, nx, ny)
            }
        }
    }

    fun solve(): IntArray {
        limit = getManhattanValue()

        print(limit)

        do {
            dfs(sx, sy, 0, 0)
            limit++
        } while(!flag)

        return path
    }

    private fun Array<IntArray>.swap(index1_i: Int, index1_j: Int, index2_i: Int, index2_j: Int) {
        val temp = this[index1_i][index1_j]
        this[index1_i][index1_j] = this[index2_i][index2_j]
        this[index2_i][index2_j] = temp
    }

}
