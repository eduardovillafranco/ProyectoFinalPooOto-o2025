package fisica.main;

import fisica.mundo.Actualizable;
import fisica.mundo.AplicarFuerza;
import fisica.mundo.Config;

public abstract class Cuerpo implements Cloneable, Actualizable, AplicarFuerza{
    protected final String id;
    protected TipoDeObjeto tipo = TipoDeObjeto.DINAMICO;
    
    public double masa, inversoMasa;
    protected double restitucion = 0.8;
    protected double amortiguamiento = 0.02;
    
    public Vec2D posicion = Vec2D.crearVectorNulo(), velocidad = Vec2D.crearVectorNulo(), 
                 aceleracion = Vec2D.crearVectorNulo(), fuerza = Vec2D.crearVectorNulo();

    // Constructor del cuerpo
    protected Cuerpo(String id, double masa){
        this.id = id;
        this.masa = masa; 
        this.inversoMasa = 1.0/masa;
    }

    // Método abstracto el cual obliga a especificar a las subclases concretas a decir como se dibujan
    public abstract void draw(java.awt.Graphics2D g2);

    // Asigna las propiedades físicas del cuerpo según un material predefinido.
    public void setMaterial(Material m){
        this.restitucion = m.restitucion; 
        this.amortiguamiento = m.amortiguamiento;
    }

    // Cambia el tipo de objeto físico (dinámico/estático).
    // Si es estático, deja de responder a fuerzas e impulsos.
    public void setTipo(TipoDeObjeto t){
        this.tipo = t;
        if (t == TipoDeObjeto.ESTATICO){
            this.masa = Double.POSITIVE_INFINITY;
            this.inversoMasa = 0.0;
        }
    }

    // Suma una fuerza externa a las fuerzas acumuladas del cuerpo
    // para ser aplicada en el siguiente paso de actualización.
    @Override
    public void aplicarFuerza(Vec2D f){ 
        fuerza = fuerza.sumaVectores(f);
    }

    // Integra todo el movimiento del cuerpo en un intervalo de tiempo, no hace nada en caso de que el cuerpo sea estático
    @Override
    public void actualizar(double dt){
        if(tipo == TipoDeObjeto.ESTATICO) return;
        // Aceleración = (fuerza acumulada / masa) + gravedad global.
        aceleracion = fuerza.escalarXVector(inversoMasa).sumaVectores(Config.GRAVEDAD);
        // Actualizar velocidad con la aceleración calculada.
        velocidad = velocidad.sumaVectores(aceleracion.escalarXVector(dt));

        // Aplicar amortiguamiento para simular pérdida de energía.
        double factor = 1.0 - amortiguamiento * dt;
        if (factor < 0)
            factor = 0;

        velocidad = velocidad.escalarXVector(factor);

        // Limitar la velocidad máxima para evitar comportamientos inestables.
        double magnitudVelocidad = velocidad.magnitudVector();
        if (magnitudVelocidad > Config.MAX_VELOCIDAD) 
            velocidad = velocidad.escalarXVector(Config.MAX_VELOCIDAD/magnitudVelocidad);

        // Actualizar posición en función de la velocidad resultante.
        posicion = posicion.sumaVectores(velocidad.escalarXVector(dt));

        // Limpiar las fuerzas acumuladas para el siguiente ciclo.
        fuerza = Vec2D.crearVectorNulo();
    }

    // Aplica un impulso instantáneo al cuerpo (típico en colisiones). Solo afecta a cuerpos dinámicos.
    public void aplicarImpulso(Vec2D j){
        if (tipo == TipoDeObjeto.DINAMICO) 
            velocidad = velocidad.sumaVectores(j.escalarXVector(inversoMasa)); 
    }

    // Ajusta manualmente la posición del cuerpo (por ejemplo, correcciones tras una colisión).
    // Solo se aplica a cuerpos dinámicos.
    public void mover(Vec2D desplazamiento) {
        if (tipo == TipoDeObjeto.DINAMICO) {
            posicion = posicion.sumaVectores(desplazamiento);
        }
    }

    // Getters y Setters de los atributos de la clase
    public String getId(){
        return id;
    }

    public double getInvMasa(){
        return (tipo == TipoDeObjeto.ESTATICO) ? 0.0 : inversoMasa;
    }

    public double getRestitucion(){
        return restitucion;
    }

    public Vec2D getPosicion(){
        return posicion;
    }

    public Vec2D getVel(){
        return velocidad;
    }

    public void setVel(Vec2D v){
        this.velocidad = v;
    }

    public void setPosicion(Vec2D p){
        this.posicion = p;
    }

    @Override 
    public String toString(){
        return id + " posicion=" + posicion + " velocidad=" + velocidad;
    }    
}