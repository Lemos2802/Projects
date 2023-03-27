package BussinessLayer.SubCarro;

import java.util.Random;

public class C1 extends Carro{


    public C1() {
    }

    public C1(double downforce, double fiabilidade, int estadoPneus, String marca, String modelo, int carID, int potenciaICE, int cilindrada, String categoria, TipoPneus tipoPneus, ModoMotor modoMotor) {
        super(downforce, fiabilidade, estadoPneus, marca, modelo, carID, potenciaICE, cilindrada, categoria, tipoPneus, modoMotor);
    }

    public C1(Carro carro) {
        super(carro);
    }

    // Função que verifica se o valor da fiabilidade de um carro é válido 
    public boolean verificaFiabilidade(){
        return (this.getFiabilidade() > 0.90 && this.getFiabilidade() < 0.95);
    }


}
