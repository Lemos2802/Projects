using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace PeopleMarket.Models
{
    public class Barraca
    {
        public int Id { get; set; }
        [Required]
        [StringLength(45)]
        [DisplayName("Nome da Barraca")]
        public string nomeBarraca { get; set; }
        [Required] 
        [StringLength(45)]
        [DisplayName("Tema")]
        public string tema { get; set; }
        [Required] 
        [StringLength(45)]
        [DisplayName("Classificação")]
        public int classificacao { get; set; }
        public int FeiranteId { get; set; }
        public string FeiraId { get; set; }

        public virtual Feirante Feirante { get; set; }
        public virtual Feira Feira { get; set; }
    }
}