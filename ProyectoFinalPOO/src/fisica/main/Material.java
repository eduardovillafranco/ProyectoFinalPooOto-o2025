package fisica.main;

/*
    Material físico que define cómo se comporta una bola o el taco
    al colisionar: restitución (rebote) y amortiguamiento (pérdida de energía).
 */
public enum Material {
    CAUCHO(0.96, 0.35),
    MADERA(2, 0.5);
    
    // Velocidad que tiene las bolas después del impacto
    public final double restitucion;

    // Valor que simula la resistencia al entorno, para que pueda frenar las bolas
    public final double amortiguamiento;
    
    Material(double r, double a){ 
        this.restitucion = r; 
        this.amortiguamiento = a; 
    }

    // Métodos getters
    public double getRestitucion(){
        return restitucion;
    }
    
    public double getDamping(){
        return amortiguamiento;
    }
}
