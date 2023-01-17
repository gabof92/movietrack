package com.bit45.movietrack.placeholder

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<Bucket> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, Bucket> = HashMap()

    init {
        createPlaceholderItems()
    }

    private fun addItem(item: Bucket) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createPlaceholderItems(){
        // Add some sample items.
        addItem(Bucket("1","Ultimate Bucket List Poster", "100 Movies"))
        addItem(Bucket("1","Chris Stuckman - Best of 2022", "10 Movies"))
        addItem(Bucket("1","YourMovieSucksDotOrg - Best movies of 2019", "32 Movies"))
        addItem(Bucket("1","Super duper hyper mega long string to test the limit of the TextView", "2687 Movies"))
        addItem(Bucket("1","A list", "4 Movies"))
        for (i in 1..20){
            val random = (3..100).random()
            addItem(Bucket("$i","Sample List #$i", "$random Movies"))
        }
    }


    /**
     * A placeholder item representing a piece of content.
     */
    data class Bucket(val id: String, val name: String, val description: String) {
        override fun toString(): String = "$name - $description"
    }
}