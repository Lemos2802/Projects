using PeopleMarket.Models;

namespace PeopleMarket.ViewModels
{
    public class FeiraViewModel
    {
        public string idFeira { get; set; }
        public int nBarracas { get; set; }
        public int classificacao { get; set; }
        public string pathImage { get; set; }
        public DateTime data { get; set; }
        public Localizacao Localizacao { get; set; }
    }
}