package juego;

import fisica.main.Bola;
import fisica.main.GrupoBola;
import fisica.main.TipoBola;
import java.util.ArrayList;
import java.util.List;

public class PartidaBillar {
    private final Jugador j1;
    private final Jugador j2;
    private Jugador jugadorActual;

    private int lisasRestantes = 7;
    private int rayadasRestantes = 7;

    private boolean partidaTerminada;

    private final List<Bola> bolasEnTroneraEnTurno = new ArrayList<>();
    private boolean blancaEnEsteTurno = false;

    // Crea la partida del billar, creando a los dos jugadores y estableciendo la partida por default
    public PartidaBillar(){
        this.j1 = new Jugador("Jugador 1");
        this.j2 = new Jugador("Jugador 2");
        reiniciar();
    }

    /*
        Reinicia completamente la partida:
        - limpia grupos, bolas restantes y turno actual.
    */
    public void reiniciar(){
        lisasRestantes = 7;
        rayadasRestantes = 7;

        j1.setGrupo(GrupoBola.SIN_ASIGNAR);
        j2.setGrupo(GrupoBola.SIN_ASIGNAR);

        jugadorActual = j1;
        partidaTerminada = false;

        bolasEnTroneraEnTurno.clear();
        blancaEnEsteTurno = false;
    }
    
    // Getters y Setters
    public Jugador getJugador1() {
        return j1;
    }

    public Jugador getJugador2() {
        return j2;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    public int getLisasRestantes() {
        return lisasRestantes;
    }

    public int getRayadasRestantes() {
        return rayadasRestantes;
    }

    public int getBolasRestantes(Jugador j) {
        if (!j.tieneGrupoAsignado()) {
            // Antes de asignar tipo, asumimos 7 como máximo de bolas por jugador
            return 7;
        }
        return (j.getGrupo() == GrupoBola.LISAS) ? lisasRestantes : rayadasRestantes;
    }

    // Metodos para GamePanel

    // Detecta cada vez que alguna bola entra en una tronera y actualiza el estado de la partida
    public void registrarBolaEnTronera(Bola bola){
        if (partidaTerminada) return;

        if ("blanca".equals(bola.getId())) blancaEnEsteTurno = true;
        else bolasEnTroneraEnTurno.add(bola);
    }

    /*
        Lógica que se ejecuta al finalizar un tiro:
        Decide si el jugador conserva el turno, si cambia de jugador
        o si la partida termina (por meter la 8 antes o después de tiempo).
    */
    public Jugador finDeTiro(){
        if (partidaTerminada) return null;

        boolean hayBolas = !bolasEnTroneraEnTurno.isEmpty();
        boolean metioOcho = false;
        boolean metioLisa = false;
        boolean metioRayada = false;

        for (Bola b : bolasEnTroneraEnTurno){
            TipoBola tipo = b.getTipo();
            switch (tipo){
                case OCHO:
                    metioOcho = true;
                    break;
                case LISA:
                    metioLisa = true;
                    break;
                case RAYADA: 
                    metioRayada = true;
                    break;
                default:
                    break;
            }
        }

        // Asignacion inicial del grupo de los jugadores
        if(!j1.tieneGrupoAsignado() && (metioLisa || metioRayada)){
            GrupoBola grupo = metioLisa ? GrupoBola.LISAS : GrupoBola.RAYADAS;
            setGrupos(jugadorActual, grupo);
        }

        // Contar bolas y detectar si fueron de su grupo o del contrario
        boolean metioGrupo = false;
        boolean metioContrario = false;

        for (Bola b : bolasEnTroneraEnTurno){
            TipoBola tipo = b.getTipo();
            switch (tipo) {
                case LISA:
                    descontarBolaDeGrupo(GrupoBola.LISAS);
                    if (jugadorActual.getGrupo() == GrupoBola.LISAS) metioGrupo = true;
                    else if (jugadorActual.getGrupo() == GrupoBola.RAYADAS) metioContrario = true;
                    break;
                case RAYADA:
                    descontarBolaDeGrupo(GrupoBola.RAYADAS);
                    if (jugadorActual.getGrupo() == GrupoBola.RAYADAS) metioGrupo = true;
                    else if (jugadorActual.getGrupo() == GrupoBola.LISAS) metioContrario = true;
                case OCHO:
                    break;
                default:
                    break;
            }
        }

        if (metioOcho){
            Jugador ganador;

            if(!jugadorActual.tieneGrupoAsignado()){
                ganador = obtenerOponente(jugadorActual);
            }else{
                int restantesGrupo = (jugadorActual.getGrupo() == GrupoBola.LISAS) ? lisasRestantes : rayadasRestantes;
                if(restantesGrupo == 0 && !blancaEnEsteTurno) ganador = jugadorActual;
                else ganador = obtenerOponente(jugadorActual);
            }

            partidaTerminada = true;
            limpiarEstado();
            return ganador;
        }

        // Cambios de turno
        boolean cambiarTurno;

         if (!jugadorActual.tieneGrupoAsignado()) {
            // Antes de que se definan lisas/rayadas
            if (!hayBolas || blancaEnEsteTurno) {
                cambiarTurno = true;   // No metió nada o metió blanca
            } else {
                cambiarTurno = false;  // Metió alguna bola de número -> sigue
            }
        } else {
            // Después de asignar lisas/rayadas
            if (blancaEnEsteTurno) {
                cambiarTurno = true;
            } else if (metioContrario) {
                cambiarTurno = true;
            } else if (metioGrupo) {
                cambiarTurno = false;
            } else if (!hayBolas) {
                cambiarTurno = true;
            } else {
                cambiarTurno = true;   // caso raro → forzamos cambio
            }
        }

        if (cambiarTurno) {
            jugadorActual = obtenerOponente(jugadorActual);
        }

        limpiarEstado();
        return null;    // La partida continúa
    }

    // Limpia las variales internas para preparar el siguiente tiro
    private void limpiarEstado() {
        bolasEnTroneraEnTurno.clear();
        blancaEnEsteTurno = false;
    }

    // Asigna el grupo(lisas o rayadas) a un jugador, despues de meter su priemra bola
    private void setGrupos(Jugador jugador, GrupoBola grupo) {
        jugador.setGrupo(grupo);
        Jugador otro = obtenerOponente(jugador);
        otro.setGrupo(grupo == GrupoBola.LISAS ? GrupoBola.RAYADAS : GrupoBola.LISAS);
    }

    // Devueleve el oponente del jugador indicado
    private Jugador obtenerOponente(Jugador jugador) {
        return (jugador == j1) ? j2 : j1;
    }

    // Resta una bola al grupo indicado en caso de haber metido una bola
    private void descontarBolaDeGrupo(GrupoBola grupo) {
        if (grupo == GrupoBola.LISAS && lisasRestantes > 0) {
            lisasRestantes--;
        } else if (grupo == GrupoBola.RAYADAS && rayadasRestantes > 0) {
            rayadasRestantes--;
        }
    }
}
