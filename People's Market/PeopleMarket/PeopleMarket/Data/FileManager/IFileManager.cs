using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using PeopleMarket.Models;
using PeopleMarket.ViewModels;

namespace PeopleMarket.Data.FileManager
{
    public interface IFileManager
    {
        Task<string> SaveImage(IFormFile image);
        FileStream ImageStream(string image);
        void RemoveImage(string image);
    }
}