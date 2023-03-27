using System.ComponentModel.DataAnnotations;

namespace PeopleMarket.Models
{
    public class Feira
    {
        public string Id { get; set; }
        public int nBarracas { get; set; }
        public int classificacao { get; set; }
        public int periodo { get; set; }
        public string pathImage { get; set; }
        public DateTime data { get; set; }
        public int LocalizacaoId { get; set; }

        public virtual Localizacao Localizacao { get; set; }
    }
}