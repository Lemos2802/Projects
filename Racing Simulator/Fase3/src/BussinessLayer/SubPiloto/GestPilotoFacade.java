package BussinessLayer.SubPiloto;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DataLayer.PilotoDAO;

public class GestPilotoFacade implements IPiloto{
    private Map<String,Piloto> pilotos;

    public GestPilotoFacade(){
        this.pilotos = PilotoDAO.getInstance();
    }

    // Função que regista um objeto piloto na base de dados
    public void registaPiloto(String nomePiloto, double cvs,double sva, String nacionalidade) {
        Piloto pilot = new Piloto(nomePiloto, cvs, sva, nacionalidade);
        this.pilotos.put(pilot.getNomePiloto(), pilot);
    }

    public Piloto getPiloto(String nomePiloto){
        return this.pilotos.get(nomePiloto);
    }

    // --- Métodos --- 

    // Função para validar o nome do piloto
    // O nome deve começar com uma letra maiuscula
    // O nome deve ter entre 2 a 29 caracteres
    public static boolean validaNomePiloto(String nomePiloto){

        String regex = "^[A-Z]\\w{2,29}$";
        Pattern p = Pattern.compile(regex);
        if (nomePiloto == null) {
            return false;
        }
        Matcher m = p.matcher(nomePiloto);
        return m.matches();
    }

    // Função que verifica se não há nomes repetidos na lista de pilotos
    public boolean verificaPiloto(String nomePiloto){
        return pilotos.containsKey(nomePiloto);
    }

    // Função para verificar a perícia CTS (Chuva vs Tempo Seco)
    public boolean verificaCts(double cts){
        return (cts >= 0 && cts <= 1);
    }

    // Função para verificar a perícia SVA (Segurança vs Agressividade)
    public boolean verificaSva(double sva){
        return (sva >= 0 && sva <= 1);
    }

}
