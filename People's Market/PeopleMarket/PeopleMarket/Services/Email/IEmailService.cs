using System.Threading.Tasks;

namespace PeopleMarket.Services.Email
{
    public interface IEmailService
    {
        Task SendEmail(string email, string subject, string message);
    }
}