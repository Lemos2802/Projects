namespace PeopleMarket.Models
{
    public class ListaHistoricoProdutos
    {
        public string Id { get; set; }
        public int quantidade { get; set; }
        public int ProdutoId { get; set; }
        public int HistoricoId { get; set; }

        public virtual Historico Historico { get; set; }
    }
}