package BussinessLayer.SubCarro;

public class C1H extends C1 {

    private int potenciaE;

    public C1H(int potenciaE) {
        this.potenciaE = potenciaE;
    }

    public C1H(double downforce, double fiabilidade, int estadoPneus, String marca, String modelo, int carID, int potenciaICE, int cilindrada, String categoria, TipoPneus tipoPneus, ModoMotor modoMotor, int potenciaE) {
        super(downforce, fiabilidade, estadoPneus, marca, modelo, carID, potenciaICE, cilindrada, categoria, tipoPneus, modoMotor);
        this.potenciaE = potenciaE;
    }

    public C1H(C1 carro, int potenciaE){
        super(carro);
        this.potenciaE = potenciaE;
    }

    public int getPotenciaE(){
        return this.potenciaE;
    }

    public void setPotenciaE(int potenciaE){
        this.potenciaE = potenciaE;
    }



    
    
 

    
}
