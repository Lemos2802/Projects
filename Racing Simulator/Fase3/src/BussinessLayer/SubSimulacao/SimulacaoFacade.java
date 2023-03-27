package BussinessLayer.SubSimulacao;

import java.util.Map;
import java.util.Map.Entry;
import util.Pair;

import BussinessLayer.SubCampeonato.Campeonato;
import BussinessLayer.SubPiloto.Piloto;
import BussinessLayer.SubCarro.Carro;

public class SimulacaoFacade implements ISimulacao{
	private Simulacao simulacao;


	public SimulacaoFacade() {
		this.simulacao = new Simulacao();
	}

	/**
	 * Atribui um novo CarId a todos os carros para serem distinguiveis posteriormente
	 * @param campeonato
	 * @param jogadores
	 * @return
	 */
	public Map<String,Integer> simulacao(Campeonato campeonato, Map<String, Pair<Carro, Piloto>> jogadores) {
		int i = 0;
		Pair<Carro, Piloto> temp;
		for (Entry<String,Pair<Carro,Piloto>> iterador : jogadores.entrySet()) {
			temp = iterador.getValue();
			Carro carro_temp = temp.getFirst();
			carro_temp.setCarID(i);
			temp.setFirst(carro_temp);
			i++;
		}
		this.simulacao.configuraSimulacao(campeonato, jogadores);
		
		for (i = 0; i < this.simulacao.getCampeonato().getNrCorridas(); i++) {
			this.simulacao.proximaCorrida();
			
		}

		return null;
	}
}
