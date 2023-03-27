using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PeopleMarket.Models;
using System.Diagnostics;

namespace PeopleMarket.Controllers
{
    // [Authorize]
    public class ClienteController : Controller
    {
        private readonly ILogger<ClienteController> _logger;

        public ClienteController(ILogger<ClienteController> logger)
        {
            _logger = logger;
        }

        public IActionResult Perfil()
        {
            return View();
        }

        public IActionResult Cesto()
        {
            return View();
        }

        public IActionResult Checkout()
        {
            return View();
        }
    }
}