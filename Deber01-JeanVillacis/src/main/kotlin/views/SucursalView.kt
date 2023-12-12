package views

import java.time.LocalDate
import datos.SucursalesDatos
import models.Sucursal
import services.SupermercadoService
import services.SucursalService

class SucursalView {

    val tables = ConsoleTable()

    fun selectSucursalMenu(parent: MainView) {
        var goBack = false
        do {
            println("Seleccione una opción:")
            println("1. Listar series")
            println("2. Crear serie")
            println("3. Actualizar serie")
            println("4. Eliminar serie")
            println("5. Volver atrás")

            val option = readln().toInt()
            when (option) {
                1 -> listAllSucursal()
                2 -> createSucursal()
                3 -> updateSucursal()
                4 -> deleteSucursal()
                5 -> {
                    goBack = true
                    parent.init()
                }
                else -> println("Opción no válida")
            }
        } while (!goBack)
    }

    fun listAllSucursal() {
        val sucursales :List<Sucursal> = SucursalService.getInstace().safeGetAll()

        if (sucursales.isEmpty()) {
            println("No hay series")
        } else {
            sucursales.forEach { println(tables.createTableFromList(it.getListOfStringFromData())) }
        }
    }

    fun createSucursal() {
        println("Ingrese el título de la serie:")
        val ciudad = readln()
        println("Ingrese el género de la serie:")
        val direccion = readln()
        println("¿La serie ya está finalizada? (s/n)")
        val servicioTecnico = readln().lowercase() == "s"
        println("Ingrese el número de temporadas de la serie:")
        val numeroEmpleados= readln().toInt()
        println("Ingrese la fecha de emision de la serie (formato: yyyy-MM-dd):")
        val fechaApertura = LocalDate.parse(readln())
        println("Selecciona el servicio de streaming de la serie:")
        val supermercado = SupermercadoService.getInstance().safeGetAll()
        supermercado.forEachIndexed { index, supermercado ->
            println("${index + 1}. ${supermercado.getNombre()}")
        }
        val supermercadoIndex = readln().toInt() - 1
        val supermercados = supermercado[supermercadoIndex]

        val sucursal = SucursalesDatos(
            ciudad = ciudad,
            direccion = direccion,
            servicioTecnico = servicioTecnico,
            numeroEmpleados = numeroEmpleados,
            fechaApertura = fechaApertura,
            supermercado = supermercados,
        )

        val createdSucursal = SucursalService.getInstace().create(sucursal)
        supermercados.agregarSucursales(createdSucursal)
      SupermercadoService.getInstance().update(supermercados)
        val formattedData = tables.createTableFromList(createdSucursal.getListOfStringFromData())
        println(formattedData)
        println("Serie creada correctamente")
    }

    fun updateSucursal() {
        val sucursal = SucursalService.getInstace().safeGetAll()
        if (sucursal.isEmpty()) {
            println("No hay series")
            return
        }
        sucursal.forEachIndexed { index, it -> println("${index + 1}. ${it.getDireccion()}") }
        println("Selecciona la serie que deseas actualizar:")
        val option = readln().toInt()
        if (option > sucursal.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSucursal = sucursal[option - 1]

        println(tables.createTableFromList(selectedSucursal.getListOfStringFromData()))

        println("Ingrese el título de la serie:")
        val ciudad = readln()
        println("Ingrese el género de la serie:")
        val direccion = readln()
        println("¿La serie ya está finalizada? (s/n)")
        val servicioTecnico = readln().lowercase() == "s"
        println("Ingrese el número de temporadas de la serie:")
        val numeroEmpleados = readln().toInt()
        println("Ingrese la fecha de emision de la serie (formato: yyyy-MM-dd):")
        val fechaApertura = LocalDate.parse(readln())
        println("Selecciona el servicio de streaming de la serie:")
        val supermercadoServices = SupermercadoService.getInstance().safeGetAll()
        supermercadoServices.forEachIndexed { index, supermercadoService ->
            println("${index + 1}. ${supermercadoService.getNombre()}")
        }
        val supermercadoIndex = readln().toInt()

        if (supermercadoIndex > supermercadoServices.size || supermercadoIndex < 1) {
            println("Opción no válida")
            return
        }

        val supermercado = supermercadoServices[supermercadoIndex - 1]

        val updatedSucursal = Sucursal(
            id = selectedSucursal.getId(),
            ciudad = ciudad,
            direccion= direccion,
            servicioTecnico = servicioTecnico,
            numeroEmpleados = numeroEmpleados,
            fechaApertura = fechaApertura,
            supermercado = supermercado,
        )

        val savedSucursal = SucursalService.getInstace().update(updatedSucursal)
        if (savedSucursal == null) {
            println("No se pudo actualizar la serie")
            return
        }

        supermercado.removerSucursales(selectedSucursal)
        supermercado.agregarSucursales(savedSucursal)
        SupermercadoService.getInstance().update(supermercado)

        val formattedData = tables.createTableFromList(updatedSucursal.getListOfStringFromData())
        println(formattedData)
        println("Serie actualizada correctamente")
    }

    fun deleteSucursal() {
        val sucursal = SucursalService.getInstace().safeGetAll()
        if (sucursal.isEmpty()) {
            println("No hay series")
            return

        }
        sucursal.forEachIndexed { index, it -> println("${index + 1}. ${it.getDireccion()}") }
        println("Selecciona la serie que deseas eliminar:")
        val option = readln().toInt()
        if (option > sucursal.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSucursal = sucursal[option - 1]
        val supermercado = selectedSucursal.getSupermercado()
        supermercado.removerSucursales(selectedSucursal)
        SupermercadoService.getInstance().update(supermercado)
        SucursalService.getInstace().remove(selectedSucursal.getId())
        println("Serie eliminada con éxito")
    }

}