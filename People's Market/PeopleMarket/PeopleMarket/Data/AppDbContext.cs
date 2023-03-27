using PeopleMarket.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
namespace PeopleMarket.Data
{
    public class AppDbContext : IdentityDbContext<Cliente>
    {
        public AppDbContext(DbContextOptions<AppDbContext> options)
            :base(options)
        {}
        public DbSet<Feirante> Feirante  { get; set; }
        public DbSet<Barraca> Barraca  { get; set; }
        public DbSet<Feira> Feira  { get; set; }
        public DbSet<Produto> Produto  { get; set; }
        public DbSet<Cesto> Cesto  { get; set; }
        public DbSet<Comentario> Comentario  { get; set; }
        public DbSet<Historico> Historico  { get; set; }
        public DbSet<Localizacao> Localizacao  { get; set; }
        public DbSet<ListaHistoricoProdutos> ListaHistoricoProdutos { get; set; }
    }
}