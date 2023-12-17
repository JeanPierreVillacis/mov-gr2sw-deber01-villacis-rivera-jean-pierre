package datos

import modelos.Sucursal

class SupermercadosDatos (
    val nombre: String,
    val telefono: Int,
    val vendeTecnologia: Boolean,
    val sucursales: List<Sucursal>,
){
}