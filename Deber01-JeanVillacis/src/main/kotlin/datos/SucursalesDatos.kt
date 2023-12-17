package datos

import modelos.Supermercado
import java.time.LocalDate

class SucursalesDatos (
    val ciudad:String,
    val direccion: String,
    val servicioTecnico: Boolean,
    val numeroEmpleados: Int,
    val fechaApertura: LocalDate,
    val supermercado: Supermercado,
){

}