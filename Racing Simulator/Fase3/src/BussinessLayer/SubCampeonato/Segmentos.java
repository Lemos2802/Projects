package BussinessLayer.SubCampeonato;


/**
 * Write a description of class Segmentos here.
 *
 */

public class Segmentos {

    public enum SegmentoEstrada{
        CURVA,
        RETA,
        CHICANE;
    }
    
    private SegmentoEstrada segmento;
    private int gdu;


    // --- Empty Constructor ---
    public Segmentos(){
        this.segmento = null;
        this.gdu = 0;
    }

    // --- Parameterized Constructor ---
    public Segmentos(SegmentoEstrada segmento, int gdu){
        this.segmento = segmento;
        this.gdu = gdu;
    }

    // --- Copy Constructor ---
    public Segmentos(Segmentos s){
        this.segmento = s.segmento;
        this.gdu = s.gdu;
    }

    // --- Getters & Setters ---
    public SegmentoEstrada getSegmento() {
        return segmento;
    }

    public void setSegmento(SegmentoEstrada segmento) {
        this.segmento = segmento;
    }

    public int getGdu() {
        return gdu;
    }
    public void setGdu(int gdu) {
        this.gdu = gdu;
    }
}
