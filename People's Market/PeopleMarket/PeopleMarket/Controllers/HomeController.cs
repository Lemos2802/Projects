using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PeopleMarket.Models;
using System.Diagnostics;
using PeopleMarket.Data;
using Microsoft.AspNetCore.Identity;
using PeopleMarket.Data.Repository;
using Microsoft.EntityFrameworkCore;

namespace PeopleMarket.Controllers
{
    //[Authorize]
    public class HomeController : Controller
    {
        private readonly AppDbContext _context;
        private readonly UserManager<Cliente> _userManager;
        private readonly IRepository _repository;

        public HomeController(AppDbContext context,
                            UserManager<Cliente> userManager,
                            IRepository repository
        )
        {
            _context = context;
            _userManager = userManager;
            _repository = repository;
        }

        public async Task<IActionResult> Index()
        {
            return View();
        }

        public IActionResult Termos()
        {
            return View();
        }

        public IActionResult Contactos()
        {
            return View();
        }

        public IActionResult Privacy()
        {
            return View();
        }

        public IActionResult Feira()
        {
<<<<<<< HEAD
            return View();
=======
            // if (idFeira == null) return NotFound();
            return View(await _context.Barraca.FirstOrDefaultAsync());
>>>>>>> refs/remotes/origin/main
        }

        public async Task<IActionResult> Barraca()
        {
            return View(await _context.Produto.FirstOrDefaultAsync());
        }

        public async Task<IActionResult> Produto()
        {
            return View(await _context.Produto.ToListAsync());
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}