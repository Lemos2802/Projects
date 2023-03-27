package BussinessLayer.SubUtilizador;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Write a description of class Utilizador here.
 *
 */

public class Utilizador {
    private String codUtil;
    private String password;
    private String email;
    private int pontuacaoGlobal;
    private Boolean isAdmin;
    private Boolean isPremium;

    // --- Empty Constructor ---
    public Utilizador() {
        this.codUtil = null;
        this.password = null;
        this.email = null;
        this.pontuacaoGlobal = 0;
        this.isAdmin = false;
        this.isPremium = false;
    }

    // --- Parameterized Constructor ---
    public Utilizador(String codUtil, String password, String email, int pontuacaoGlobal, Boolean isAdmin, Boolean isPremium){
        this.codUtil = codUtil;
        this.password = password;
        this.email = email;
        this.pontuacaoGlobal = pontuacaoGlobal; 
        this.isAdmin = isAdmin;
        this.isPremium = isPremium;
    }

    // --- Copy Constructor ---
    public Utilizador(Utilizador utilizador) {
        this.codUtil = utilizador.getCodUtil();
        this.password = utilizador.getPassword();
        this.email = utilizador.getEmail();
        this.pontuacaoGlobal = utilizador.getPontuacaoGlobal();
        this.isAdmin = utilizador.getIsAdmin();
        this.isPremium = utilizador.getIsPremium();
    }

    // --- Getters & Setters ---
    public String getCodUtil() {
        return this.codUtil;
    }

    public void setCodUtil(String codUtil) {
        this.codUtil = codUtil;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPontuacaoGlobal() {
        return this.pontuacaoGlobal;
    }

    public void setPontuacaoGlobal(int pontuacaoGlobal) {
        this.pontuacaoGlobal = pontuacaoGlobal;
    }

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsPremium() {
        return this.isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

   

    
}
