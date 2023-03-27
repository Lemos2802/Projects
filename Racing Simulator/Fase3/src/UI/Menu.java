package UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public boolean logged;
    public interface Handler {
        public void execute();
    }
    public interface PreCondition { 
        public boolean validate();
    }
    private static Scanner is = new Scanner(System.in);
    private List<String> opcoes;            
    private List<PreCondition> disponivel;  
    private List<Handler> handlers;       
    
    // Constructor for objects of class NewMenu
    public Menu(String[] opcoes) {
        this.opcoes = Arrays.asList(opcoes);
        this.disponivel = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.opcoes.forEach(s -> {
            this.disponivel.add(() -> true);
            this.handlers.add(() -> System.out.println("\nATENÇÃO: Opção não implementada!"));

        });
    }

    // --- Métodos ---

    /**
     * Método para correr o Newmenu
     */
    public void run() {
        int op;
        do {
            show();
            op = readOption();
            // testar pré-condição
            if (op > 0 && !this.disponivel.get(op - 1).validate()) {
                System.out.println("Opção indisponível! Tente novamente.");
            } else if (op > 0) {
                // executar handler
                this.handlers.get(op - 1).execute();
            }
        } while (op != 0);
    }

    /**
     * Método que regista uma uma pré-condição numa opção do NewMenu.
     *
     * @param i índice da opção (começa em 1)
     * @param b pré-condição a registar
     */
    public void setPreCondition(int i, PreCondition b) {
        this.disponivel.set(i - 1, b);
    }

    /**
     * Método para registar um handler numa opção do NewMenu.
     *
     * @param i indice da opção  (começa em 1)
     * @param h handlers a registar
     */
    public void setHandler(int i, Handler h) {
        this.handlers.set(i - 1, h);
    }

    // Método para representação visual do menu
    private void show() {
        System.out.println("--------------------------------------------");
        System.out.println("                    Menu                    ");
        System.out.println("--------------------------------------------");
        for (int i = 0; i < this.opcoes.size(); i++) {
            if (this.disponivel.get(i).validate()) {
                System.out.print(i + 1);
                System.out.print(" - ");
                System.out.println(this.disponivel.get(i).validate() ? this.opcoes.get(i) : "---");
            }
        }
        System.out.println("0 - Sair");
        System.out.println("--------------------------------------------");
    }

    /** Função que realiza a leitura das opções
    * Retorna uma string de erro caso leia uma opção inválida
    */
    private int readOption() {
        int op=0;

        System.out.print("Opção: ");
        try {
            String str = is.nextLine();
            op = Integer.parseInt(str);
        } catch (Exception e) { // Não foi inscrito um int
            op = -1;
        }
        if (op < 0 || op > this.opcoes.size()) {
            System.out.println("Opção Inválida");
            op = -1;
        }
        return op;
    }
}