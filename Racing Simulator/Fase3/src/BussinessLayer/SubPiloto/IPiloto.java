package BussinessLayer.SubPiloto;

public interface IPiloto {
    public void registaPiloto(String nomePiloto, double cvs,double sva, String nacionalidade);
    public Piloto getPiloto(String nomePiloto);
    public boolean verificaPiloto(String nomePiloto);
    public boolean verificaCts(double cts);
    public boolean verificaSva(double sva);
}