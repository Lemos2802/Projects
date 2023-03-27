using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PeopleMarket.Models
{
    public class Comentario
    {
        public int Id { get; set; }
        public string texto { get; set; }
        public string ClienteUserName { get; set; } 
        public int BarracaId { get; set; }

        public virtual Cliente Cliente { get; set; }
        public virtual Barraca Barraca { get; set; }
    }
}