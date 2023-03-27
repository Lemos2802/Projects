package BussinessLayer.SubSimulacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import BussinessLayer.SubCampeonato.Campeonato;
import BussinessLayer.SubCampeonato.Circuito;
import BussinessLayer.SubCarro.Carro;
import BussinessLayer.SubPiloto.Piloto;
import util.Pair;

public class Simulacao {
	private int corridaAtual;
	private	Map<String,Integer> pontuacaoCamp;
	private Map<String,Integer> afinacoes;
	private int nrMaxAfinacoes;
	private Campeonato campeonato;
	private Map<String,Pair<Carro,Piloto>> jogadores;
	private Corrida corrida;


	public Simulacao() {
		this.corridaAtual = 0;
		this.pontuacaoCamp = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int value1 = pontuacaoCamp.get(o1);
				int value2 = pontuacaoCamp.get(o2);
				return Integer.compare(value2, value1);
			}
		});
		this.afinacoes = new HashMap<>();
		this.nrMaxAfinacoes = 0;
		this.campeonato = null;
		this.jogadores = new HashMap<>();
		this.corrida = new Corrida();
	}

	public Simulacao(int corridaAtual, Map<String,Integer> pontuacaoCamp, Map<String,Integer> afinacoes,
	int nrMaxAfinacoes, Campeonato campeonato, Map<String,Pair<Carro,Piloto>> jogadores, Corrida corrida) {
		this.corridaAtual = corridaAtual;
		this.pontuacaoCamp = pontuacaoCamp;
		this.afinacoes = afinacoes;
		this.nrMaxAfinacoes = nrMaxAfinacoes;
		this.campeonato = campeonato;
		this.jogadores = jogadores;
		this.corrida = corrida;
	}

	public Simulacao(Simulacao s) {
		this.corridaAtual = s.getCorridaAtual();
		this.pontuacaoCamp = s.getPontuacaoCamp();
		this.afinacoes = s.getAfinacoes();
		this.nrMaxAfinacoes = s.getNrMaxAfinacoes();
		this.campeonato = s.getCampeonato();
		this.jogadores = s.getJogadores();
		this.corrida = s.getCorrida();
	}

	public int getCorridaAtual() {
		return this.corridaAtual;
	}

	public void setCorridaAtual(int corridaAtual) {
		this.corridaAtual = corridaAtual;
	}

	public Map<String,Integer> getPontuacaoCamp() {
		return (Map<String, Integer>) this.pontuacaoCamp.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
	}

	public void setPontuacaoCamp(Map<String,Integer> pontuacaoCamp) {
		this.pontuacaoCamp = pontuacaoCamp;
	}

	public Map<String,Integer> getAfinacoes() {
		return (Map<String, Integer>) this.afinacoes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
	}

	public void setAfinacoes(Map<String,Integer> afinacoes) {
		this.afinacoes = afinacoes;
	}

	public int getNrMaxAfinacoes() {
		return this.nrMaxAfinacoes;
	}

	public void setNrMaxAfinacoes(int nrMaxAfinacoes) {
		this.nrMaxAfinacoes = nrMaxAfinacoes;
	}

	public Campeonato getCampeonato() {
		return new Campeonato(this.campeonato);
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Map<String,Pair<Carro,Piloto>> getJogadores() {
		return this.jogadores;
	}
	
	public void setJogadores(Map<String,Pair<Carro,Piloto>> jogadores) {
		this.jogadores = jogadores;
	}

	public Corrida getCorrida() {
		return this.corrida;
	}
	
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	// --- Métodos ---

	/**
	 * Itera sobre o map dos jogadores (Map que associa um ID a um par -> (Carro,Piloto))
	 * 		Inicializa a pontuacaoCamp associando a todos os IDs o valor 0
	 * 		Inicializa a afinacoes associando a todos os IDs o valor 0
	 * Calcula o numero maximo de afinacoes que se pode fazer durante o campeonato
	 * @param campeonato
	 * @param jogadores
	 */
	public void configuraSimulacao(Campeonato campeonato, Map<String,Pair<Carro,Piloto>> jogadores){
		this.corridaAtual = 0;
		for(Entry<String,Pair<Carro,Piloto>> iterador : jogadores.entrySet()) {
			this.pontuacaoCamp.put(iterador.getKey(), 0);
			this.afinacoes.put(iterador.getKey(), 0);
		}
		this.campeonato = campeonato;
		this.nrMaxAfinacoes = 2*(campeonato.getNrCorridas()/3);
		this.jogadores = jogadores;
	}

	/**
	 * Funcao que prepara a proxima corrida
	 * se a corrida atual for a 1.a do campeonato entao a ordem dos jogadores sera aquela com que foi inicializada o map jogadores
	 * caso contrario a ordem sera igual a classificacao da corrida anterior isto e o 1.o lugar comeca na primeira posicao e o ultimo comeca na ultima posicao
	 */
	public void proximaCorrida() {
		this.corrida = new Corrida();
		ArrayList<Pair<Carro,Piloto>> jogadoresCorrida = new ArrayList<>();
		Circuito c = this.campeonato.getCircuitosIntegrantes().get(corridaAtual);
		if (this.corridaAtual == 0){
			for (Entry<String,Pair<Carro,Piloto>> iterador : jogadores.entrySet())
				jogadoresCorrida.add(iterador.getValue());
			this.corrida.simulaCorrida(c, jogadoresCorrida);
		}
		else {
			for (Entry<String,Integer> iterador : pontuacaoCamp.entrySet())
				jogadoresCorrida.add(jogadores.get(iterador.getKey()));
			this.corrida.simulaCorrida(c, jogadoresCorrida);
		}
	}
}
