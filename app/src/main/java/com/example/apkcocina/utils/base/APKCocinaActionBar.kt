package com.example.apkcocina.utils.base

open class APKCocinaActionBar(var title: String,
                           var haveBack: Boolean,
/*                         var haveOrder: OrderState?,
                           var orderFunction: ((OrderState) -> Unit)?,*/
                           var haveFilter: Boolean,
                           var filterFunction: (() -> Unit)?,
                           var haveSearch: Boolean,
                           var searchFunction: (() -> Unit)?): ConfigureActionBar


class PrincipalActionBar(title:String) : APKCocinaActionBar(
    title = title,
    haveBack = false,
/*    haveOrder = null,
    orderFunction = null,*/
    haveFilter = false,
    filterFunction = null,
    haveSearch = false,
    searchFunction = null,
)
class TitleActionBar(title:String) : APKCocinaActionBar(
    title = title,
    haveBack = true,
/*    haveOrder = null,
    orderFunction = null,*/
    haveFilter = false,
    filterFunction = null,
    haveSearch = false,
    searchFunction = null,
)

class FilterActionBar(title:String,filterFunction: (() -> Unit)?,searchFunction: (() -> Unit)?) : APKCocinaActionBar(
    title = title,
    haveBack = true,
/*    haveOrder = null,
    orderFunction = null,*/
    haveFilter = true,
    filterFunction = filterFunction,
    haveSearch = true,
    searchFunction = searchFunction
)

/*enum class OrderState{
    ALPHABETICAL,
    TIEMPO
}*/

interface ConfigureActionBar