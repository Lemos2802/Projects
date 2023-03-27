package BussinessLayer.SubCarro;

public class SC extends Carro{

    public SC() {
    }

    public SC(double downforce, double fiabilidade, int estadoPneus, String marca, String modelo, int carID, int potenciaICE, int cilindrada, String categoria, TipoPneus tipoPneus, ModoMotor modoMotor) {
        super(downforce, fiabilidade, estadoPneus, marca, modelo, carID, potenciaICE, cilindrada, categoria, tipoPneus, modoMotor);
    }

    public SC(Carro carro) {
        super(carro);
    }

    public boolean verificaCilindrada(){
        return(this.getCilindrada() == 2500);
    }
 
}
