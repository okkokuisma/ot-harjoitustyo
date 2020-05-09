# Arkkitehtuurikuvaus

## Rakenne
Sovelluksen rakenne on toteutettu kolmistasoista kerrosarkkitehtuuria noudattaen. Pakkaus kurssihallinta.ui sisältää sovelluksen JavaFX:llä toteutetun graafisen käyttöliittymän, kurssihallinta.domain sisältää sovelluksen sovelluslogiikan ja kurssihallinta.dao sisältää koodin, joka vastaa datan pysyväistallennuksesta.

![Pakkausrakenne](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/pakkausrakenne.png)

## Käyttöliittymä
Sovelluksen käyttöliittymä rakentuu kolmesta päänäkymästä, jotka on toteutettu omina Scene-olioinaan. Käyttöliittymän ylälaidassa pysyy näkymästä huolimatta navigointipalkki, jonka nappeja painamalla käyttäjä voi liikkua näkymästä toiseen, jolloin kyseinen Scene-olio sijoitetaan ohjelman ensisijaisen Stage-olion lapseksi. Päänäkymillä on myös useampia alinäkymiä, joista osa on toteutettu suoraan sovelluksen start-metodiin ja osa on eriytetty omiksi metodeikseen. Oppituntien lisäämisnäkymä on toteutettu omana Stage-olionaan, joten se aukeaa omaan ikkunaansa.

Sovelluksen varsinainen sovelluslogiikka on eriytetty käyttöliittymästä KurssihallintaService-luokalle.

## Sovelluslogiikka
Sovelluksen loogisen datamallin luovat luokat Course, Student, Lesson ja Classroom. Luokilla on metodeinaan vain yksinkertaiset kontruktorit sekä getteri- ja setterimetodit, ja niiden tehtävänä onkin toimia vain yksittäisten kurssi-, luokka- oppitunti- tai luokkatapausten tallennusobjekteina. Lesson-olio saa oliomuuttujana yhden Course-olion, mikä on ainoa näiden luokkien välinen suora riippuvuus.

![domainluokat](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/domainluokat.png)

Sovelluslogiikkaa pyörittää KurssihallintaService-luokka, joka vastaa sovelluksen toiminnoista ja DAO-luokkien metodien kutsuista. Sovelluksessa on käytössä yksi ainoa KurssihallintaService-olio, jonka käyttöliittymästä vastaava KurssihallitaUi-luokka saa oliomuuttujanaan. KurssihallintaServicen metodeja kutsutaan käyttöliittymän tapahtumakäsittelijöissä eli kun käyttäjä on vuorovaikutuksessa käyttöliittymän komponenttien kanssa. 

KurssihallintaService saa oliomuuttujina kurssihallinta.dao-pakkauksen luokat CourseDao, StudentDao, RegistrationDao, LessonDao ja ClassroomDao, joiden avulla se pääsee lukemaan ja muokkaamaan tietokantaan tallennettua dataa.  

![Pakkauskaavio](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/pakkauskaavio.png)

## Tietojen pysyväistallennus
Tietojen pysyväistallennus on pyritty toteuttamaan Data Access Object -mallia noudattaen. Jokaiselle loogisen datamallin luokalle on olemassa oma DAO-luokkansa ja taulunsa tietokannassa. Näiden lisäksi sovelluksessa on myös tietokannan Registrations-taulun, eli kurssi-ilmoittautumisten, lukemisesta ja muokkaamisesta vastaava luokka RegistrationDao.

DAO-luokkien lisäksi kurssihallinta.dao-pakkauksessa on DatabaseUtil-luokka, joka vastaa tietokannan luomisesta, jos sitä ei ole vielä käynnityshetkellä olemassa, ja DAO-luokkien ja tietokannan välisten yhteyksien muodostamisesta. Jokaista sovelluksen ja tietokannan välistä tapahtumaa varten luodaan uusi yhteys, jotta yhteyksiä on auki aina vain yksi kerrallaan, mikä pienentää riskiä tietokannan lukkiutumisesta.

Sovellus tarvitsee toimiakseen tietokantatiedoston, jonka se luo automaattisesti käynnistettäessä ensimmäisen kerran sovelluksen juurikansioon. Käyttäjän on mahdollista vaihtaa sovelluksen käyttämää tietokantatiedostoa juurikansiossa sijaitsevan config.properties-tiedoston avulla. Jos konfigurointitiedostoon kirjattua tietokantatiedostoa ei ole olemassa sovelluksen käynnistyshetkellä, luo sovellus juurikansioon uuden tiedoston.

### Tietokanta
Sovellus käyttää tietokantajärjestelmänä SQLiteä. Tietokanta muodostuu viidestä taulusta, Courses-, Students-, Classrooms-, Lessons- ja Registrations-tauluista, joista tauluissa Lessons ja Registrations viitataan toisiin tauluihin niiden id-avainten avulla. Tiedon eheyttä on pyritty ylläpitämään joillain yksittäisillä UNIQUE-ehdoilla.

Tietokannan SQL-skeema on vastaava:
`CREATE TABLE Courses (id INTEGER PRIMARY KEY, name TEXT UNIQUE, startdate TEXT, enddate TEXT, teacher TEXT, students INTEGER, max_students INTEGER);
CREATE TABLE Students (id INTEGER PRIMARY KEY, first_name TEXT, surname TEXT, id_number TEXT UNIQUE, address TEXT, zip TEXT, city TEXT, country TEXT, email TEXT);
CREATE TABLE Classrooms (id INTEGER PRIMARY KEY, name TEXT UNIQUE);
CREATE TABLE Registrations (id INTEGER PRIMARY KEY, course_id INTEGER, student_id INTEGER);
CREATE TABLE Lessons (id INTEGER PRIMARY KEY, course_id INTEGER, classroom_id INTEGER, date TEXT, starttime TEXT, endtime TEXT);`

![Sekvenssikaavio](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/sekvenssikaavio_kurssilisays.png)
