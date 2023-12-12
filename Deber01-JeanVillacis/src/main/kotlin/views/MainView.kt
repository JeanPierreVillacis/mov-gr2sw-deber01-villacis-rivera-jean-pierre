package views

import services.SupermercadoService

class MainView {
    private val tables = ConsoleTable()
    private val sucursalView = SucursalView()
    private val supermercadoView = SupermercadoView()

    fun init() {
        header()
        selectEntitiesMenu()
    }

    fun header() {
        val tableWithTitle = tables.createTableWithText("Bienvenido a la Aplicación de Gestión de Series")
        println(tableWithTitle)
    }

    fun selectEntitiesMenu() {
        var showAgain = false
        do {
            println("Seleccione una opción:")
            println("1. Series")
            println("2. Streaming Services")
            println("3. Salir")

            val option = readln().toInt()
            when (option) {
                1 -> {
                    if (SupermercadoService.getInstance().safeGetAll().isEmpty()) {
                        println("No existen Servicios de Streaming, primero crea uno.")
                        showAgain = true
                    } else {
                        sucursalView.selectSucursalMenu(this)
                    }
                }
                2 -> supermercadoView.selectSupermercadoMenu(this)
                3 -> println("Hasta pronto!")
                else -> println("Opción no válida")
            }
        } while (showAgain)

    }


}