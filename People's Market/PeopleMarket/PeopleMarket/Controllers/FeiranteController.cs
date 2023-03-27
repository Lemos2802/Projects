using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PeopleMarket.Models;
using System.Diagnostics;

namespace PeopleMarket.Controllers
{
    // [Authorize]
    public class FeiranteController : Controller
    {
        private readonly ILogger<FeiranteController> _logger;

        public FeiranteController(ILogger<FeiranteController> logger)
        {
            _logger = logger;
        }

        public IActionResult Perfil()
        {
            return View();
        }

        public IActionResult AddBarraca()
        {
            return View();
        }

        public IActionResult AddProduto()
        {
            return View();
        }
    }
}