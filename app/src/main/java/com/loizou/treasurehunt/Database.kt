package com.loizou.treasurehunt

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Database {
    val db = Firebase.firestore

    init {
        // Fetch treasure hunts
    }
}