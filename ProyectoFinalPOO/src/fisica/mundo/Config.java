package fisica.mundo;

import fisica.main.Vec2D;

/*
    Clase de configuración global de la simulación física.
 */
public class Config {
    // Evitamos crear instancias de la clase con un constructor privado
    private Config(){

    }
    // Controla qué tan “rápido” avanza la física entre cada actualización.
    public static final double DELTA_TIME = 1.0/60.0;

    // Umbral de velocidad por debajo del cual un cuerpo podría considerarse “en reposo”.
    public static final double VELOCIDAD_REPOSO = 4.0;
    
    // Sirve para evitar inestabilidades numéricas o movimientos irreales.
    public static final double MAX_VELOCIDAD = 800.0;

    // Porcentaje de corrección de penetración aplicado al separar cuerpos tras una colisión.
    public static final double PORCENTAJE_CORRECCION_PENETRACION = 0.8;

    // Tolerancia mínima de penetración permitida antes de aplicar corrección.
    // Evita microcorrecciones constantes por errores numéricos
    public static final double TOLERANCIA_PENETRACION = 0.01;

    // Vector de gravedad global del mundo.
    public static final Vec2D GRAVEDAD = Vec2D.crearVectorNulo();
}
