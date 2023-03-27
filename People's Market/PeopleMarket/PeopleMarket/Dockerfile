FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["PeopleMarket/PeopleMarket.csproj", "PeopleMarket/"]
RUN dotnet restore "PeopleMarket/PeopleMarket.csproj"
COPY . .
WORKDIR "/src/PeopleMarket"
RUN dotnet build "PeopleMarket.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "PeopleMarket.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "PeopleMarket.dll"]