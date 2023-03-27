using System.ComponentModel.DataAnnotations;

namespace PeopleMarket.ViewModels
{
    public class RegisterFeiranteViewModel
    {
        public string Username { get; set; }

        [EmailAddress]
        public string Email { get; set; }

        [DataType(DataType.Password)]
        public string Password { get; set; }

        [DataType(DataType.Password)]
        [Compare("Password")]
        public string ConfirmPassword { get; set; }

        public string NomeCompleto { get; set; }

        public string Morada { get; set; }

        [DataType(DataType.PhoneNumber)]
        public string PhoneNumber { get; set; }
        
        public string Iban { get; set; }

        public string CC { get; set; }
    }
}