package servicios
import datos.SupermercadosDatos
import modelos.Supermercado
import java.io.File
class SupermercadoService {

    private val file:File =File("src/main/resources/supermercados.txt").also {
        if (!it.exists()) {
            it.createNewFile()
        }
    }

    companion object {
        private var instance: SupermercadoService? = null;

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: SupermercadoService().also { instance = it }
        }
    }
    private fun randomString(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { chars.random() }
            .joinToString("")
    }

    fun getAll(): List<Supermercado> {
        val lines = file.readLines()
        val supermercado = lines.map { it ->
            val supermercadoSplit = it.split(",")
            // get series from an array of ids
            val sucursales= supermercadoSplit[4].split(";")
                .map { SucursalService.getInstace().getOne(it) }

            val validarSucursal = sucursales.filterNotNull()
            return@map Supermercado(
                supermercadoSplit[0],
                supermercadoSplit[1],
                supermercadoSplit[2].toInt(),
                supermercadoSplit[3].toBoolean(),
                validarSucursal.toMutableList()
            )
        }
        return supermercado
    }

    fun safeGetAll(): List<Supermercado> {
        val lines = file.readLines()
        val supermercadoServices = lines.map { it ->
            val supermercadoSplit = it.split(",")
            // get series from an array of ids
            val sucursales= supermercadoSplit[4].split(";")
                .map { SucursalService.getInstace().getOneWithoutStreamingService(it) }

            val validarSucursal = sucursales.filterNotNull()
            return@map Supermercado(
                supermercadoSplit[0],
                supermercadoSplit[1],
                supermercadoSplit[2].toInt(),
                supermercadoSplit[3].toBoolean(),
                validarSucursal.toMutableList()
            )
        }
        return supermercadoServices
    }

    fun getOne(ruc:String): Supermercado?{
        val lines = file.readLines()
        val supermercadoServices = lines.find { it.split(",")[0] == ruc } ?: return null
            val supermercadoSplit = supermercadoServices.split(",")
            // get series from an array of ids
            val sucursales= supermercadoSplit[4].split(";")
                .map { SucursalService.getInstace().getOne(it) }
            val validarSucursal = sucursales.filterNotNull()

            return Supermercado(
                supermercadoSplit[0],
                supermercadoSplit[1],
                supermercadoSplit[2].toInt(),
                supermercadoSplit[3].toBoolean(),
                validarSucursal.toMutableList()
            )

    }

    fun getOneWithoutSupermercado(ruc: String): Supermercado? {
        val lines = file.readLines()
        val supermercadoServices = lines.find { it.split(",")[0] == ruc } ?: return null

        val supermercadoSplit = supermercadoServices.split(",")
        // get series from an array of ids
        val sucursales= supermercadoSplit[4].split(";")
            .map { SucursalService.getInstace().getOneWithoutStreamingService(it) }

        val validarSucursal = sucursales.filterNotNull()
        return Supermercado(
            supermercadoSplit[0],
            supermercadoSplit[1],
            supermercadoSplit[2].toInt(),
            supermercadoSplit[3].toBoolean(),
            validarSucursal.toMutableList()
        )
    }

    fun create(supermercado: SupermercadosDatos): Supermercado {
        val newSupermercado = Supermercado(
            randomString(),
            supermercado.nombre,
            supermercado.telefono,
            supermercado.vendeTecnologia,
            supermercado.sucursales.toMutableList()
        )
        file.appendText(newSupermercado.toString() + "\n")
        return newSupermercado
    }

    fun update(supermercado: Supermercado): Supermercado? {
        val lines = file.readLines()
        val supermercadoString = lines.find { it.split(",")[0] == supermercado.getRuc() } ?: return null

        val supermercadoSplit = supermercadoString.split(",")

        val newSupermercado = Supermercado(
            supermercadoSplit[0],
            supermercado.getNombre(),
            supermercado.getTelefono(),
            supermercado.getVendeTecnologia(),
            supermercado.getSucursales(),
        )

        this.remove(supermercado.getRuc())

        file.appendText(newSupermercado.toString() + "\n")

        return newSupermercado
    }
    fun remove(id: String) {
        file.readLines()
            .filter { it -> it.split(",")[0] != id }
            .joinToString("\n", postfix = "\n")
            .also { file.writeText(it) }
    }

}