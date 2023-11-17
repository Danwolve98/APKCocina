package com.example.apkcocina.utils.base

open class APKCocinaActionBar(
    var title: String,
    var haveBack: Boolean = true,
    var orderFunction: (() -> Unit)? = null,
    var filterFunction: (() -> Unit)? = null,
    var searchFunction : (() -> Unit)? = null,
    var infoLayoutFunction : (() -> Unit)? = null) : ConfigureActionBar


class PrincipalActionBar(title:String) : APKCocinaActionBar(
    title = title,
    haveBack = false
)

class TitleActionBar(title:String) : APKCocinaActionBar(
    title = title,
    filterFunction = null,
    searchFunction = null
)

class InfoActionBar(title:String,infoLayoutFunction : (() -> Unit)) : APKCocinaActionBar(
    title = title,
    filterFunction = null,
    searchFunction = null,
    infoLayoutFunction = infoLayoutFunction
)

class FilterActionBar(title:String,filterFunction: (() -> Unit)?,searchFunction: (() -> Unit)?) : APKCocinaActionBar(
    title = title,
    filterFunction = filterFunction,
    searchFunction = searchFunction
)

/*enum class OrderState{
    ALPHABETICAL,
    TIEMPO
}*/

interface ConfigureActionBar