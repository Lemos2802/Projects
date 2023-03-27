using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Routing;
using Microsoft.EntityFrameworkCore;
using PeopleMarket.Models;
using PeopleMarket.ViewModels;

namespace PeopleMarket.Data.Repository
{
    public class Repository : IRepository
    {
        private AppDbContext _context;
        private LinkGenerator _linkGenerator;

        public Repository(AppDbContext context,
            LinkGenerator linkGenerator)
        {
            _context = context;
            _linkGenerator = linkGenerator;
        }

        public async Task<List<Barraca>> GetBarracasAsync(string idFeira)
        {
            return await _context.Barraca.Where(fs => fs.FeiraId.Equals(idFeira)).ToListAsync();
        }
    }
}