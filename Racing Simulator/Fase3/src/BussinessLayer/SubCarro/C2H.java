package BussinessLayer.SubCarro;

public class C2H extends C2{
    private int potenciaE;

    public C2H(int potenciaE) {
        this.potenciaE = potenciaE;
    }

    public C2H(double downforce, double fiabilidade, int estadoPneus, String marca, String modelo, int carID, int potenciaICE, int cilindrada, String categoria, TipoPneus tipoPneus, ModoMotor modoMotor, int potenciaE) {
        super(downforce, fiabilidade, estadoPneus, marca, modelo, carID, potenciaICE, cilindrada, categoria, tipoPneus, modoMotor);
        this.potenciaE = potenciaE;
    }

    public C2H(Carro carro, int potenciaE) {
        super(carro);
        this.potenciaE = potenciaE;
    }

    public int getPotenciaE() {
        return this.potenciaE;
    }

    public void setPotenciaE(int potenciaE) {
        this.potenciaE = potenciaE;
    }



 
}
