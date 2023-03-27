using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using PeopleMarket.Models;
using PeopleMarket.ViewModels;

namespace PeopleMarket.Data.Repository
{ 
    public interface IRepository
    {

        /* Devolve as barracas pertencentes a uma certa feira */
        Task<List<Barraca>> GetBarracasAsync(string idFeira);
    }
}