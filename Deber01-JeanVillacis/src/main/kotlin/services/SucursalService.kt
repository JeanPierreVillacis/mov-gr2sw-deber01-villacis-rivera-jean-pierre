package services
import datos.SucursalesDatos
import models.Sucursal
import java.io.File
import java.time.LocalDate

class SucursalService {

private val file: File = File("src/main/resources/sucursales.txt").also {
    if(!it.exists()){
        it.createNewFile()
    }
}
companion object{
    private var instace:SucursalService? =null;
    fun getInstace() = instace ?: synchronized(this){
        instace ?: SucursalService().also { instace = it }
    }
}
    private fun randomString(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { chars.random() }
            .joinToString("")
    }

    fun getAll(): List<Sucursal> {
        val lines = file.readLines()
        val sucursal = lines.map { it ->
            val sucursalSplit = it.split(",")
            val supermercado = SupermercadoService.getInstance().getOne(sucursalSplit[6]) ?: return@map null

            return@map Sucursal(

                sucursalSplit[0],
                sucursalSplit[1],
                sucursalSplit[2],
                sucursalSplit[3].toBoolean(),
                sucursalSplit[4].toInt(),
                LocalDate.parse( sucursalSplit[5]),
                supermercado
            )
        }
        return sucursal.filterNotNull()
    }

    fun safeGetAll(): List<Sucursal> {
        val lines = file.readLines()
        val sucursal = lines.map { it ->
            val sucursalSplit= it.split(",")
            val supermercado = SupermercadoService.getInstance().getOneWithoutSupermercado(sucursalSplit[6]) ?: return@map null

            return@map Sucursal(
                sucursalSplit[0],
                sucursalSplit[1],
                sucursalSplit[2],
                sucursalSplit[3].toBoolean(),
                sucursalSplit[4].toInt(),
                LocalDate.parse( sucursalSplit[5]),
                supermercado
            )
        }
        return sucursal.filterNotNull()
    }

    fun getOne(id: String): Sucursal? {
        val lines = file.readLines()
        val sucursalString = lines.find { it.split(",")[0] == id } ?: return null

        val sucursalSplit = sucursalString.split(",")
        val supermercado = SupermercadoService.getInstance().getOne(sucursalSplit[6]) ?: return null

        return Sucursal(
            sucursalSplit[0],
            sucursalSplit[1],
            sucursalSplit[2],
            sucursalSplit[3].toBoolean(),
            sucursalSplit[4].toInt(),
            LocalDate.parse( sucursalSplit[5]),
            supermercado
        )
    }

    fun create(sucursal: SucursalesDatos): Sucursal {
        val newSucursal = Sucursal(
            id = randomString(),
            ciudad = sucursal.ciudad,
            direccion = sucursal.direccion,
            servicioTecnico = sucursal.servicioTecnico,
            numeroEmpleados = sucursal.numeroEmpleados,
            fechaApertura = sucursal.fechaApertura,
            supermercado = sucursal.supermercado
        )
        file.appendText(newSucursal.toString() + "\n")
        return newSucursal
    }
}