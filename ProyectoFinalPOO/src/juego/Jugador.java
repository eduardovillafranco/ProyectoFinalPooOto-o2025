package juego;

import fisica.main.GrupoBola;

public class Jugador {
    private final String nombre;
    private GrupoBola grupo = GrupoBola.SIN_ASIGNAR;

    // Constructor para crear un jugador
    public Jugador(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public GrupoBola getGrupo(){
        return grupo;
    }

    // Asigna el grupo de bolas al jugador una vez que mete la primera bola
    public void setGrupo(GrupoBola grupo){
        this.grupo = grupo;
    }

    // Regresa VERDADERO si el jugador ya tiene grupo asignado
    public boolean tieneGrupoAsignado(){
        return grupo != GrupoBola.SIN_ASIGNAR;
    }
}
