package servicios
import datos.SucursalesDatos
import modelos.Sucursal
import modelos.Supermercado
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

    fun getOneWithoutStreamingService(id: String): Sucursal? {
        val lines = file.readLines()
        val sucursalString = lines.find { it.split(",")[0] == id } ?: return null

        val sucursalSplit = sucursalString.split(",")

        return Sucursal(
            sucursalSplit[0],
            sucursalSplit[1],
            sucursalSplit[2],
            sucursalSplit[3].toBoolean(),
            sucursalSplit[4].toInt(),
            LocalDate.parse(sucursalSplit[5]),
            Supermercado()
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

    fun update(sucursal: Sucursal): Sucursal? {
        val lines = file.readLines()
        val sucursalString = lines.find { it.split(",")[0] == sucursal.getId() } ?: return null

        val sucursalSplit = sucursalString.split(",")

        val newSucursal = Sucursal(
            id = sucursalSplit[0],
            ciudad = sucursal.getCiudad(),
            direccion = sucursal.getDireccion(),
            servicioTecnico = sucursal.getServicioTecnico(),
            numeroEmpleados = sucursal.getNumeroEmpleados(),
            fechaApertura = sucursal.getFechaApertura(),
            supermercado = sucursal.getSupermercado()
        )

        this.remove(sucursal.getId())

        file.appendText(newSucursal.toString() + "\n")

        return newSucursal
    }

    fun remove(id: String) {
        file.readLines()
            .filter { it.split(",")[0] != id }
            .joinToString("\n", postfix = "\n")
            .also { file.writeText(it) }
    }
}