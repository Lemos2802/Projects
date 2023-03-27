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
import BussinessLayer.SubCampeonato.Circuito;
import BussinessLayer.SubCampeonato.Segmentos;
import BussinessLayer.SubCampeonato.Segmentos.SegmentoEstrada;

public class CircuitoDAO implements Map<String,Circuito>{
	private static CircuitoDAO singleton = null;

	private CircuitoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS circuito (" +
                            "nomeCir VARCHAR(30) NOT NULL PRIMARY KEY," +
                            "distancia DOUBLE NOT NULL,"+
                            "nVoltas INT NOT NULL," +
                            "tempoBox INT NOT NULL)";
                stm.executeUpdate(sql);
                sql = "CREATE TABLE IF NOT EXISTS SegmentoEstrada(" +
                    "nomeCir varchar(30) NOT NULL," +
                    "tipoSegmento int NOT NULL,"+
                    "gdu double NOT NULL,"+
                    "PRIMARY KEY (nomeCir,tipoSegmento),"+
                    "FOREIGN KEY(nomeCir) REFERENCES circuito(nomeCir))";
                stm.executeUpdate(sql);
            } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

	// Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static CircuitoDAO getInstance() {
        if (CircuitoDAO.singleton == null) {
            CircuitoDAO.singleton = new CircuitoDAO();
        }
        return CircuitoDAO.singleton;
    }

	@Override
	// Este método devolve o número de entradas na tabela na base de dados
	public int size() {
		int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("######")) {	// falta adicionar isto
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
			stm.executeQuery("SELECT nomeCir FROM circuito WHERE nomeCir='"+key.toString()+"'")) {	// falta adicionar cenas
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
		Circuito a = (Circuito) value;
        return this.containsKey(a.getNomeCir());
	}

	@Override
	// Método que devolve o circuito cujo nome é o passado como argumento
    // Lança exceção caso haja algum problema na procura na base de dados
	public Circuito get(Object key) {
		Circuito c = null;
        ArrayList<Segmentos> segmentos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM circuito WHERE nomeCircuito='"+key+"'")) {	// falta adicionar cenas
            if (rs.next()) {  // A chave existe na tabela
                ResultSet r = stm.executeQuery("SELECT * FROM SeccaoCircuito WHERE nomeCircuito='"+key+"'");
                while(r.next()) {
                    SegmentoEstrada seg = null;
                    int inteiro = r.getInt(2);
                    switch(inteiro){
                        case 1:
                            seg = SegmentoEstrada.CURVA;
                        case 2:
                            seg = SegmentoEstrada.RETA;
                        case 3:
                            seg = SegmentoEstrada.CHICANE;
                    }
                    Segmentos s = new Segmentos(seg,r.getInt(3));
                    segmentos.add(s);

            }
            c = new Circuito(rs.getString(1),rs.getDouble(2),rs.getInt(3),rs.getInt(4),segmentos);

            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
	}

	@Override
	// Adiconar entrada á tabela dos circuitos na base de dados. O nome do circuito é a identificação do objeto na tabela
    // É lançada exceção caso haja algum problema relativo á database
	public Circuito put(String key, Circuito c) {
		Circuito res = null;
        if (!this.containsKey(key)) {
            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                 Statement stm = conn.createStatement()) {
                
                stm.executeUpdate(
                        "INSERT INTO circuito VALUES ('" + c.getNomeCir() + "', '" +c.getDistancia()+ "', '" +c.getNumeroVoltas() + "', '" +c.getTempoBox() + "')"
                );
                for (Segmentos m : c.getListaSegmentos()) {
                    stm.executeUpdate(
                            "INSERT INTO SegmentoEstrada VALUES ('" + c.getNomeCir() + "', '" +m.getSegmento()+ "', '" +m.getGdu() + "') "
                    );
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
	// Método que remove da tabela dos circuitos na database, o cicuito cujo nome é passado como argumento.
    // É necessário por null em todas as referencias do circuito removido em outras tabelas
    // Lançada exceção caso haja algum problema na base de dados
	public Circuito remove(Object key) {
		Circuito t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM circuito WHERE nomeCir='"+key+"'");
            stm.executeUpdate("DELETE FROM SegmentoEstrada WHERE nomeCir='"+key+"'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
	}

	@Override
	//Método que adiciona de uma unica vez vários circuitos à tabela na base de dados
	public void putAll(Map<? extends String, ? extends Circuito> m) {
		for(Circuito t : m.values()) {
            this.put(t.getNomeCir(), t);
        }		
	}

	@Override
	// Método que elimina todas as entradas na tabela circuitos.
    // É necessário por null em todas as referencias do circuito removido em outras tabelas
    // É lançada exceção caso haja algum problema com a base de dados
	public void clear() {
		try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {	// falta adicionar cenas
            stm.executeUpdate("TRUNCATE circuito");
            stm.executeUpdate("TRUNCATE SeccaoCircuito");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
	}

	@Override
	// Método que devolve um set com todos os nomes de circuitos presentes na base de dados
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
	// Método que devolve uma collection com todos os objetos circuitos presentes na base de dados
    // É lançada exceção caso haja algum problema com a base de dados
	public Collection<Circuito> values() {
		Collection<Circuito> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT nomeCircuito FROM circuito")) {	// "SELECT Codigo FROM arestas"
            while (rs.next()) {
                String idC = rs.getString("nomeCircuito");  
                Circuito c = this.get(idC);                    
                res.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
	}

	@Override
	// Método que devolve um set com todos "pares" formados pelo nome do circuito e o objeto circuito correspondente
    // É lançada exceção caso haja algum problema com a base de dados
	public Set<Entry<String, Circuito>> entrySet() {
		Map.Entry<String,Circuito> entry;
        HashSet<Map.Entry<String, Circuito>> col = new HashSet<>();
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
