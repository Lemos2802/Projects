package BussinessLayer.SubSimulacao;

import java.util.Map;

import BussinessLayer.SubCampeonato.Campeonato;
import BussinessLayer.SubCarro.Carro;
import BussinessLayer.SubPiloto.Piloto;
import util.Pair;

public interface ISimulacao {
    public Map<String,Integer> simulacao(Campeonato campeonato, Map<String, Pair<Carro, Piloto>> jogadores);
}