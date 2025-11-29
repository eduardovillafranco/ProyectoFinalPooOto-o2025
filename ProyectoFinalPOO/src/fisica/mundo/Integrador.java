package fisica.mundo;

@FunctionalInterface
public interface Integrador {
    // Integra el estado de un objeto durante un intervalo de tiempo.
    void integrar(Actualizable obj, double dt);
}
