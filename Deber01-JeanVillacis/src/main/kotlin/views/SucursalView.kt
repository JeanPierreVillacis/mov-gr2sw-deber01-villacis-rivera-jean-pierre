package views

import java.time.LocalDate
import datos.SucursalesDatos
import models.Sucursal
import services.SupermercadoService
import services.SucursalService

class SucursalView {

    val tables = ConsoleTable()

    fun selectSeriesMenu(parent: MainView) {
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
        val supermercado = supermercado[supermercadoIndex]

        val sucursal = SucursalesDatos(
            ciudad = ciudad,
            direccion = direccion,
            servicioTecnico = servicioTecnico,
            numeroEmpleados = numeroEmpleados,
            fechaApertura = fechaApertura,
            supermercado = supermercado,
        )

        val createdSeries = SeriesService.getInstance().create(series)
        supermercado.addSeries(createdSeries)
        StreamingServiceService.getInstance().update(supermercado)
        val formattedData = tables.createTableFromList(createdSeries.getListOfStringFromData())
        println(formattedData)
        println("Serie creada correctamente")
    }

    fun updateSucursal() {
        val series = SeriesService.getInstance().safeGetAll()
        if (series.isEmpty()) {
            println("No hay series")
            return
        }
        series.forEachIndexed { index, it -> println("${index + 1}. ${it.getTitle()}") }
        println("Selecciona la serie que deseas actualizar:")
        val option = readln().toInt()
        if (option > series.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSeries = series[option - 1]

        println(tables.createTableFromList(selectedSeries.getListOfStringFromData()))

        println("Ingrese el título de la serie:")
        val title = readln()
        println("Ingrese el género de la serie:")
        val genre = readln()
        println("¿La serie ya está finalizada? (s/n)")
        val isFinished = readln().lowercase() == "s"
        println("Ingrese el número de temporadas de la serie:")
        val seasons = readln().toInt()
        println("Ingrese la fecha de emision de la serie (formato: yyyy-MM-dd):")
        val emissionDate = LocalDate.parse(readln())
        println("Selecciona el servicio de streaming de la serie:")
        val streamingServices = StreamingServiceService.getInstance().safeGetAll()
        streamingServices.forEachIndexed { index, streamingService ->
            println("${index + 1}. ${streamingService.getName()}")
        }
        val streamingServiceIndex = readln().toInt()

        if (streamingServiceIndex > streamingServices.size || streamingServiceIndex < 1) {
            println("Opción no válida")
            return
        }

        val streamingService = streamingServices[streamingServiceIndex - 1]

        val updatedSeries = Serie(
            id = selectedSeries.getId(),
            title = title,
            genre = genre,
            isFinished = isFinished,
            seasons = seasons,
            emissionDate = emissionDate,
            streamingService = streamingService,
        )

        val savedSerie = SeriesService.getInstance().update(updatedSeries)

        if (savedSerie == null) {
            println("No se pudo actualizar la serie")
            return
        }

        streamingService.removeSeries(selectedSeries)
        streamingService.addSeries(savedSerie)
        StreamingServiceService.getInstance().update(streamingService)

        val formattedData = tables.createTableFromList(updatedSeries.getListOfStringFromData())
        println(formattedData)
        println("Serie actualizada correctamente")
    }

    fun deleteSucursal() {
        val series = SeriesService.getInstance().safeGetAll()
        if (series.isEmpty()) {
            println("No hay series")
            return
        }
        series.forEachIndexed { index, it -> println("${index + 1}. ${it.getTitle()}") }
        println("Selecciona la serie que deseas eliminar:")
        val option = readln().toInt()
        if (option > series.size || option < 1) {
            println("Opción no válida")
            return
        }
        val selectedSeries = series[option - 1]
        val streamingService = selectedSeries.getStreamingService()
        streamingService.removeSeries(selectedSeries)
        StreamingServiceService.getInstance().update(streamingService)
        SeriesService.getInstance().remove(selectedSeries.getId())
        println("Serie eliminada con éxito")
    }
}