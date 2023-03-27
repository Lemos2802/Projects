package BussinessLayer.SubCarro;

public class GTH extends GT{
    private int potenciaE;

    

    public GTH(double downforce, double fiabilidade, int estadoPneus, String marca, String modelo, int carID, int potenciaICE, int cilindrada, String categoria, TipoPneus tipoPneus, ModoMotor modoMotor, double taxaDeterioracao, int potenciaE) {
        super(downforce, fiabilidade, estadoPneus, marca, modelo, carID, potenciaICE, cilindrada, categoria, tipoPneus,modoMotor,taxaDeterioracao);
        this.potenciaE = potenciaE;
    }

    public GTH(GT carro,int potenciaE) {
        super(carro, potenciaE);
        this.potenciaE = potenciaE;
    }

    public int getPotenciaE() {
        return this.potenciaE;
    }

    public void setPotenciaE(int potenciaE) {
        this.potenciaE = potenciaE;
    }


    
}
