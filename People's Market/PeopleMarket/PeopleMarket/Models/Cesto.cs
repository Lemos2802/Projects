using System.ComponentModel.DataAnnotations;

namespace PeopleMarket.Models
{
    public class Cesto
    {
        public string Id { get; set; }
        public int quantidade { get; set; }
        public int ProdutoId { get; set; }
        public string ClienteUserName { get; set; }

        public virtual Produto Produto { get; set; }
        public virtual Cliente Cliente { get; set; }
    }
}