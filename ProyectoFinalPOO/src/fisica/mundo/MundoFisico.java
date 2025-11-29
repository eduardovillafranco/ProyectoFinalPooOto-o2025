package fisica.mundo;

import fisica.colision_basica.*;
import fisica.main.Bola;
import fisica.main.Cuerpo;
import fisica.main.EventoTroneraListener;
import fisica.main.Mesa;
import fisica.main.Tronera;
import fisica.main.Vec2D;
import java.util.ArrayList;
import java.util.List;

/* Contiene y gestiona todos los cuerpos de la mesa de billar */
public class MundoFisico {
    private final List<Cuerpo> cuerpos = new ArrayList<>();
    private final Integrador integrador = (obj, dt) -> obj.actualizar(dt);
    private Mesa mesaBillar;
    private EventoTroneraListener eventoTroneraListener;

    // Establece la mesa principal del mundo físico y la registra como cuerpo.
    public void setMesaBillar(Mesa m){
        this.mesaBillar = m;
        agregarCuerpo(m);
    }
    
    // Agrega un cuerpo físico al mundo para que sea considerado en la simulación.
    public void agregarCuerpo(Cuerpo c){
        cuerpos.add(c);
    }

    // Devuelve una copia de la lista de cuerpos para evitar modificaciones externas directas.
    public List<Cuerpo> getCuerpos(){
        return new java.util.ArrayList<>(cuerpos);
    }

    public void setEventoTroneraListener(EventoTroneraListener listener){
        this.eventoTroneraListener = listener;
    }

    // Función detecta si alguna de las bolas entra en una tronera para quitarla del mundo, en caso de la bola blanca
    // reinicia a su posición inicial
    public void detectarTroneras(){
        List<Cuerpo> eliminar = new ArrayList<>();
        for (Cuerpo c : cuerpos){
            if (!(c instanceof Bola bola)) continue;
            for (Tronera t : mesaBillar.getAgujeros()){
                if (t.contieneBola(bola)){

                    // Se noticia al juego que la bola cayó en una tronera
                    if (eventoTroneraListener != null){
                        eventoTroneraListener.bolaEmbolsada(bola);
                    }

                    if ("blanca".equals(bola.getId())){
                        // Posición "nueva" de la bola blanca
                        Vec2D reinicioBolaBlanca = Vec2D.crearVector(260, 576/2.0);
                        bola.setPosicion(reinicioBolaBlanca);
                        bola.setVel(Vec2D.crearVectorNulo());
                    }else{
                        /// Se agrega a la lista las bolas que entraron en alguna tronera
                        eliminar.add(bola);
                    }
                    break;
                }
            }
        }
        // En caso de que la lista no este vacía, eliminará del mundo todas las bolas en la lista
        if (!eliminar.isEmpty()) cuerpos.removeAll(eliminar);
    }

    // Avanza la simulación física del mundo una cantidad de tiempo dada.
    public void actualizarSimulacion(double deltaTime){
        // 1) Integrar movimiento de todos los cuerpos según sus velocidades y fuerzas
        for (Cuerpo cuerpoSimulado : cuerpos) {
            integrador.integrar(cuerpoSimulado, deltaTime);
        }

        // 2) Detectar y resolver colisiones
        int cantidadCuerpos = cuerpos.size();

        for (int indiceCuerpoA = 0; indiceCuerpoA < cantidadCuerpos; indiceCuerpoA++) {
            Cuerpo cuerpoA = cuerpos.get(indiceCuerpoA);

            // Colisiones bola vs mesa (paredes)
            if (cuerpoA instanceof Bola bolaActual) {

                // Información de contacto entre la bola y la mesa
                ContactoColision contactoConMesa = new ContactoColision(bolaActual, mesaBillar);

                // Si hay colisión contra las bandas/llantas de la mesa, la resolvemos
                if (DetectorColisiones.bolaVsMesa(bolaActual, mesaBillar, contactoConMesa)) {
                    ResolverColisiones.resolver(contactoConMesa);
                }
            }

            for (int indiceCuerpoB = indiceCuerpoA + 1; indiceCuerpoB <  cantidadCuerpos; indiceCuerpoB++){
                Cuerpo cuerpoB = cuerpos.get(indiceCuerpoB);
                if (cuerpoA instanceof Bola bolaA && cuerpoB instanceof Bola bolaB){
                    ContactoColision contactoEntreBolas = new ContactoColision(bolaA, bolaB);
                    if(DetectorColisiones.bolaVsBola(bolaA, bolaB, contactoEntreBolas)){
                        ResolverColisiones.resolver(contactoEntreBolas);
                    }
                }
            }
        }
        // Si alguna bola llegará a entrar en alguna tronera esta función la quita de la mesa
        detectarTroneras();
    }
}