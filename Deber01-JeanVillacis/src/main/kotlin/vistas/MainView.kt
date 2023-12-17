package vistas

import servicios.SupermercadoService

class MainView {
    private val tables = ConsoleTable()
    private val sucursalView = SucursalView()
    private val supermercadoView = SupermercadoView()

    fun init() {
        header()
        selectEntitiesMenu()
    }

    fun header() {
        val tableWithTitle = tables.createTableWithText("---Bienvenido al Gestor de Supermercados y Sucursales--")
        println(tableWithTitle)
    }

    fun selectEntitiesMenu() {
        var showAgain = false
        do {
            println("Seleccione una opción:")
            println("1. Sucursales")
            println("2. Supermercados")
            println("3. Salir")

            val option = readln().toInt()
            when (option) {
                1 -> {
                    if (SupermercadoService.getInstance().safeGetAll().isEmpty()) {
                        println("No existen Supermercados, primero crea uno.")
                        showAgain = true
                    } else {
                        sucursalView.selectSucursalMenu(this)
                    }
                }
                2 -> supermercadoView.selectSupermercadoMenu(this)
                3 -> println("Gracias por utilizar nuestra aplicacion!")
                else -> println("Opción no válida")
            }
        } while (showAgain)

    }


}