package BussinessLayer.SubUtilizador;

public interface IUtilizador {
    public void registaUtilizador(String codUtil, String password, String email, boolean isPremium);
    public void registaAdmin(String codUtil);
    public boolean autenticaUtilizador(String codUtil, String password);
    public boolean verificaMail(String email);
    public boolean verificaPass(String password);
    public void addPontuacaoGlobal(String codUtil, int pontuacaoCampeonato);
    public boolean verificaUtilizador(String codUtil);
}
