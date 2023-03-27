using System.ComponentModel.DataAnnotations;

namespace PeopleMarket.ViewModels
{
    public class PerfilViewModel
    {
        [Required]
        public string Username { get; set; }

        [EmailAddress]
        public string Email { get; set; }

        [DataType(DataType.PhoneNumber)]
        public string PhoneNumber { get; set; }

        public string nCC { get; set; }

        public string nif { get; set; }
    }
}