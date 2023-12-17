package vistas

import java.time.LocalDate
import datos.SucursalesDatos
import modelos.Sucursal
import servicios.SupermercadoService
import servicios.SucursalService

class SucursalView {

    val tables = ConsoleTable()

    fun selectSucursalMenu(parent: MainView) {
        var goBack = false
        do {
            println("Seleccione una opción:")
            println("1. Listar sucursales")
            println("2. Crear sucursal")
            println("3. Actualizar sucursal")
            println("4. Eliminar sucursal")
            println("5. Regresar")

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
            println("No hay sucursales")
        } else {
            sucursales.forEach { println(tables.createTableFromList(it.getListOfStringFromData())) }
        }
    }

    fun createSucursal() {
        println("Ingrese la ciudad de la sucursal:")
        val ciudad = readln()
        println("Ingrese la direccion de la sucursal:")
        val direccion = readln()
        println("¿La cuenta con servicio tecnico ? (s/n)")
        val servicioTecnico = readln().toBoolean()
        println("Ingrese el número de empleados de la sucursal:")
        val numeroEmpleados= readln().toInt()
        println("Ingrese la fecha de apertura de la sucursal (formato: yyyy-MM-dd):")
        val fechaApertura = LocalDate.parse(readln())
        println("Selecciona la linea de supermercados a la que pertenece la sucursal:")
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
        println("Sucursal creada correctamente")
    }

    fun updateSucursal() {
        val sucursal = SucursalService.getInstace().safeGetAll()
        if (sucursal.isEmpty()) {
            println("No hay series")
            return
        }
        sucursal.forEachIndexed { index, it -> println("${index + 1}. ${it.getDireccion()}") }
        println("Selecciona la sucursal que deseas actualizar:")
        val option = readln().toInt()
        if (option > sucursal.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSucursal = sucursal[option - 1]

        println(tables.createTableFromList(selectedSucursal.getListOfStringFromData()))

        println("Ingrese la nueva ciudad de la sucursal:")
        val ciudad = readln()
        println("Ingrese la nueva direccion de la sucursal:")
        val direccion = readln()
        println("¿La sucursal cuenta con servicio tecnico? (s/n)")
        val servicioTecnico = readln().toBoolean()
        println("Ingrese el nuevo número de empleados de la sucursal:")
        val numeroEmpleados = readln().toInt()
        println("Ingrese la nueva fecha de emision de la serie (formato: yyyy-MM-dd):")
        val fechaApertura = LocalDate.parse(readln())
        println("Selecciona la nueva linea de supermercados a la que pertenece la sucursal:")
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
            println("No se pudo actualizar la sucursal")
            return
        }

        supermercado.removerSucursales(selectedSucursal)
        supermercado.agregarSucursales(savedSucursal)
        SupermercadoService.getInstance().update(supermercado)

        val formattedData = tables.createTableFromList(updatedSucursal.getListOfStringFromData())
        println(formattedData)
        println("Sucursal actualizada correctamente")
    }

    fun deleteSucursal() {
        val sucursal = SucursalService.getInstace().safeGetAll()
        if (sucursal.isEmpty()) {
            println("No hay sucursales")
            return

        }
        sucursal.forEachIndexed { index, it -> println("${index + 1}. ${it.getDireccion()}") }
        println("Selecciona la sucursal que deseas eliminar:")
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
        println("Sucursal eliminada con éxito")
    }

}