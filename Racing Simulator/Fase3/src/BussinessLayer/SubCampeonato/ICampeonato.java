package BussinessLayer.SubCampeonato;

import java.util.ArrayList;

public interface ICampeonato {
    public void registaCircuito(String nomeCir, double distancia, int numeroVoltas, ArrayList<Segmentos> listaSegmentos);
    public void registaCampeonato(String nomeCamp, int nrMaxParticipantes, ArrayList<Circuito> listaCircuitosIntegrantes);
    public boolean verificaNomeCamp(String nomeCamp);
    public boolean verificaNomeCir(String nomeCir);
    public ArrayList<Circuito> getCircuitos();
    public ArrayList<Segmentos> assemble(ArrayList<Segmentos> lista,int n_chicanes, int ncurvas);
}