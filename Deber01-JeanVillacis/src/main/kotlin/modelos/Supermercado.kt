package modelos

class Supermercado(
    private val ruc:String,
    private val nombre: String,
    private val telefono: Int,
    private val vendeTecnologia: Boolean,
    private var sucursales: MutableList<Sucursal>,
){
    constructor():this("","",0,true, mutableListOf())
     public fun getRuc(): String {
        return ruc
    }
    fun getNombre(): String {
        return nombre
    }
     fun getTelefono(): Int {
        return telefono
    }
    fun getVendeTecnologia(): Boolean{
        return vendeTecnologia
    }
    fun getSucursales(): MutableList<Sucursal>{
        return sucursales
    }
    fun  agregarSucursales(sucursales: Sucursal){
        this.sucursales.add(sucursales)
    }
    fun removerSucursales(sucursales: Sucursal){
        this.sucursales = this.sucursales.filter { it.getId() != sucursales.getId() }.toMutableList()
    }

    public override fun toString(): String {
        val ids: String = sucursales.map { it.getId()}.joinToString(";")
       return "$ruc,$nombre,$telefono,$vendeTecnologia,$ids"
    }
    fun getListOfStringFromData(): List<String>{
        return listOf(
            "Nombre: $nombre",
            "Telefono: $telefono",
            "Vende Tecnologia: $vendeTecnologia",
            "Sucursales: ${sucursales.map { it.getCiudad() }}",
        )
    }
    }

