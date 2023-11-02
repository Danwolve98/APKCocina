package com.example.apkcocina.utils.extensions

import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import com.example.apkcocina.R
import com.google.android.material.navigation.NavigationBarView

fun NavDestination.getDestinationId(): Int =
    if (this is NavGraph)
        this.startDestinationId
    else
        this.id