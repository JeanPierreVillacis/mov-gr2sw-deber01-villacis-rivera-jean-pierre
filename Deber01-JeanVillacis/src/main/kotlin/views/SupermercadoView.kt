package views

import datos.SupermercadosDatos
import services.SupermercadoService
import models.Sucursal
import models.Supermercado

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
        println("1. Listar servicios de streaming")
        println("2. Crear servicio de streaming")
        println("3. Actualizar servicio de streaming")
        println("4. Eliminar servicio de streaming")
        println("5. Volver atrás")
    }

    private fun listSupermercado() {
        val supermercadoServices = SupermercadoService.getInstance().safeGetAll()
        if (supermercadoServices.isEmpty()) {
            println("No hay servicios de streaming")
        } else {
            supermercadoServices.forEach { println(tables.createTableFromList(it.getListOfStringFromData())) }
        }
    }

    private fun createSupermercado() {
        println("Ingrese el nombre del servicio de streaming:")
        val nombre = readln()
        println("Ingrese la descripción del servicio de streaming:")
        val telefono = readln().toInt()
        println("Ingrese el precio del servicio de streaming:")
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
        println("Servicio de Streaming creado con éxito")
    }

    private fun updateSupermercado() {
        val supermercados = SupermercadoService.getInstance().safeGetAll()
        if (supermercados.isEmpty()) {
            println("No hay servicios de streaming")
            return
        }

        supermercados.forEachIndexed { index, it -> println("${index + 1}. ${it.getNombre()}") }
        println("Selecciona el servicio de streaming que deseas actualizar:")
        val option = readln().toInt()
        if (option > supermercados.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSupermercado = supermercados[option - 1]
        println(tables.createTableFromList(selectedSupermercado.getListOfStringFromData()))
        println("Ingrese el nuevo nombre del servicio de streaming:")
        val nombre = readln()
        println("Ingrese la nueva descripción del servicio de streaming:")
        val telefono = readln().toInt()
        println("Ingrese el nuevo precio del servicio de streaming:")
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
            println("No se pudo actualizar el servicio de streaming")
            return
        }
        val formattedData = tables.createTableFromList(updatedSupermercado.getListOfStringFromData())
        println(formattedData)
        println("Servicio de Streaming actualizado con éxito")
    }

    private fun deleteSupermercado() {
        val supermercado = SupermercadoService.getInstance().safeGetAll()
        if (supermercado.isEmpty()) {
            println("No hay servicios de streaming")
            return

        }
        supermercado.forEachIndexed { index, it -> println("${index + 1}. ${it.getNombre()}") }
        println("Selecciona el servicio de streaming que deseas eliminar:")
        val option = readln().toInt()
        if (option > supermercado.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSupermercado = supermercado[option - 1]
        if (selectedSupermercado.getSucursales().isNotEmpty()) {
            println("No se puede eliminar el servicio de streaming porque tiene series asociadas")
            return
        }
        val id = selectedSupermercado.getRuc()
        SupermercadoService.getInstance().remove(id)
        println("Servicio de Streaming eliminado con éxito")
    }
}