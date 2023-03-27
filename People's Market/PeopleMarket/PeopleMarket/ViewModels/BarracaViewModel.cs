using PeopleMarket.Models;

namespace PeopleMarket.ViewModels
{
    public class BarracaViewModel
    {
        public List<Produto> produtos;
        public int idBarraca { get; set; }
        public string nomeBarraca { get; set; }
        public string tema { get; set; }
        public int classificacao { get; set; }
    }
}