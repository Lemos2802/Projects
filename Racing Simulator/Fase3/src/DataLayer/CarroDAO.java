package DataLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import BussinessLayer.SubCarro.Carro;
import BussinessLayer.SubCarro.GT;
import BussinessLayer.SubCarro.C1;
import BussinessLayer.SubCarro.C1H;
import BussinessLayer.SubCarro.C2;
import BussinessLayer.SubCarro.C2H;
import BussinessLayer.SubCarro.GTH;
import BussinessLayer.SubCarro.SC;

public class CarroDAO implements Map<Integer,Carro>{
	private static CarroDAO singleton = null;

	private CarroDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS carros (" +
                    "carID INT NOT NULL PRIMARY KEY," +
                    "categoria varchar(10) NOT NULL," +
                    "marca varchar(45) NOT NULL," +
                    "modelo varchar(45) NOT NULL," +
                    "cilindrada INT NOT NULL," +
                    "potenciaICE INT NOT NULL," +
                    "fiabilidade INT NOT NULL," +
                    "downforce INT NOT NULL,"+
                    "estadoPneus INT NOT NULL,"+
                    "tipoPneus varchar(45) NOT NULL,"+
                    "modoMotor varchar(45) NOT NULL,"+
                    "potenciaE INT NOT NULL,"+
                    "taxaDeterioracaoGT INT NOT NULL)";

            stm.executeUpdate(sql);
			
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

	// Implementação do padrão Singleton
    // devolve a instância única desta classe
    public static CarroDAO getInstance() {
        if (CarroDAO.singleton == null) {
            CarroDAO.singleton = new CarroDAO();
        }
        return CarroDAO.singleton;
    }

	@Override
	// Este método devolve o número de entradas na tabela na base de dados
	public int size() {
		int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT count(*) FROM carros")) {	// falta adicionar isto
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
			stm.executeQuery("SELECT CarId FROM carros WHERE CarID='"+key.toString()+"'")) {	// falta adicionar cenas
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
		Carro a = (Carro) value;
        return this.containsKey(a.getCarID());
	}

	@Override
	// Método que devolve o carro cujo carId é o passado como argumento
    // Lança exceção caso haja algum problema na procura na base de dados
	public Carro get(Object key) {
		Carro a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM carros WHERE CarId='"+key+"'")) {	// falta adicionar cenas
            if (rs.next()) {  // A chave existe na tabela
                
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
	}

	@Override
	// Adiconar entrada á tabela dos carros na base de dados. O carId do carro é a identificação do objeto na tabela
    // É lançada exceção caso haja algum problema relativo á database
	public Carro put(Integer key, Carro c) {
		Carro res = null;
        if (!this.containsKey(key)) {
            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                 Statement stm = conn.createStatement()) {
                // Actualizar a turma
                if(c.getClass() == C1H.class) {
                    C1H c1= (C1H) c;
                    stm.executeUpdate(		
                        "INSERT INTO carro VALUES ('" + c.getCarID() + "', '" + c.getMarca() + "', '" + c.getModelo() + "', '" + c.getCilindrada() + "','" + c.getPotenciaICE() + "', '" 
                        + c.getFiabilidade() + "', '"+ c.getDownforce() + "', '" + c.getEstadoPneus() + "', '" + c.getTipoPneus() + "', '" + c.getModoMotor() + "', " +((C1H)c).getPotenciaE() + "', '" + "NULL" + "')");
                }
                else if(c.getClass() == C2H.class) {
                    C2H c1= (C2H) c;
                    stm.executeUpdate(		
                        "INSERT INTO carro VALUES ('" + c.getCarID() + "', '" + c.getMarca() + "', '" + c.getModelo() + "', '" + c.getCilindrada() + "','" + c.getPotenciaICE() + "', '" 
                        + c.getFiabilidade() + "', '"+ c.getDownforce() + "', '" + c.getEstadoPneus() + "', '" + c.getTipoPneus() + "', '" + c.getModoMotor() + "', " +((C2H)c).getPotenciaE() + "', '" + "NULL" + "')");
                }
                else if(c.getClass() == GTH.class) {
                    GTH c1= (GTH) c;
                    stm.executeUpdate(		
                        "INSERT INTO carro VALUES ('" + c.getCarID() + "', '" + c.getMarca() + "', '" + c.getModelo() + "', '" + c.getCilindrada() + "','" + c.getPotenciaICE() + "', '" 
                        + c.getFiabilidade() + "', '"+ c.getDownforce() + "', '" + c.getEstadoPneus() + "', '" + c.getTipoPneus() + "', '" + c.getModoMotor() + "', " +((GTH)c).getPotenciaE() + "', '" + ((GTH)c).getTaxaDeterioracao() + "')");
                }
                else if (c.getClass() == SC.class){
                    SC c1= (SC) c;
                    stm.executeUpdate(		
                        "INSERT INTO carro VALUES ('" + c.getCarID() + "', '" + c.getMarca() + "', '" + c.getModelo() + "', '" + c.getCilindrada() + "','" + c.getPotenciaICE() + "', '" 
                        + c.getFiabilidade() + "', '"+ c.getDownforce() + "', '" + c.getEstadoPneus() + "', '" + c.getTipoPneus() + "', '" + c.getModoMotor() + "', " +"NULL" + "', '" + "NULL" + "')");
                }
                else if(c.getClass() == GT.class) {
                    GT c1= (GT) c;
                    stm.executeUpdate(		
                        "INSERT INTO carro VALUES ('" + c.getCarID() + "', '" + c.getMarca() + "', '" + c.getModelo() + "', '" + c.getCilindrada() + "','" + c.getPotenciaICE() + "', '" 
                        + c.getFiabilidade() + "', '"+ c.getDownforce() + "', '" + c.getEstadoPneus() + "', '" + c.getTipoPneus() + "', '" + c.getModoMotor() + "', " +"NULL" + "', '" + ((GT)c).getTaxaDeterioracao() + "')");
                }
                else if(c.getClass() == C1.class) {
                    C1 c1= (C1) c;
                    stm.executeUpdate(		
                        "INSERT INTO carro VALUES ('" + c.getCarID() + "', '" + c.getMarca() + "', '" + c.getModelo() + "', '" + c.getCilindrada() + "','" + c.getPotenciaICE() + "', '" 
                        + c.getFiabilidade() + "', '"+ c.getDownforce() + "', '" + c.getEstadoPneus() + "', '" + c.getTipoPneus() + "', '" + c.getModoMotor() + "', " +"NULL" + "', '" + "NULL" + "')");
                }
                else if(c.getClass() == C2.class) {
                    C2 c1= (C2) c;
                    stm.executeUpdate(		
                        "INSERT INTO carro VALUES ('" + c.getCarID() + "', '" + c.getMarca() + "', '" + c.getModelo() + "', '" + c.getCilindrada() + "','" + c.getPotenciaICE() + "', '" 
                        + c.getFiabilidade() + "', '"+ c.getDownforce() + "', '" + c.getEstadoPneus() + "', '" + c.getTipoPneus() + "', '" + c.getModoMotor() + "', " +"NULL" + "', '" + "NULL" + "')");
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
	// Método que remove da tabela dos carros na database, o cicuito cujo carId é passado como argumento.
    // É necessário por null em todas as referencias do carro removido em outras tabelas
    // Lançada exceção caso haja algum problema na base de dados
	public Carro remove(Object key) {
		Carro t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM carros WHERE CarID='"+key+"'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
	}

	@Override
	//Método que adiciona de uma unica vez vários carros à tabela na base de dados
	public void putAll(Map<? extends Integer, ? extends Carro> m) {
		for(Carro t : m.values()) {
            this.put(t.getCarID(), t);
        }		
	}

	@Override
	// Método que elimina todas as entradas na tabela carros.
    // É necessário por null em todas as referencias do carro removido em outras tabelas
    // É lançada exceção caso haja algum problema com a base de dados
	public void clear() {
		try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {	// falta adicionar cenas
            
            stm.executeUpdate("TRUNCATE carros");
            
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
	}

	@Override
	// Método que devolve um set com todos os carIds de carros presentes na base de dados
    // É lançada exceção caso haja algum problema com a base de dados
	public Set<Integer> keySet() {
		Set<Integer> col = new HashSet<>();
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
	// Método que devolve uma collection com todos os objetos carros presentes na base de dados
    // É lançada exceção caso haja algum problema com a base de dados
	public Collection<Carro> values() {
		Collection<Carro> col = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("")) {	// "SELECT Codigo FROM arestas"
            while (rs.next()) {
                // col.add(this.get(rs.getString("Codigo"))); // falta adicionar cenas acho eu
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
	}

	@Override
	// Método que devolve um set com todos "pares" formados pelo carId do carro e o objeto carro correspondente
    // É lançada exceção caso haja algum problema com a base de dados
	public Set<Entry<Integer, Carro>> entrySet() {
		Map.Entry<Integer,Carro> entry;
        HashSet<Map.Entry<Integer, Carro>> col = new HashSet<>();
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
