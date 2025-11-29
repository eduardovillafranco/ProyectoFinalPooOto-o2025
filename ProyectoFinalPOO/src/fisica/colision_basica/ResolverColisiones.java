package fisica.colision_basica;

import fisica.main.*;
import fisica.mundo.*;

public class ResolverColisiones {
    /* 
        Resuelve una colisión usando los datos almacenados en contactoColision.
        Corrige el traslape separando los cuerpos
        Aplica un impulso para cambiar sus velocidades 
    */
    public static void resolver(ContactoColision m){
        Cuerpo A = m.A, B = m.B; // Cuerpos involucrados en la colisión
        
        // Evita resolver si se separan
        if (m.proyVelocidadRelEnNormal > 0) return;

        // Inversos de masa (si un cuerpo es estático, su inverso de masa será 0).
        double inversoMasaA = A.getInvMasa();
        double inversoMasaB = B.getInvMasa();
        // Si la suma es cero, no hay masa móvil que ajustar (colisión inválida o solo cuerpos estáticos).
        double denom = inversoMasaA + inversoMasaB;
        if (denom <= 0) return;

        // Coeficiente de restitución efectivo
        // Se toma el mínimo de ambos para evitar rebotes exagerados.
        double e = Math.min(A.getRestitucion(), B.getRestitucion());
        //Impulso Escalar
        double impulsoEscalar = -(1.0 + e) * m.proyVelocidadRelEnNormal / denom;
        // Vector de impulso
        Vec2D impulso = m.vectorNormal.escalarXVector(impulsoEscalar);

        A.aplicarImpulso(impulso.escalarXVector(-1));
        B.aplicarImpulso(impulso);

        // Corrección posicional, para que las bolas se mantengan dentro de la mesa
        double magnitudCoreccion = Math.max(m.traslape - Config.TOLERANCIA_PENETRACION, 0.0)
                         * Config.PORCENTAJE_CORRECCION_PENETRACION / denom;
        // Vector de corrección en dirección de la normal.
        Vec2D correction = m.vectorNormal.escalarXVector(magnitudCoreccion);
        
        // Ajuste de posiciones proporcional a sus masas
        A.mover(correction.escalarXVector(-1));
        B.mover(correction);
    }
}