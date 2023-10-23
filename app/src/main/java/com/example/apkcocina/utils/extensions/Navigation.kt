package com.example.apkcocina.utils.extensions

import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import com.google.android.material.navigation.NavigationBarView

fun NavDestination.getDestinationId(): Int =
    if (this is NavGraph)
        this.startDestinationId
    else
        this.id

@OptIn(NavigationUiSaveStateControl::class)
fun NavController.navigateToMenuItem(menuItem: MenuItem) : Boolean{
    if (this.findDestination(menuItem.itemId)?.getDestinationId() != this.currentDestination?.id)
        NavigationUI.onNavDestinationSelected(menuItem,this,false)

    return true
}