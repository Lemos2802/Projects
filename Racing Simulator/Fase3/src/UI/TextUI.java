package UI;

import java.util.ArrayList;
import java.util.Scanner;

import BussinessLayer.SubCampeonato.Circuito;
import BussinessLayer.SubCampeonato.GestCampeonatoFacade;
import BussinessLayer.SubCampeonato.ICampeonato;
import BussinessLayer.SubCampeonato.Segmentos;
import BussinessLayer.SubCampeonato.Segmentos.SegmentoEstrada;
import BussinessLayer.SubCarro.GestCarroFacade;
import BussinessLayer.SubCarro.ICarros;
import BussinessLayer.SubPiloto.GestPilotoFacade;
import BussinessLayer.SubPiloto.IPiloto;
import BussinessLayer.SubSimulacao.ISimulacao;
import BussinessLayer.SubSimulacao.SimulacaoFacade;
import BussinessLayer.SubUtilizador.GestUtilizadorFacade;
import BussinessLayer.SubUtilizador.IUtilizador;
import BussinessLayer.SubUtilizador.Utilizador;

/**
 * TextUI
 */
public class TextUI {
	private ICampeonato modelCampeonato;
	private ICarros modelCarro;
	private IPiloto modelPiloto;
	private IUtilizador modelUtilizador;
	private ISimulacao modelSimulacao;

	// Scanner para leitura
	private Scanner scin;
	private Scanner scin2;

	public TextUI() {
		this.modelCampeonato = new GestCampeonatoFacade();
		this.modelCarro = new GestCarroFacade();
		this.modelPiloto = new GestPilotoFacade();
		this.modelUtilizador = new GestUtilizadorFacade();
		this.modelSimulacao = new SimulacaoFacade();
		this.scin = new Scanner(System.in);
        this.scin2 = new Scanner(System.in);
	}

	public void run() {
		boolean logged = false;
		Menu menu = new Menu(new String[]{
			"Registar-se",
			"Autenticar-se",
			"Adicionar Circuito",
			"Adicionar Piloto",
			"Adicionar Campeonato",
			"Adicionar Carro",
		});
		menu.setPreCondition(1,()-> !logged);
        menu.setPreCondition(2,()-> !logged);
        menu.setPreCondition(3,()-> logged);
        menu.setPreCondition(4,()-> logged);
        menu.setPreCondition(5,()-> logged);
        menu.setPreCondition(6,()-> logged);

		menu.setHandler(1,()->{
            int tom = -1;
            for (int i = 0; tom < 0 || tom > 2; i++) {
                if (i > 0)
                    System.out.println("A opção escolhida não se encontra nas opções disponíveis!\n");
                System.out.println("Selecione o tipo de utilizador que pretende");
                System.out.println("1-> Jogador");
                System.out.println("2-> Administrador");
                System.out.println("0-> Terminar Operação");
                tom = scin2.nextInt();
                if (tom!=0) {
                    System.out.println("Insira o seu nome de utilizador!");
                    String username = scin.nextLine();
                    while(modelUtilizador.verificaUtilizador(username) == false){
                        System.out.println("Este nome de utilizador ja existe por favor insira um diferente");
                        username = scin.nextLine();
                    }
                    System.out.println("Insira a sua password!");
                    String password = scin.nextLine();
                    while(modelUtilizador.verificaPass(password) == false){
                        System.out.println("A password e muito fraca.\nA password deve ter no minimo 6 a 20 carateres, conter uma maiuscula, uma minuscula, um digito e um carater especial (@, #, $, %, ! ou ?)");
                        password = scin.nextLine();
                    }
                    System.out.println("Insira o seu email!");
                    String email = scin.nextLine();
                    while(modelUtilizador.verificaPass(email) == false){
                        System.out.println("O email introduzido nao esta dentro das normas.");
                        email = scin.nextLine();
                    }
                    if (tom==1) {
                        this.modelUtilizador.registaUtilizador(username, password, email, false);
                    }
                    else {
                        this.modelUtilizador.registaUtilizador(username, password, email, false);
                        this.modelUtilizador.registaAdmin(username);
                    }
                }
            }
        });

		menu.setHandler(2,()->{
            int tom = -1;
            for (int i = 0; tom < 0 || tom > 2; i++) {
                if (i > 0)
                    System.out.println("A opção escolhida não se encontra nas opções disponíveis!\n");
                System.out.println("Selecione o tipo de utilizador que pretende");
                System.out.println("1-> Jogador");
                System.out.println("2-> Administrador");
                System.out.println("0-> Terminar Operação");
                tom = scin2.nextInt();
                if (tom!=0) {
                    System.out.println("Insira o seu nome de utilizador!");
                    String username = scin.nextLine();
                    System.out.println("Insira a sua password!");
                    String password = scin.nextLine();
                    if (tom==1) {
                        System.out.println(modelUtilizador.autenticaUtilizador(username, password));
                    }
                }
            }
        });

		menu.setHandler(3,()->{
            try {
                System.out.println("Insira a nome do circuito:");
                String nomeCir = scin.nextLine();
                System.out.println("Insira a distância do circuito:");
                double distancia = scin2.nextDouble();
                int opt=-1;
                ArrayList<Segmentos> segmentos = new ArrayList<>();
                int nCurvas = 0, nChicanes = 0;
                for (int i = 0; opt !=0; i++) {
                    if (i > 0 && (opt < 0 || opt > 2))
                        System.out.println("A opção escolhida não se encontra nas opções disponíveis!\n");
                    else {
                        System.out.println("Selecione o tipo de manobra a adicionar ao circuito:");
                        System.out.println("1-> Curva (" + Integer.toString(nCurvas) + ")");
                        System.out.println("2-> Chicane (" + Integer.toString(nChicanes) + ")");
                        System.out.println("0-> Terminar Operação");
                        opt = scin2.nextInt();
                        if (opt != 0) {
                            System.out.println("Insira o grau de dificuldade de ultrapassagem (Valor entre 0 e 1):");
                            int gdu = scin2.nextInt();
							Segmentos s;
                            if (opt == 1) {
								s = new Segmentos(SegmentoEstrada.CURVA, gdu);
                                nCurvas++;
                            }
                            else{
                                s = new Segmentos(SegmentoEstrada.CHICANE, gdu);
                                nChicanes++;
                            }
                            segmentos.add(s);
                        }
                    }
                }
                segmentos = modelCampeonato.assemble(segmentos, nChicanes, nCurvas);
                for (Segmentos iterador : segmentos) {
                    if(iterador.getGdu() == -1)
                        System.out.println("Insira o grau de dificuldade de ultrapassagem da reta (Valor entre 0 e 1):");
                        int gdu = scin2.nextInt();
                        while(gdu < 0 ||  gdu > 1) {
                            System.out.println("Insira o grau de dificuldade de ultrapassagem da reta CORRETAMENTE (VALOR ENTRE 0 e 1):");
                            gdu = scin2.nextInt();
                        }
                        iterador.setGdu(gdu);
                }
                System.out.println("Insira o número de voltas do circuito:");
                int numeroVoltas = scin2.nextInt();
                modelCampeonato.registaCircuito(nomeCir, distancia,numeroVoltas,segmentos);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        });

		menu.setHandler(4,()->{
            try {
                System.out.println("Insira o nome do Piloto a registar");
                String nome = scin.nextLine();
                double chuvaVsSeco= -1;
                for (int i = 0; chuvaVsSeco < 0 || chuvaVsSeco > 1; i++) {
                    if ((i < 0 || i > 1) && i != 0)
                        System.out.println("O valor indicado não é válido!\n");
                    System.out.println("Insira a sua habilidade Chuva vs Tempo Seco (Valor entre 0 e 1)");
                    chuvaVsSeco = scin2.nextDouble();
                }
                double SegVSAgre= -1;
                for (int i = 0; SegVSAgre < 0 || SegVSAgre > 1; i++) {
                    if ((i < 0 || i > 1) && i != 0)
                        System.out.println("O valor indicado não é válido!\n");
                    System.out.println("Insira a sua habilidade Segurança vs Agressividade (Valor entre 0 e 1)");
                    SegVSAgre = scin2.nextDouble();
                }
                System.out.println("Insira a nacionalidade do Piloto a registar");
                String nacionalidade = scin.nextLine();
                modelPiloto.registaPiloto(nome,chuvaVsSeco,SegVSAgre,nacionalidade);
            }
            catch (Exception e) {
                System.out.println("Dados inválidos");
            }
        });

		menu.setHandler(5,()->{
            try {
                System.out.println("Insira o nome do campeonato a registar");
                String nome = scin.nextLine();
                int opt=-1;
                ArrayList<Circuito> circuitos = new ArrayList<>();
                for (int i = 0; opt !=0; i++) {
                    if (i > 0 && (opt < 0 || opt > 2))
                        System.out.println("A opção escolhida não se encontra nas opções disponíveis!\n");
                    System.out.println("1-> Adicionar um novo Circuito ao Campeonato");
                    System.out.println("0-> Terminar Operação");
                    opt = scin2.nextInt();
                    if (opt==1) {
                        ArrayList<Circuito> circ = modelCampeonato.getCircuitos();
                        for (Circuito c : circ) {
                            System.out.println(">" + c.getNomeCir());
                        }
                        System.out.println("Insira o nome do Circuito a Adicionar:");
                        String nomeCirc = scin.nextLine();
                        for (Circuito c : circ)
                            if(c.getNomeCir() == nomeCirc){
                                circuitos.add(c);
                                break;
                            }
                        else {
                            System.out.println("O circuito não é válido");
                        }
                    }
                }
                System.out.println("Insira o numero maximo de participantes para este campeonato (Valor entre 2 e 30):");
                int nrMaxParticipantes = scin2.nextInt();
                while(nrMaxParticipantes < 2 ||  nrMaxParticipantes > 30) {
                    System.out.println("Insira o numero maximo de participantes para este campeonato CORRETAMENTE (VALOR ENTRE 2 e 30):");
                    nrMaxParticipantes = scin2.nextInt();
                }
                modelCampeonato.registaCampeonato(nome, nrMaxParticipantes, circuitos);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        });

		menu.setHandler(6,()->{
            try {
                System.out.println("Insira a marca do carro a registar");
                String marca = scin.nextLine();
                System.out.println("Insira o modelo do Carro a registar");
                String modelo = scin.nextLine();
                int opt=-1;
                for (int i = 0; opt<0 || opt>4; i++) {
                    if (i > 0)
                        System.out.println("A opção escolhida não se encontra nas opções disponíveis!\n");
                    else {
                        System.out.println("Selecione a categoria do carro a adicionar:");
                        System.out.println("1-> Classe 1");
                        System.out.println("2-> Classe 2");
                        System.out.println("3-> Classe Grand Turismo");
                        System.out.println("4-> Stock Cars");
                        System.out.println("0-> Terminar Operação");
                        opt = scin2.nextInt();
                        int cilindrada = 0;
                        if (opt == 2 || opt == 3) {
                                while (!modelCarro.verificaCilindrada(cilindrada)) {
                                    System.out.println("Insira a cilindrada do carro:");
                                    cilindrada = scin2.nextInt();
                                }
                        }
                        System.out.println("Insira a potência do motor de combustão do carro:");
                        int potenciaICE = scin2.nextInt();
                        System.out.println("Insira o downforce do carro (valor 0 a 1):");
                        double downforce = scin2.nextDouble();
                        if (opt ==1 || opt==2 || opt==3) {
                            int opt1 = -1;
                            for (int j = 0; opt1<1 || opt1>2; j++) {
                                if (j > 0)
                                    System.out.println("A opção escolhida não se encontra nas opções disponíveis!\n");
                                else {
                                    System.out.println("Selecione o tipo de veículo a adicionar:");
                                    System.out.println("1-> Combustão");
                                    System.out.println("2-> Híbrido");
                                    opt1 = scin2.nextInt();
                                }
                                if (opt1==2) {
                                    System.out.println("Insira a potência do motor elétrico do carro:");
                                    int potenciaE = scin2.nextInt();
                                    if (opt == 3) {
                                        modelCarro.registaGTHibrido(marca, modelo, cilindrada, downforce, potenciaICE, potenciaE);
                                    }
                                    else if (opt==1) {
                                        modelCarro.registaC1Hibrido(marca, modelo, downforce, potenciaICE, potenciaE);
                                    }
                                    else;
                                        modelCarro.registaGTHibrido(marca, modelo, cilindrada, downforce, potenciaICE, potenciaE);
                                }
                                else {

                                    if (opt == 3) {
                                        modelCarro.registaGT(marca, modelo, cilindrada, downforce, potenciaICE);
                                    }
                                    else if (opt==1) {
                                        modelCarro.registaC1(marca, modelo, downforce, potenciaICE);
                                    }
                                    else if (opt==2) {
                                        modelCarro.registaC2(marca, modelo, cilindrada, downforce, potenciaICE);
                                    }
                                }
                            }
                        }
                        else if (opt==4){
                            modelCarro.registaSC(marca, modelo, downforce, potenciaICE);
                        }
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Dados inválidos");
            }
        });

        menu.run();
	}
}
