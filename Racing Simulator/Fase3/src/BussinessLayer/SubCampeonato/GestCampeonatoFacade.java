package BussinessLayer.SubCampeonato;


import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BussinessLayer.SubCampeonato.Segmentos.SegmentoEstrada;
import DataLayer.CampeonatoDAO;
import DataLayer.CircuitoDAO;

public class GestCampeonatoFacade implements ICampeonato{
    private Map<String, Campeonato> campeonatos;
    private Map<String, Circuito> circuitos;
    

    public GestCampeonatoFacade(){
        this.circuitos = CircuitoDAO.getInstance();
        this.campeonatos = CampeonatoDAO.getInstance();
    }

    public void registaCircuito(String nomeCir, double distancia, int numeroVoltas, ArrayList<Segmentos> listaSegmentos){
        Circuito c = new Circuito(nomeCir, distancia, numeroVoltas, 0, listaSegmentos); // TO-DO falta definir como vamos fazer com o tempoBox
        this.circuitos.put(c.getNomeCir(),c);
    }

    public void registaCampeonato(String nomeCamp, int nrMaxParticipantes, ArrayList<Circuito> listaCircuitosIntegrantes){
        Campeonato camp = new Campeonato(nomeCamp, nrMaxParticipantes, listaCircuitosIntegrantes);
        this.campeonatos.put(camp.getNomeCamp(), camp);
    }

    /**
    public Collection<Circuito> getCircuitos() {
        return this.circuitos.values();
    }

    public Campeonato getCampeonato(String nome){
        Campeonato c = new Campeonato();
        c = this.campeonatos.get(nome);
        ArrayList <Circuito> lCirc = new ArrayList<>();
        for(Circuito m : c.getLc().values()){
            lCirc.add(this.circuitos.get(m.getnomeCir()));
        }
        return  new Campeonato(nome,lCirc);
    }
    */

    // --- Métodos ---

    // Função para validar o nome do campeonato
    // O nome deve começar com uma letra
    // O nome deve ter entre 3 a 29 caracteres
    public static boolean validaNomeCamp(String nomeCamp)
    {

        String regex = "^[A-Za-z]\\w{3,29}$";

        Pattern p = Pattern.compile(regex);

        if (nomeCamp == null) {
            return false;
        }
        Matcher m = p.matcher(nomeCamp);

        return m.matches();
    }

    // Função para validar o nome do circuito
    // O nome deve começar com uma letra
    // O nome deve ter entre 3 a 29 caracteres
    public static boolean validaNomeCir(String nomeCir)
    {

        String regex = "^[A-Za-z]\\w{3,29}$";

        Pattern p = Pattern.compile(regex);

        if (nomeCir == null) {
            return false;
        }
        Matcher m = p.matcher(nomeCir);

        return m.matches();
    }

    // Função que verifica se não há nomes repetidos na lista de campeonatos
    public boolean verificaNomeCamp(String nomeCamp){
        return campeonatos.containsKey(nomeCamp);
    }

    // Função que verifica se não há nomes repetidos na lista de circuitos
    public boolean verificaNomeCir(String nomeCir){
        return circuitos.containsKey(nomeCir);
    }

    public ArrayList<Circuito> getCircuitos(){
        ArrayList<Circuito> r = new ArrayList<>();
        for (Entry<String,Circuito> iterador : circuitos.entrySet()) {
            r.add(iterador.getValue());
        }
        return r;
    }

    public ArrayList<Segmentos> assemble(ArrayList<Segmentos> lista,int n_chicanes, int ncurvas){
        int nretas = n_chicanes + ncurvas;
        ArrayList<Segmentos> seg = new ArrayList<Segmentos>();
        int iterator = 0;
        for (int i = 0; iterator < lista.size();i++){
            if (nretas !=0){
            
                if (i % 2 != 0){
                    seg.add(lista.get(iterator));
                    iterator++;
                }
                else {
                    Segmentos n = new Segmentos(SegmentoEstrada.RETA,-1);
                    seg.add(n);
                    nretas--;
                    
                }
                
            }
        }
        return seg;
    }
}
