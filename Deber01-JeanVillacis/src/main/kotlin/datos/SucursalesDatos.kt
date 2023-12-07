package datos

import models.Supermercado
import java.time.LocalDate

class SucursalesDatos (
    val id: String,
    val ciudad:String,
    val direccion: String,
    val servicioTecnico: Boolean,
    val numeroEmpleados: Int,
    val fechaApertura: LocalDate,
    val supermercado: Supermercado,
){

}