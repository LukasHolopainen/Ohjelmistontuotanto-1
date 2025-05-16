Ohjelmistotuotanto 1 -kurssin lopputyö, kirjoittajat Lukas Holopainen, Jarno Maksimainen, Mirko Kaukonen, Antti Eronen

Ohjelmisto on yksinkertainen mökinvarausjärjestelmä, joka on tarkoitettu mökkifirman työntekijöiden käyttöön.

Ohjelmaa on mahdollista ajaa, kun kaikki ohjelman luokat ovat samassa packagessa ja tietokantaan on onnistuneesti yhdistetty. Ajaminen tapahtuu käynnistämällä mokkiGUI-JavaFX-applikaatio.

SQL-tietokantaa käyttääkseen täytyy olla käytössä omalla localhostilla oleva MySQL. Yhdistääkseen tietokannan Javaan, vaaditaan JDBC-ajurin asennus.
VarauksenTallennus-luokan riviltä 9 alkaen on url- (muodossa esim. "jdbc:mysql://localhost:3306/tietokanta_mokki"), käyttäjä- ja salasana-muuttujat, johon täytyy 
kirjoittaa oma MySQL URL-osoite, käyttäjänimi ja salasana, jotta Java-koodi saa tietokantaan yhteyden. 
