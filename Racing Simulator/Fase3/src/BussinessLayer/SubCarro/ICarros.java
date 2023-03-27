package BussinessLayer.SubCarro;

public interface ICarros {
    public void registaC1Hibrido(String marca, String modelo, double downforce, int potenciaICE, int PotenciaE);
    public void registaC1(String marca, String modelo, double downforce, int potenciaICE);
    public void registaC2Hibrido(String marca, String modelo, int cilindrada, double downforce, int potenciaICE, int PotenciaE);
    public void registaC2(String marca, String modelo, int cilindrada, double downforce, int potenciaICE);
    public void registaSC(String marca, String modelo, double downforce, int potenciaICE);
    public void registaGT(String marca, String modelo, int cilindrada, double downforce, int potenciaICE);
    public void registaGTHibrido(String marca, String modelo, int cilindrada, double downforce, int potenciaICE, int potenciaE);
    public boolean verificaPac(double downforce);
    public boolean verificaCilindrada(int option);
}
