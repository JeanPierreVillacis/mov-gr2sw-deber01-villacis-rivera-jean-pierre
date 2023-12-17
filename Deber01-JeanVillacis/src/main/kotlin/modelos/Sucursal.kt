package modelos

import java.time.LocalDate

class Sucursal (
    private val id: String,
    private val ciudad:String,
    private val direccion: String,
    private val servicioTecnico: Boolean,
    private val numeroEmpleados: Int,
    private val fechaApertura: LocalDate,
    private val supermercado: Supermercado,
    ){
    constructor(): this("","","",false,0, LocalDate.now(), Supermercado())

    public fun getId(): String{
        return id
    }
    fun getCiudad():String{
        return ciudad
    }
    fun  getDireccion():String{
        return direccion
    }
    fun  getServicioTecnico():Boolean{
        return servicioTecnico
    }
    fun  getNumeroEmpleados(): Int {
        return numeroEmpleados
    }

    fun getFechaApertura(): LocalDate{
        return fechaApertura
    }

    fun getSupermercado(): Supermercado {
        return supermercado
    }

    override fun toString(): String {
        return "$id,$ciudad,$direccion,$servicioTecnico,$numeroEmpleados,$fechaApertura,${supermercado.getRuc()}"
    }
    fun getListOfStringFromData(): List<String> {
        return listOf(
            "Ciudad: $ciudad",
            "Direccion: $direccion",
            "Servicio Tecnico: $servicioTecnico",
            "Numero de Empleados: $numeroEmpleados",
            "Fecha Apertura: $fechaApertura",
            "Supermercado: ${supermercado.getNombre()}",
        )
    }

}