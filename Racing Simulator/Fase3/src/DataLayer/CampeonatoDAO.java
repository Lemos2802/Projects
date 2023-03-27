package DataLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import BussinessLayer.SubCampeonato.Campeonato;
import BussinessLayer.SubCampeonato.Circuito;

public class CampeonatoDAO implements Map<String,Campeonato>{
	private static CampeonatoDAO singleton = null;

	private CampeonatoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
             String sql = "CREATE TABLE IF NOT EXISTS campeonatos (" +
                        "NomeCamp varchar(10) NOT NULL,"+
                        "nrCorridas INT NOT NULL ,"+
                        "nrMaxParticipantes INT NOT NULL ,"+
                        "nomeCir varchar(45) NOT NULL ,"+
                        "PRIMARY KEY(NomeCamp,nomeCir),"+
                        "FOREIGN KEY(nomeCir) references circuito(nomeCir))";
             stm.executeUpdate(sql);
			
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

	// Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static CampeonatoDAO getInstance() {
        if (CampeonatoDAO.singleton == null) {
            CampeonatoDAO.singleton = new CampeonatoDAO();
        }
        return CampeonatoDAO.singleton;
    }

	@Override
	// Este método devolve o número de entradas na tabela na base de dados
	public int size() {
		int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("")) {	// falta adicionar isto
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
	}

	@Override
	// Método que devolve se uma tabela está vazia ou não
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	// Método que devolve se o código de um dado  se encontra registada na base de dados
    // Lança exceção caso ocorra algum problema na procura.
	public boolean containsKey(Object key) {
		boolean r;
        try	(Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
			Statement stm = conn.createStatement();
			ResultSet rs =
			stm.executeQuery("'"+key.toString()+"'")) {	// falta adicionar cenas
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
	}

	@Override
	// Método que devolve se um dado cricuito se encontra registada na base de dados
	public boolean containsValue(Object value) {
		Campeonato a = (Campeonato) value;
        return this.containsKey(a.getNomeCamp());
	}

	@Override
	// Método que devolve o campeonato cujo nome é o passado como argumento
    // Lança exceção caso haja algum problema na procura na base de dados
	public Campeonato get(Object key) {
		Campeonato camp = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM campeonatos WHERE NomeCamp='"+key+"'")) {
                // A chave existe na tabela
              ArrayList<Circuito> listaCircuitos =new ArrayList<>();
              while (rs.next()) {
                      listaCircuitos.add(new Circuito(rs.getString(2)));
              }
              camp=new Campeonato(key.toString(),rs.getInt(2),rs.getInt(3),listaCircuitos);
  
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return camp;
	}

	@Override
	// Adiconar entrada á tabela dos campeonatos na base de dados. O nome do campeonato é a identificação do objeto na tabela
    // É lançada exceção caso haja algum problema relativo á database
	public Campeonato put(String key, Campeonato camp) {
		Campeonato res = null;
        if (!this.containsKey(key)) {
            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                 Statement stm = conn.createStatement()) {
                // Actualizar a turma
                for (Circuito circuito : camp.getCircuitosIntegrantes() ){
                    stm.executeUpdate(
                        "INSERT INTO campeonato VALUES ('" + camp.getNomeCamp() + "', '" + camp.getNrCorridas() + "', '" + camp.getNrMaxParticipantes()+ "', '" + circuito.getNomeCir() + "') " );
                }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        }
        return res;
	}

	@Override
	// Método que remove da tabela dos campeonatos na database, o cicuito cujo nome é passado como argumento.
    // É necessário por null em todas as referencias do campeonato removido em outras tabelas
    // Lançada exceção caso haja algum problema na base de dados
	public Campeonato remove(Object key) {
		Campeonato t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM campeonatos WHERE NomeCamp='"+key+"'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
	}

	@Override
	//Método que adiciona de uma unica vez vários campeonatos à tabela na base de dados
	public void putAll(Map<? extends String, ? extends Campeonato> m) {
		for(Campeonato t : m.values()) {
            this.put(t.getNomeCamp(), t);
        }		
	}

	@Override
	// Método que elimina todas as entradas na tabela campeonatos.
    // É necessário por null em todas as referencias do campeonato removido em outras tabelas
    // É lançada exceção caso haja algum problema com a base de dados
	public void clear() {
		try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {	// falta adicionar cenas
            stm.executeUpdate("TRUNCATE campeonatos");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
	}

	@Override
	// Método que devolve um set com todos os nomes de campeonatos presentes na base de dados
    // É lançada exceção caso haja algum problema com a base de dados
	public Set<String> keySet() {
		Set<String> col = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("")) { // "SELECT Codigo FROM arestas"
            while (rs.next()) {
                // col.add(rs.getString("Codigo")); // falta adicionar cenas acho eu
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
	}

	@Override
	// Método que devolve uma collection com todos os objetos campeonatos presentes na base de dados
    // É lançada exceção caso haja algum problema com a base de dados
	public Collection<Campeonato> values() {
		Collection<Campeonato> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT NomeCamp FROM campeonatos")) { // ResultSet com os ids de todas as turmas
            while (rs.next()) {
                String idC = rs.getString(1);
                Campeonato c = this.get(idC);
                res.add(c);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

	@Override
	// Método que devolve um set com todos "pares" formados pelo nome do campeonato e o objeto campeonato correspondente
    // É lançada exceção caso haja algum problema com a base de dados
	public Set<Entry<String, Campeonato>> entrySet() {
		Map.Entry<String,Campeonato> entry;
        HashSet<Map.Entry<String, Campeonato>> col = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("")) { // "SELECT Codigo FROM arestas"
            while (rs.next()) {
                entry = null; // new AbstractMap.SimpleEntry<>(rs.getString("Codigo"),this.get(rs.getString("Codigo"))); // falta cenas
                col.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
	}
}
