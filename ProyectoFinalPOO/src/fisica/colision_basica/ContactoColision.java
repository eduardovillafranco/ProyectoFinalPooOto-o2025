package fisica.colision_basica;

import fisica.main.Cuerpo;
import fisica.main.Vec2D;

public class ContactoColision{
    public final Cuerpo A, B;
    // Vector unitario normal, utilizado para calcular los impulsos
    public Vec2D vectorNormal = Vec2D.crearVectorNulo();
    // Profundidad entre dos cuerpos, se usa para separar los cuerpos después de la colisión
    public double traslape = 0.0;
    // Proyección la cuál permite saber si los objetos se acercan o se separan
    public double proyVelocidadRelEnNormal = 0.0;

    // Crea un contacto entre dos cuerpos
    public ContactoColision(Cuerpo A, Cuerpo B){
        this.A = A;
        this.B = B;
    }
}