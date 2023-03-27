using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;


namespace PeopleMarket.Models
{
    public class Feirante
    {
        public int Id { get; set; }

        [Required]
        [PersonalData]
        public string iban { get; set; }

        [Required]
        [PersonalData]
        public string numCC { get; set; }
        public string descricao { get; set; }
        public string ClienteUserName { get; set; }

        public virtual Cliente Cliente { get; set; }
    }
}
