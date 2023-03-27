using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PeopleMarket.Models
{
    public class Cliente : IdentityUser
    {
        [Required]
        public string nomeApelido { get; set; }

        [Required]
        [DataType(DataType.Password)]
        public string Password { get; set; }

        [Required]
        [EmailAddress]
        [StringLength(100)]
        public string Email { get; set; }

        [PersonalData]
        [StringLength(100)]
        public string morada { get; set; }
        [Phone]
        [PersonalData]
        [DataType(DataType.PhoneNumber)]
        public string telemovel { get; set; }
        public int ? FeiranteId { get; set; }

        public virtual Feirante Feirante { get; set; }

    }
}