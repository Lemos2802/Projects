using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PeopleMarket.Models
{
    public class Historico
    {
        public int Id { get; set; }
        public string morada { get; set; }
        public int ClienteUserName { get; set; } 
        public int FeiranteId { get; set; }

        public virtual Cliente Cliente { get; set; }
        public virtual Feirante Feirante { get; set; }
    }
}
