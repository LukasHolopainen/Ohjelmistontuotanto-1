CREATE DATABASE IF NOT EXISTS tietokanta_mokki;

CREATE TABLE IF NOT EXISTS mokki (
mokkiID INT	PRIMARY KEY,
mokkiNimi VARCHAR(30),
nukkumaPaikat INT,
huoneet INT ,
osoite VARCHAR(30),
hintaPerPaiva FLOAT 
);

CREATE TABLE IF NOT EXISTS asiakas (
asiakasID INT PRIMARY KEY,
etuNimi VARCHAR(30),
sukuNimi VARCHAR(30),
puhelinNumero INT,
sahkoPostiOsoite VARCHAR(50),
osoite VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS varaus(
varausID INT PRIMARY KEY,
kesto INT,
mokkiID INT,
asiakasID INT,
FOREIGN KEY (mokkiID) REFERENCES mokki(mokkiID),
FOREIGN KEY (asiakasID) REFERENCES asiakas(asiakasID)
);

CREATE TABLE IF NOT EXISTS lasku(
laskuID INT PRIMARY KEY,
varausID INT,
hinta FLOAT,
FOREIGN KEY (varausID) REFERENCES varaus(varausID)
);

INSERT INTO mokki (mokkiID, mokkiNimi, nukkumaPaikat, huoneet, osoite, hintaPerPaiva) VALUES (000, 'Revontulen Ratsastaja', 6, 3, 'Katukujantie 99', 98.76);
INSERT INTO mokki (mokkiID, mokkiNimi, nukkumaPaikat, huoneet, osoite, hintaPerPaiva) VALUES (001, 'Villa Crocodilo', 8, 5, 'Skibidinkatu 420', 199.99);
INSERT INTO mokki (mokkiID, mokkiNimi, nukkumaPaikat, huoneet, osoite, hintaPerPaiva) VALUES (002, 'Tralaleron Tila', 10, 8, 'Metsämökkitie 33', 299.95);
INSERT INTO mokki (mokkiID, mokkiNimi, nukkumaPaikat, huoneet, osoite, hintaPerPaiva) VALUES (003, 'Cocofanton Cartano',  4, 4, 'Kappucinokuja 1', 79.99);
INSERT INTO mokki (mokkiID, mokkiNimi, nukkumaPaikat, huoneet, osoite, hintaPerPaiva) VALUES (004, 'Meemimökki', 2, 1, 'Kauppiaankatu 22', 14.99);

INSERT INTO asiakas (asiakasID, etuNimi, sukuNimi, puhelinNumero, sahkoPostiOsoite, osoite) VALUES (111, 'Epa', 'Esimerkki', 0401231234, 'epa.esimerkki@mail.com', 'Kissankatu 7');
INSERT INTO asiakas (asiakasID, etuNimi, sukuNimi, puhelinNumero, sahkoPostiOsoite, osoite) VALUES (222, 'Maija', 'Maijanen', 0402342345, 'maija.maija@mail.com', 'Vilkkuväylä 99');
INSERT INTO asiakas (asiakasID, etuNimi, sukuNimi, puhelinNumero, sahkoPostiOsoite, osoite) VALUES (333, 'Seppo', 'Salainen', 0403453456, 'sexisepi@mail.com', 'Kaunolanraitti 3');