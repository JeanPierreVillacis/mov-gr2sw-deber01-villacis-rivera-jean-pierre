package vistas

import datos.SupermercadosDatos
import servicios.SupermercadoService
import modelos.Sucursal
import modelos.Supermercado

class SupermercadoView {

    private val tables = ConsoleTable()

    fun selectSupermercadoMenu(parent: MainView) {
        var goBack = false
        do {
            showStreamingServicesMenu()
            val option = readln().toInt()
            when (option) {
                1 -> listSupermercado()
                2 -> createSupermercado()
                3 -> updateSupermercado()
                4 -> deleteSupermercado()
                5 -> {
                    goBack = true
                    parent.init()
                }
                else -> println("Opción no válida")
            }
        } while (!goBack)
    }

    fun showStreamingServicesMenu() {
        println("Seleccione una opción:")
        println("1. Listar supermercados")
        println("2. Crear supermercado")
        println("3. Actualizar supermercado")
        println("4. Eliminar supermercado")
        println("5. Regresar")
    }

    private fun listSupermercado() {
        val supermercadoServices = SupermercadoService.getInstance().safeGetAll()
        if (supermercadoServices.isEmpty()) {
            println("No hay supermercados")
        } else {
            supermercadoServices.forEach { println(tables.createTableFromList(it.getListOfStringFromData())) }
        }
    }

    private fun createSupermercado() {
        println("Ingrese el nombre del supermercado:")
        val nombre = readln()
        println("Ingrese el telefono  del supermercado:")
        val telefono = readln().toInt()
        println("Vende tecnologia el supermercado (s/n):")
        val vendeTecnologia = readln().toBoolean()

        val supermercadoService = SupermercadosDatos(
            nombre = nombre,
            telefono= telefono,
            vendeTecnologia = vendeTecnologia,
            sucursales = listOf<Sucursal>()
        )

        val createdSupermercado= SupermercadoService.getInstance().create(supermercadoService)
        val formattedData = tables.createTableFromList(createdSupermercado.getListOfStringFromData())
       println(formattedData)
        println("Supermercado creado con éxito")
    }

    private fun updateSupermercado() {
        val supermercados = SupermercadoService.getInstance().safeGetAll()
        if (supermercados.isEmpty()) {
            println("No hay supermercados")
            return
        }

        supermercados.forEachIndexed { index, it -> println("${index + 1}. ${it.getNombre()}") }
        println("Selecciona el supermercado que deseas actualizar:")
        val option = readln().toInt()
        if (option > supermercados.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSupermercado = supermercados[option - 1]
        println(tables.createTableFromList(selectedSupermercado.getListOfStringFromData()))
        println("Ingrese el nuevo nombre del supermercado:")
        val nombre = readln()
        println("Ingrese el nuevo telefono del supermercado:")
        val telefono = readln().toInt()
        println("Ingrese si venden tecnologia en el supermercado: (s/n)")
        val vendeTecnologia= readln().toBoolean()

        val supermercado = Supermercado(
            ruc = selectedSupermercado.getRuc(),
            nombre = nombre,
            telefono = telefono,
            vendeTecnologia= vendeTecnologia,
            sucursales = selectedSupermercado.getSucursales()
        )

        val updatedSupermercado = SupermercadoService.getInstance().update(supermercado)
        if (updatedSupermercado == null) {
            println("No se pudo actualizar el supermercado")
            return
        }
        val formattedData = tables.createTableFromList(updatedSupermercado.getListOfStringFromData())
        println(formattedData)
        println("Supermercado actualizado con éxito")
    }

    private fun deleteSupermercado() {
        val supermercado = SupermercadoService.getInstance().safeGetAll()
        if (supermercado.isEmpty()) {
            println("No hay supermercado")
            return

        }
        supermercado.forEachIndexed { index, it -> println("${index + 1}. ${it.getNombre()}") }
        println("Selecciona el supermercado que deseas eliminar:")
        val option = readln().toInt()
        if (option > supermercado.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSupermercado = supermercado[option - 1]
        if (selectedSupermercado.getSucursales().isNotEmpty()) {
            println("No se puede eliminar el supermercado porque tiene series asociadas")
            return
        }
        val id = selectedSupermercado.getRuc()
        SupermercadoService.getInstance().remove(id)
        println("Supermercado eliminado con éxito")
    }
}