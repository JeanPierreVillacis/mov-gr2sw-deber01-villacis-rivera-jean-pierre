package models

import java.time.LocalDate

class Sucursal (
    private val id: String,
    private val ciudad:String,
    private val direccion: String,
    private val servicioTecnico: Boolean,
    private val fechaApertura: LocalDate,
    private val supermercado: Supermercado,
    ){
constructor(): this("","","",false, LocalDate.now(), Supermercado())

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
    fun getFechaApertura(): LocalDate{
        return fechaApertura
    }

    fun getSupermercado(): Supermercado {
        return supermercado
    }

    override fun toString(): String {
        return ""
    }

}