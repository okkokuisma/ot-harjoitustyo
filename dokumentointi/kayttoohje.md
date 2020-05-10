# Käyttöohje
[Lataa sovelluksen jar-tiedosto ja konfigurointitiedosto](https://github.com/okkokuisma/ot-harjoitustyo/releases/tag/loppupalautus)

## Sovelluksen konfigurointi
Sovellus olettaa, että sen juurikansiossa on käynnistyshetkellä tiedosto "config.properties", joka määrittelee käytettävän tietokantatiedoston nimen. Tämän konfigurointitiedoston avulla käyttäjän on mahdollista vaihtaa käytettävää tietokantatiedostoa. Jos tietokantatiedostoa ei ole olemassa käynnistyshetkellä, sovellus luo sen automaattisesti.

Jotta sovellus toimii oikein, tulee konfigurointitiedoston sisältää vastaava rivi:

`dbFile=*tiedostoNimi*`

Esimerkiksi rivin

`dbFile=esimerkki`

myötä kansioon luodaan "esimerkki.db"-tiedosto, kun sovellus käynnistetään.

## Sovelluksen käynnistäminen
Sovellus käynnistetään komennolla

`java -jar Kurssihallinta.jar`

Alla esitettynä joitain sovelluksen perustoiminnallisuuksia.

## Uuden kurssin luominen
Sovellus avautuu kurssinäkymään, josta voit joko etsiä kursseja tai luoda uusia kursseja. Painamalla alarivin "Add a new course"-painiketta pääset lisäysnäkymään, josta voit tallentaa uuden kurssin tietokantaan:

![Kurssilisäys](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/kurssilisays.png)

Lisättyäsi kurssin voit etsiä sitä nimeltä hakupalkin avulla:

![Kurssietsi](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/kurssietsi.png)

## Uuden opiskelijan luominen ja kurssirekisteröinnin lisääminen
Uuden opiskelijan voit lisätä tietokantaan siirtymällä sovelluksen yläpalkin "Registrations"-painikkeesta ilmoittautumisnäkymään. Painamalla alapalkin painiketta "Add a new registration" pääset näkymään, jossa voit valita, haluatko ilmoittaa kurssille tietokannasta jo löytyvän opiskelijan vai haluatko lisätä tietokantaan kokonaan uuden opiskelijan:

![Valintanakyma](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/valintanakyma.png)

Lisättyäsi opiskelijan tiedot ja painettuasi "Add to database"-painiketta ilmestyy sovellukseen uusi hakupalkki, josta voit etsiä tietokantaan lisättyjä kursseja. Valitse haluamasi kurssi painamalla sitä, jolloin se muuttuu siniseksi, ja paina "Select course"-painiketta. Kurssi-ilmoittautuminen on nyt lisätty.

## Uuden oppitunnin lisääminen
Jotta voit lisätä oppitunteja, tietokannassa tulee olla lisättynä luokkahuoneita. Voit tehdä tämän "Lessons"-näkymässä. Lisätäksesi kurssille oppitunnin navigoi ensin yksittäisen kurssin kurssinäkymään. Voit tehdä tämän etsimällä kursseja ja painamalla "Go to course page" -painiketta. Pääset kurssinäkymään:

![Kurssinakyma](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/kurssinakyma.png)

Paina "Edit lessons" -painiketta. Voit nyt valita luokkahuoneen ja lisätä oppitunnin alkamis- ja loppumisajankohdat:

![Valintanakyma](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/oppituntinakyma.png)

Lisätty oppitunti näkyy myös "Lessons"-näkymässä, jossa voit selata oppitunteja ja rajata niitä luokkahuoneiden ja päivämäärän mukaan:

![Valintanakyma](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kuvat/oppitunnit.png)
