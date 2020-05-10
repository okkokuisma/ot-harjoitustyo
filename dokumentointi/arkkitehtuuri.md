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

Sovelluslogiikka parin päätoiminnallisuuden takana sekvenssikaavioin:

![Sekvenssikaavio](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/sekvenssikaavio_kurssilisays.png)

Kuvassa esitettynä uuden kurssin lisääminen tietokantaan. Alussa käyttäjä navigoi käyttöliittymässä lisäysnäkymään. Kurssitietojen kirjaamisen ja lisäyspainikkeen painamisen myötä käyttöliittymä luo uuden Course-olion ja kutsuu KurssihallintaServicen addCourse-metodia uusi Course-olio parametrina. KurssihallintaService taas kutsuu CourseDao:n add-metodia ja palauttaa käyttöliittymälle true tai false sen mukaan, onko lisäys tietokantaan onnistunut. Käyttöliittymä välittää tiedon käyttäjälle.

![Sekvenssikaavio](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/registrationsekvenssi.png)

Tässä esitettynä uuden kurssi-ilmoittautumisen lisääminen tietokantaan tapauksessa, jossa ilmoittautumisen yhteydessä lisätään myös uusi opiskelija tietokantaan. Opiskelijan lisääminen tapahtuu samaan tapaan kuin edellä esitetty kurssin lisääminen. Kun opiskelija on lisätty, käyttöliittymä kutsuu omaa metodiaan addRegistrationView parametrina juuri luotu Student-olio. Tässä kurssi-ilmoittautumisten lisäysnäkymässä käyttäjä ensin hakee hakupalkkia hyödyntämällä kursseja tietokannasta, mikä tapahtuu kutsumalla KurssihallintaServicen searchCourses- ja CourseDao:n search-metodeita. Käyttäjälle palautetaan lista hakusanaa vastanneita kursseja. Käyttäjän valittua listasta haluamansa kurssin käyttöliittymä kutsuu KurssihallintaServicen addRegistration-metodia. Metodissa haetaan ensin kyseisen kurssin ja opiskelijan rivi-id:t CourseDao:n ja StudentDao:n getId-metodeilla, minkä jälkeen lopulta kutsutaan RegistrationDao:n add-metodia. Palautetaan true, jos lisäys on onnistunut.

## Tietojen pysyväistallennus
Tietojen pysyväistallennus on pyritty toteuttamaan Data Access Object -mallia noudattaen. Jokaiselle loogisen datamallin luokalle on olemassa oma DAO-luokkansa ja taulunsa tietokannassa. Näiden lisäksi sovelluksessa on myös tietokannan Registrations-taulun, eli kurssi-ilmoittautumisten, lukemisesta ja muokkaamisesta vastaava luokka RegistrationDao.

DAO-luokkien lisäksi kurssihallinta.dao-pakkauksessa on DatabaseUtil-luokka, joka vastaa tietokannan luomisesta, jos sitä ei ole vielä käynnityshetkellä olemassa, ja DAO-luokkien ja tietokannan välisten yhteyksien muodostamisesta. Jokaista sovelluksen ja tietokannan välistä tapahtumaa varten luodaan uusi yhteys, jotta yhteyksiä on auki aina vain yksi kerrallaan, mikä pienentää riskiä tietokannan lukkiutumisesta.

Sovellus tarvitsee toimiakseen tietokantatiedoston, jonka se luo automaattisesti käynnistettäessä ensimmäisen kerran sovelluksen juurikansioon. Käyttäjän on mahdollista vaihtaa sovelluksen käyttämää tietokantatiedostoa juurikansiossa sijaitsevan config.properties-tiedoston avulla. Jos konfigurointitiedostoon kirjattua tietokantatiedostoa ei ole olemassa sovelluksen käynnistyshetkellä, luo sovellus juurikansioon uuden tiedoston.

### Tietokanta
Sovellus käyttää tietokantajärjestelmänä SQLiteä. Tietokanta muodostuu viidestä taulusta, Courses-, Students-, Classrooms-, Lessons- ja Registrations-tauluista, joista tauluissa Lessons ja Registrations viitataan toisiin tauluihin niiden id-avainten avulla. Tiedon eheyttä on pyritty ylläpitämään joillain yksittäisillä UNIQUE-ehdoilla.

Tietokannan SQL-skeema on vastaava:

![Sekvenssikaavio](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/sqlskeema.png)
