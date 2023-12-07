package datos

import models.Sucursal

class SupermercadosDatos (
    val nombre: String,
    val telefono: Int,
    val vendeTecnologia: Boolean,
    val sucursales: MutableList<Sucursal>,
){
}