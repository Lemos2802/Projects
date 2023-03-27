using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PeopleMarket.Models
{
    public class Localizacao
    {
        public int Id { get; set; }
        public string distrito { get; set; }
        public string concelho { get; set; }
        public string pathImage { get; set; }
    }
}