SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema PeopleMarket
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema PeopleMarket
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `PeopleMarket` DEFAULT CHARACTER SET utf8 ;
USE `PeopleMarket` ;

-- -----------------------------------------------------
-- Table `PeopleMarket`.`Feirante`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Feirante` (
  `id_Feirante` INT NOT NULL,
  `IBAN` VARCHAR(30) NOT NULL,
  `num_Cartao_Cidadao` VARCHAR(20) NOT NULL,
  `Descricao` VARCHAR(200) NULL,
  PRIMARY KEY (`id_Feirante`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Localizacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Localizacao` (
  `id_Localizacao` INT NOT NULL,
  `nome_Distrito` VARCHAR(45) NOT NULL,
  `nome_Concelho` VARCHAR(45) NOT NULL,
  `Imagem` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_Localizacao`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Feira`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Feira` (
  `id_Feira` INT NOT NULL,
  `num_Barracas` INT NOT NULL,
  `Classificacao` INT NULL,
  `Data` DATETIME NOT NULL,
  `Periodo` INT NOT NULL,
  `Imagem` VARCHAR(45) NOT NULL,
  `Localizacao_id_Localizacao` INT NOT NULL,
  PRIMARY KEY (`id_Feira`, `Localizacao_id_Localizacao`),
  INDEX `fk_Feira_Localizacao1_idx` (`Localizacao_id_Localizacao` ASC) VISIBLE,
  CONSTRAINT `fk_Feira_Localizacao1`
    FOREIGN KEY (`Localizacao_id_Localizacao`)
    REFERENCES `PeopleMarket`.`Localizacao` (`id_Localizacao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Barraca`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Barraca` (
  `id_Barraca` INT NOT NULL,
  `nome_Barraca` VARCHAR(45) NOT NULL,
  `Tema` VARCHAR(45) NOT NULL,
  `Imagem` VARCHAR(45) NOT NULL,
  `Classificacao` INT NULL,
  `Feira_id_Feira` INT NOT NULL,
  `Feirante_id_Feirante` INT NOT NULL,
  `Localizacao_id_Localizacao` INT NOT NULL,
  PRIMARY KEY (`id_Barraca`, `Feira_id_Feira`, `Feirante_id_Feirante`, `Localizacao_id_Localizacao`),
  INDEX `fk_Barraca_Feirante1_idx` (`Feirante_id_Feirante` ASC) VISIBLE,
  INDEX `fk_Barraca_Localizacao1_idx` (`Localizacao_id_Localizacao` ASC) VISIBLE,
  INDEX `fk_Barraca_Feira1_idx` (`Feira_id_Feira` ASC) VISIBLE,
  CONSTRAINT `fk_Barraca_Feirante1`
    FOREIGN KEY (`Feirante_id_Feirante`)
    REFERENCES `PeopleMarket`.`Feirante` (`id_Feirante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Barraca_Localizacao1`
    FOREIGN KEY (`Localizacao_id_Localizacao`)
    REFERENCES `PeopleMarket`.`Localizacao` (`id_Localizacao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Barraca_Feira1`
    FOREIGN KEY (`Feira_id_Feira`)
    REFERENCES `PeopleMarket`.`Feira` (`id_Feira`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Produto` (
  `id_Produto` INT NOT NULL,
  `nome_Produto` VARCHAR(45) NOT NULL,
  `Descricao` VARCHAR(200) NOT NULL,
  `Preço` FLOAT NOT NULL,
  `Stock` INT NOT NULL,
  `Imagem` VARCHAR(45) NOT NULL,
  `Barraca_id_Barraca` INT NOT NULL,
  PRIMARY KEY (`id_Produto`, `Barraca_id_Barraca`),
  INDEX `fk_Produto_Barraca1_idx` (`Barraca_id_Barraca` ASC) VISIBLE,
  CONSTRAINT `fk_Produto_Barraca1`
    FOREIGN KEY (`Barraca_id_Barraca`)
    REFERENCES `PeopleMarket`.`Barraca` (`id_Barraca`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Cesto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Cesto` (
  `id_Cesto` INT NOT NULL,
  PRIMARY KEY (`id_Cesto`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Cliente` (
  `id_CLiente` INT NOT NULL,
  `nome_Cliente` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `Morada` VARCHAR(100) NOT NULL,
  `Telemovel` VARCHAR(9) NOT NULL,
  `Cesto_id_Cesto` INT NOT NULL,
  `Feirante_id_Feirante` INT NOT NULL,
  PRIMARY KEY (`id_CLiente`, `Cesto_id_Cesto`, `Feirante_id_Feirante`),
  INDEX `fk_Cliente_Cesto1_idx` (`Cesto_id_Cesto` ASC) VISIBLE,
  INDEX `fk_Cliente_Feirante1_idx` (`Feirante_id_Feirante` ASC) VISIBLE,
  CONSTRAINT `fk_Cliente_Cesto1`
    FOREIGN KEY (`Cesto_id_Cesto`)
    REFERENCES `PeopleMarket`.`Cesto` (`id_Cesto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cliente_Feirante1`
    FOREIGN KEY (`Feirante_id_Feirante`)
    REFERENCES `PeopleMarket`.`Feirante` (`id_Feirante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Historico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Historico` (
  `id_Historico` INT NOT NULL,
  `Metodo_Pagamento` VARCHAR(45) NOT NULL,
  `Cliente_id_CLiente` INT NOT NULL,
  PRIMARY KEY (`id_Historico`, `Cliente_id_CLiente`),
  INDEX `fk_Checkout_Cliente1_idx` (`Cliente_id_CLiente` ASC) VISIBLE,
  CONSTRAINT `fk_Checkout_Cliente1`
    FOREIGN KEY (`Cliente_id_CLiente`)
    REFERENCES `PeopleMarket`.`Cliente` (`id_CLiente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Comentario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Comentario` (
  `id_Comentario` INT NOT NULL,
  `Texto` VARCHAR(45) NOT NULL,
  `Cliente_id_CLiente` INT NOT NULL,
  `Barraca_id_Barraca` INT NOT NULL,
  PRIMARY KEY (`id_Comentario`, `Cliente_id_CLiente`, `Barraca_id_Barraca`),
  INDEX `fk_Comentario_Cliente1_idx` (`Cliente_id_CLiente` ASC) VISIBLE,
  INDEX `fk_Comentario_Barraca1_idx` (`Barraca_id_Barraca` ASC) VISIBLE,
  CONSTRAINT `fk_Comentario_Cliente1`
    FOREIGN KEY (`Cliente_id_CLiente`)
    REFERENCES `PeopleMarket`.`Cliente` (`id_CLiente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comentario_Barraca1`
    FOREIGN KEY (`Barraca_id_Barraca`)
    REFERENCES `PeopleMarket`.`Barraca` (`id_Barraca`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Barraca_has_Tema`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Barraca_has_Tema` (
  `Barraca_id_Barraca` INT NOT NULL,
  `Barraca_Cliente_Feirante_Cliente_Distrito_idDistrito` INT NOT NULL,
  `Barraca_Cliente_Feirante_Feirante_id_Feirante` INT NOT NULL,
  `Barraca_Feira_id_Feira` INT NOT NULL,
  `Barraca_Cliente_Feirante_Cliente_id_CLiente` INT NOT NULL,
  `Tema_id_Tema` INT NOT NULL,
  PRIMARY KEY (`Barraca_id_Barraca`, `Barraca_Cliente_Feirante_Cliente_Distrito_idDistrito`, `Barraca_Cliente_Feirante_Feirante_id_Feirante`, `Barraca_Feira_id_Feira`, `Barraca_Cliente_Feirante_Cliente_id_CLiente`, `Tema_id_Tema`),
  INDEX `fk_Barraca_has_Tema_Barraca1_idx` (`Barraca_id_Barraca` ASC, `Barraca_Cliente_Feirante_Cliente_Distrito_idDistrito` ASC, `Barraca_Cliente_Feirante_Feirante_id_Feirante` ASC, `Barraca_Feira_id_Feira` ASC, `Barraca_Cliente_Feirante_Cliente_id_CLiente` ASC) VISIBLE,
  CONSTRAINT `fk_Barraca_has_Tema_Barraca1`
    FOREIGN KEY (`Barraca_id_Barraca`)
    REFERENCES `PeopleMarket`.`Barraca` (`id_Barraca`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Cesto_has_Produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Cesto_has_Produto` (
  `Cesto_id_Cesto` INT NOT NULL,
  `Produto_id_Produto` INT NOT NULL,
  `Quantidade` INT NOT NULL,
  PRIMARY KEY (`Cesto_id_Cesto`, `Produto_id_Produto`),
  INDEX `fk_Cesto_has_Produto_Produto1_idx` (`Produto_id_Produto` ASC) VISIBLE,
  INDEX `fk_Cesto_has_Produto_Cesto1_idx` (`Cesto_id_Cesto` ASC) VISIBLE,
  CONSTRAINT `fk_Cesto_has_Produto_Cesto1`
    FOREIGN KEY (`Cesto_id_Cesto`)
    REFERENCES `PeopleMarket`.`Cesto` (`id_Cesto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cesto_has_Produto_Produto1`
    FOREIGN KEY (`Produto_id_Produto`)
    REFERENCES `PeopleMarket`.`Produto` (`id_Produto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PeopleMarket`.`Historico_has_Produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PeopleMarket`.`Historico_has_Produto` (
  `Historico_id_Historico` INT NOT NULL,
  `Produto_id_Produto` INT NOT NULL,
  `Quantidade` INT NOT NULL,
  PRIMARY KEY (`Historico_id_Historico`, `Produto_id_Produto`),
  INDEX `fk_Historico_has_Produto_Produto1_idx` (`Produto_id_Produto` ASC) VISIBLE,
  INDEX `fk_Historico_has_Produto_Historico1_idx` (`Historico_id_Historico` ASC) VISIBLE,
  CONSTRAINT `fk_Historico_has_Produto_Historico1`
    FOREIGN KEY (`Historico_id_Historico`)
    REFERENCES `PeopleMarket`.`Historico` (`id_Historico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Historico_has_Produto_Produto1`
    FOREIGN KEY (`Produto_id_Produto`)
    REFERENCES `PeopleMarket`.`Produto` (`id_Produto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
