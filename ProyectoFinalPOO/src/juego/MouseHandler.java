package juego;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {
    public int mouseX, mouseY;
    public boolean isPressed = false;
    public boolean justPressed = false;
    public boolean justReleased = false;

    // Actualiza la posición del mouse cuando se mueve sin arrastrar.
    @Override 
    public void mouseMoved(MouseEvent e) { 
        mouseX = e.getX();
        mouseY = e.getY();
    }
    
    // Actualiza la posición del mouse mientras se arrastra (botón presionado).
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    // 
    @Override
    public void mousePressed(MouseEvent e) {
        isPressed = true;
        justPressed = true;
        justReleased = false;
        mouseMoved(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
        justReleased = true;
        justPressed = false;
        mouseMoved(e);
    }

    // Métods no usados para efectos del juego, pero obligados a sobrescribirlos por la interface.
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // Llamar al final de cada update() para limpiar los “just”
    public void consumeEdgeTriggers(){
        justPressed = false;
        justReleased = false;
    }
}