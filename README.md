# Kurssihallinnointi
Sovelluksen avulla opiston tai koulun on mahdollista pitää kirjaa sen järjestämistä kursseista ja kursseille osallistuneista opiskelijoista.

## Dokumentaatio
[Vaatimusmäärittely](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/vaatimusm%C3%A4%C3%A4rittely.md)

[Työaikakirjanpito](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/tuntikirjanpito.md)

[Arkkitehtuurikuvaus](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/arkkitehtuuri.md)

[Käyttöohje](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/kayttoohje.md)

[Tesatusdokumentti](https://github.com/okkokuisma/ot-harjoitustyo/blob/master/dokumentointi/testausdokumentti.md)

## Releaset
[Viikko 5](https://github.com/okkokuisma/ot-harjoitustyo/releases/tag/viikko5)

[Viikko 6](https://github.com/okkokuisma/ot-harjoitustyo/releases/tag/viikko6)

[Loppupalautus](https://github.com/okkokuisma/ot-harjoitustyo/releases/tag/loppupalautus)

## Komentorivitoiminnot

Sovelluksesta generoidaan suoritettava jar-tiedosto komennolla

`mvn package`

Jar-tiedosto ajetaan komennolla

`java -jar Kurssihallinta.jar`

Testit kattavuusraportilla ajetaan komennolla

`mvn test jacoco:report`

Sovelluksen JavaDoc generoidaan komennolla

`mvn javadoc:javadoc`

Chechstyle-tarkistus ajetaan komennolla

`mvn jxr:jxr checkstyle:checkstyle`


