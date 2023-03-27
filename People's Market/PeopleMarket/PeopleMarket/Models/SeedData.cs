using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using PeopleMarket.Data;

namespace PeopleMarket.Models
{
    public class SeedData
    {
        public static void Initialize(IServiceProvider serviceProvider)
        {
            using (var context = new AppDbContext(
                serviceProvider.GetRequiredService<
                    DbContextOptions<AppDbContext>>()))
            {

                if (context.Feirante.Any())
                {
                    return;   // DB has been seeded
                }

                Localizacao BragaLoc = new Localizacao
                {
                    distrito = "Braga",
                    concelho = "Braga",
                    pathImage = "braga_1.png"
                };
                context.Localizacao.Add(BragaLoc);

                Feirante Orlando = new Feirante
                {
                    iban = "PT123456789",
                    numCC = "123456789",
                    descricao = "Maior Vendedor de Sempre",
                    ClienteUserName = "Belo"
                };
                context.Feirante.Add(Orlando);
                context.SaveChanges();

                Feira Braga = new Feira
                {
                    Id = "Braga",
                    nBarracas = 1,
                    classificacao = 4,
                    periodo = 24,
                    pathImage = "braga_1.png",
                    LocalizacaoId = BragaLoc.Id,
                    data = new DateTime(2023, 3, 1)
                };
                context.Feira.Add(Braga);

                Barraca acacia = new Barraca
                {
                    nomeBarraca = "Barraca da Dona Acácia",
                    tema = "roupa",
                    classificacao = 4,
                    FeiraId = "Braga",
                    FeiranteId = Orlando.Id
                };
                context.Barraca.Add(acacia);
                context.SaveChanges();

                Produto casaco = new Produto
                {
                    nome = "Casaco Camurça Masculino",  
                    descricao = "Com a aliança da camurça suave, este blazer será o principal da sua toilette de inverno. - Casaco de camurça suave. - Interior forrado a malha sherpa. - Abertura com 3 botões grandes de fantasia.",
                    preco = 23.47,
                    stock = 1,
                    pathImage = "casaco.png",
                    BarracaId = acacia.Id
                };
                
                Produto saia = new Produto
                {
                    nome = "Saia de malha",  
                    descricao = "Saia de cintura subida com cintura elástica. Bainha em evasé. Fecho atrás com fecho de correr metálico.",
                    preco = 22.95,
                    stock = 2,
                    pathImage = "saia.png",
                    BarracaId = acacia.Id
                };

                Produto casacoCabedal = new Produto
                {
                    nome = "Casaco de cabedal masculino",  
                    descricao = "Blusão em pele com capuz e carcela amovível com fecho, confecionado com uma pele maleável e confortável, o forro é em algodão bem quente e tem dois bolsos interiores. Um blusão jovem e informal.",
                    preco = 50.99,
                    stock = 3,
                    pathImage = "cabedal.png",
                    BarracaId = acacia.Id
                };

                Produto maca = new Produto
                {
                    nome = "Maçã Golden",  
                    descricao = "A maçã mais saborosa do país",
                    preco = 1.50,
                    stock = 30,
                    pathImage = "maca.png",
                    BarracaId = acacia.Id
                };

                Produto pera = new Produto
                {
                    nome = "Pêra Rocha",  
                    descricao = "A pêra mais fresca e suculenta",
                    preco = 1.27,
                    stock = 40,
                    pathImage = "pera.png",
                    BarracaId = acacia.Id
                };

                Produto laranja = new Produto
                {
                    nome = "Laranja do Algarve",  
                    descricao = "Laranja fresca e da melhor qualidade",
                    preco = 2.10,
                    stock = 35,
                    pathImage = "laranja.png",
                    BarracaId = acacia.Id
                };
                List<Produto> produtos = new List<Produto> {casaco, saia, casacoCabedal, maca, pera, maca, laranja};
                context.Produto.AddRange(produtos);

                context.SaveChanges();

                var userMgr = serviceProvider.GetRequiredService<UserManager<Cliente>>();

                Cliente ToGi = new Cliente
                {
                    UserName = "ToGi",
                    nomeApelido = "Antonio Afonso",
                    Password = "Asd2002",
                    Email = "togi@togi.com",
                    morada = "asd, riba de mouro",
                    telemovel = "927754333",
                    FeiranteId = null
                };
                userMgr.CreateAsync(ToGi, ToGi.Password).GetAwaiter().GetResult();
                context.SaveChanges();

                Cliente Belo = new Cliente
                {
                    UserName = "Belo",
                    nomeApelido = "Orlando Belo",
                    Password = "Orlando",
                    Email = "orlandobelo@gmail.com",
                    morada = "Rua do Bela Vista",
                    telemovel = "965375283",
                    FeiranteId = Orlando.Id
                };
                userMgr.CreateAsync(Belo, Belo.Password).GetAwaiter().GetResult();
                context.SaveChanges();

                // creating the root user
                var roleMgr = serviceProvider.GetRequiredService<RoleManager<IdentityRole>>();

                var adminRole = new IdentityRole("Admin");
                var rootRole = new IdentityRole("Root");

                //create a role
                roleMgr.CreateAsync(adminRole).GetAwaiter().GetResult();
                roleMgr.CreateAsync(rootRole).GetAwaiter().GetResult();

                //create the root user
                Cliente Admin = new Cliente
                {
                    UserName = "Admin",
                    nomeApelido = "BaseAdmin",
                    Password = "Adminn",
                    Email = "admin@admin.com",
                    morada = "Admin",
                    telemovel = "965375283",
                    FeiranteId = null
                };

                var result = userMgr.CreateAsync(Admin, "Adminn").GetAwaiter().GetResult();
                Console.WriteLine(result.Succeeded);

                userMgr.AddToRoleAsync(Admin, adminRole.Name).GetAwaiter().GetResult();
                userMgr.AddToRoleAsync(Admin, rootRole.Name).GetAwaiter().GetResult();

                context.SaveChanges();
                Console.WriteLine("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            }
            
        }
    }
}
