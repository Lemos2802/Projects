using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PeopleMarket.Models
{
    public class Produto
    {
        public int Id { get; set; }
        public string nome { get; set; }
        public string descricao { get; set; }
        public double preco { get; set; }
        public int stock { get; set; }
        public string pathImage { get; set; }
        public int BarracaId { get; set; }

        public virtual Barraca Barraca { get; set; }
    }
}