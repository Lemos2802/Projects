using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PeopleMarket.Data;
using PeopleMarket.Models;
using PeopleMarket.ViewModels;

namespace PeopleMarket.Controllers
{
    public class AuthController : Controller
    {
        private readonly AppDbContext _context;
        private readonly SignInManager<Cliente> _signInManager;
        private readonly UserManager<Cliente> _userManager;

        public AuthController(AppDbContext context,SignInManager<Cliente> signInManager,
            UserManager<Cliente> userManager)
        {
            _context = context;
            _signInManager = signInManager;
            _userManager = userManager;
        }
        
        [HttpGet]
        public IActionResult Login()
        {
            return View(new LoginViewModel());
        }

        [HttpPost]
        public async Task<IActionResult> Login(LoginViewModel vm)
        {
            var user = await _userManager.FindByNameAsync(vm.Username);
            if (user == null)
            {
                user = await _userManager.FindByEmailAsync(vm.Username);
            }

            if (user != null)
            {
                var result = await _signInManager.PasswordSignInAsync(user, vm.Password, false, false);
                if (result.Succeeded)
                {
                    return RedirectToAction("Index", "Home");
                }
            }

            ModelState.AddModelError(string.Empty, "Invalid login attempt.");
            return View(vm);
        }

        [HttpGet]
        public IActionResult Register()
        {
            return View(new RegisterViewModel());
        }

        [HttpPost]
        public async Task<IActionResult> Register(RegisterViewModel vm)
        {
            if (!ModelState.IsValid)
            {
                return View(vm);
            }

            var user = new Cliente
            {
                UserName = vm.Username,
                nomeApelido = vm.NomeCompleto,
                Password = vm.Password,
                Email = vm.Email,
                morada = vm.Morada,
                telemovel = vm.PhoneNumber,
                FeiranteId = null,
            };

            var result = await _userManager.CreateAsync(user, vm.Password);
            if (!result.Succeeded) return View(vm);

            await _signInManager.SignInAsync(user, false);
            return RedirectToAction("Index", "Home");
        }
        
        [HttpGet]
        public IActionResult RegisterFeirante()
        {
            return View(new RegisterFeiranteViewModel());
        }

        [HttpPost]
        public async Task<IActionResult> RegisterFeirante(RegisterFeiranteViewModel vm)
        {
            if (!ModelState.IsValid)
            {
                return View(vm);
            }

            var user = new Cliente
            {
                UserName = vm.Username,
                nomeApelido = vm.NomeCompleto,
                Password = vm.Password,
                Email = vm.Email,
                morada = vm.Morada,
                telemovel = vm.PhoneNumber,
            };

            var feirante = new Feirante
            {
                iban = vm.Iban,
                numCC = vm.CC,
                ClienteUserName = vm.Username
            };
            await _context.AddAsync(feirante);
            await _context.SaveChangesAsync();
            user.FeiranteId = feirante.Id;
            await _context.AddAsync(user);

            await _context.SaveChangesAsync();
            await _signInManager.SignInAsync(user, false);
            return RedirectToAction("Index", "Home");
        }

        [HttpGet]
        public async Task<IActionResult> Logout()
        {
            await _signInManager.SignOutAsync();

            return RedirectToAction("Login", "Auth");
        }
    }
}