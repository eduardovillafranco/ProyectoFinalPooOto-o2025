package fisica.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bola extends Cuerpo {
    private final double radio;
    private TipoBola tipo;
    private Color color = Color.WHITE;

    // Atributos para el diseño de las bolas
    public BufferedImage uno, dos, tres, cuatro, cinco, seis, siete, ocho, 
                        nueve, diez, once, doce, trece, catorce, quince, blanca;

    // Método Constructor
    public Bola(String id, double masa, double radio, Vec2D posInicial){
        super(id, masa);
        this.radio = radio;
        this.posicion = posInicial.clone();
        this.tipo = setTipoBola(id);
        getImageBola();
    }

    // Devuelve el radio visual de la bola.
    public double getRadio(){
        return radio;
    }

    // Actualiza el color de la bola 
    public void setColor(Color c){
        this.color = c;
    }

    // Define el tipo de bola dependiendo del id de la bola
    private TipoBola setTipoBola(String id){
        if ("blanca".equals(id)) return TipoBola.BLANCA;
        if ("ocho".equals(id)) return TipoBola.OCHO;

        switch(id){
            case "uno":
            case "dos":
            case "tres":
            case "cuatro":
            case "cinco":
            case "seis":
            case "siete":
                return TipoBola.LISA;
            case "nueve":
            case "diez":
            case "once":
            case "doce":
            case "trece":
            case "catorce":
            case "quince":
                return TipoBola.RAYADA;
            default:
                throw new IllegalArgumentException("Id de bola no válido: " + id);
                
        }
    }

    public TipoBola getTipo(){
        return tipo;
    }

    //  Carga las imagenes que representan cada tipo de bola desde recursos
    private void getImageBola(){
        try{
            

            blanca = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_Blanca.png"));
            uno = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_1.png"));
            dos = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_2.png"));
            tres = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_3.png"));
            cuatro = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_4.png"));
            cinco = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_5.png"));
            seis = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_6.png"));
            siete = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_7.png"));
            ocho = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_8.png"));
            nueve = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_9.png"));
            diez = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_10.png"));
            once = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_11.png"));
            doce = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_12.png"));
            trece = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_13.png"));
            catorce = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_14.png"));
            quince = ImageIO.read(getClass().getResourceAsStream("/bolas/Bola_15.png"));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // Dibuja la bola en el contexto gráfico
    @Override
    public void draw(Graphics2D g){
        BufferedImage image = null;

        switch (id) {
            case "blanca":
                image = blanca;
                break;
            case "uno":
                image = uno;
                break;
            case "dos":
                image = dos;
                break;
            case "tres":
                image = tres;
                break;
            case "cuatro":
                image = cuatro;
                break;
            case "cinco":
                image = cinco;
                break;
            case "seis":
                image = seis;
                break;
            case "siete":
                image = siete;
                break;
            case "ocho":
                image = ocho;
                break;
            case "nueve":
                image = nueve;
                break;
            case "diez":
                image = diez;
                break;
            case "once":
                image = once;
                break;
            case "doce":
                image = doce;
                break;
            case "trece":
                image = trece;
                break;
            case "catorce":
                image = catorce;
                break;
            case "quince":
                image = quince;
                break;
            default:
                int d = (int)Math.round(radio * 2);
                g.setColor(color);
                g.fillOval((int)(posicion.x - radio), (int)(posicion.y - radio), d, d);
                g.setColor(Color.BLACK);
                g.drawOval((int)Math.round(posicion.x - radio), (int)Math.round(posicion.y - radio), d, d);
                return;
        }
        if (image != null) {
            int d = (int)Math.round(radio * 2);
            int x = (int)Math.round(posicion.x - radio);
            int y = (int)Math.round(posicion.y - radio);
            g.drawImage(image, x, y, d, d, null);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, d, d);
        }else {
            // Si no se cargó la textura esperada, usa fallback
            int d = (int)Math.round(radio * 2);
            g.setColor(color);
            g.fillOval((int)(posicion.x - radio), (int)(posicion.y - radio), d, d);
            g.setColor(Color.BLACK);
            g.drawOval((int)Math.round(posicion.x - radio), (int)Math.round(posicion.y - radio), d, d);
        }    }
}