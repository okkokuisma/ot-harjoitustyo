# Arkkitehtuurikuvaus

## Rakenne
Sovelluksen rakenne on toteutettu kolmistasoista kerrosarkkitehtuuria noudattaen. Pakkaus kurssihallinta.ui sisältää sovelluksen JavaFX:llä toteutetun graafisen käyttöliittymän, kurssihallinta.domain sisältää sovelluksen sovelluslogiikan ja kurssihallinta.dao sisältää koodin, joka vastaa datan pysyväistallennuksesta.

![Pakkausrakenne](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/pakkausrakenne.png)

## Käyttöliittymä
Sovelluksen käyttöliittymä rakentuu kolmesta päänäkymästä, jotka on toteutettu omina Scene-olioinaan. Käyttöliittymän ylälaidassa pysyy näkymästä huolimatta navigointipalkki, jonka nappeja painamalla käyttäjä voi liikkua näkymästä toiseen.

## Sovelluslogiikka
Sovelluksen loogisen datamallin luovat luokat Course, Student, Lesson ja Classroom.

![domainluokat](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/domainluokat.png)

Sovelluslogiikka on eriytetty käyttöliittymästä KurssihallintaService-luokalle, joka vastaa sovelluksen toiminnoista ja dao-luokkien metodien kutsuista.
KurssihallintaService saa oliomuuttujina kurssihallinta.dao-pakkauksen luokat CourseDao, StudentDao, RegistrationDao, LessonDao ja ClassroomDao, joiden avulla se pääsee käsiksi tietokantaan tallennettuun dataan ja voi muokata sitä. 

![Pakkauskaavio](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/pakkauskaavio.png)

## Tietojen pysyväistallennus
Tietojen pysyväistallennus on pyritty toteuttaa Data Access Object -mallia noudattaen. Sovellus tarvitsee toimiakseen tietokantatiedoston, jonka se luo automaattisesti käynnistettäessä ensimmäisen kerran sovelluksen juurikansioon. 

### Tietokanta
Sovellus käyttää tietokantajärjestelmänä SQLiteä.

![Sekvenssikaavio](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/sekvenssikaavio_kurssilisays.png)
