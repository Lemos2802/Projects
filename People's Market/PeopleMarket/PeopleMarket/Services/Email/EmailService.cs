using System;
using System.Net;
using System.Net.Mail;
using System.Threading.Tasks;
using Microsoft.Extensions.Options;
using PeopleMarket.Configuration;
using RestSharp;
using RestSharp.Authenticators;

namespace PeopleMarket.Services.Email
{
    public class EmailService : IEmailService
    {
        private readonly SmtpSettings _settings;
        private readonly SmtpClient _client;

        public EmailService(IOptions<SmtpSettings> options)
        {
            _settings = options.Value;
            _client = new SmtpClient(_settings.Server)
            {
                Credentials = new NetworkCredential(_settings.Username, _settings.Password)
            };
        }

        public Task SendEmail(string email, string subject, string message)
        {
            var mailMessage = new MailMessage(
                _settings.From,
                email,
                subject,
                message);

            return _client.SendMailAsync(mailMessage);
        }
    }
}