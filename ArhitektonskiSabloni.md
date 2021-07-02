## Mikrokernel

Mikrokernel arhitektura se sadrži  od 2 glavne komponente
- srži sistema  
- plug-in komponenata 
 
Srž sistema sadrži minimalnu funkcionalnost za rad sistema.

Koristan je kada softver ima mnogo tačaka proširenja funkcionalnosti, a ne znamo sta će biti integrisano u budućnosti. 

Dodatne funkcionalnosti su podijeljene u plug-in komponentama pa njihova izmjena,brisanje ili dodavanje nema efekta
na druge jer su izolovane i nezavisne.

![1](https://user-images.githubusercontent.com/38840934/111918922-a8137e80-8a87-11eb-81d3-2f0d879c66f6.png)


## Višeslojna

Višeslojnu arhitekturu koristimo prilikom razvoja aplikacije čiju programsku logiku mozemo da "razbijemo" na manje komponente koje mogu da se postave na nekoliko različitih servera.

Svaki sloj je zasebna celina, stoga je moguće menjati jedan sloj nezavisno od ostalih.
Slojevi međusobno komuniciraju putem unapred definisanih API-a.

#### Osnovna podela

##### Prezentacioni sloj
Najviši sloj - predstavlja vizuelnu reprezentaciju sadržaja/servisa koji nudimo korisnicima programa. Korisnik moze da pristupi servisu preko različitih uređaja.
Prezentacioni sloj je esencijalan za interakciju korisnika sa aplikacijom, nešto poput frontend-a kod web aplikacija.

##### Aplikacioni sloj 
Srednji sloj koji sadrži biznis logiku aplikacije. Za naš projekat bismo u okviru aplikacionog sloja imali implementaciju komponenti koje omogućavaju pravljenje projekata, koji mogu da sadrze RQM, UseCase i Class dijagrame.

##### Sloj podataka
Najniži sloj arhitekture čija je osnovna dužnost da čuva/čita podatke iz baza podataka. Sloj podataka pruža API aplikacionom sloju preko kojeg mu pristupa.


## Mikroservisna arhitektura:

U rešenju sa mikroservisnom arhitekturom DesignerPowera klijent ima pristup frontend-u, frontend šalje zahteve na API_GATEWAY koji može da se realizuje putem REST-a.

API_GATEWAY pozivima komunicira sa ostalim servisima koji ne znaju jedni za druge, svaki ima kontakt samo sa API_GATEWAY-em.


![mirkoServis](https://user-images.githubusercontent.com/49309777/111916675-5e716680-8a7c-11eb-9668-0092d375746f.png)

Na sistem možemo da implementiramo komponentu "Registar servisa" koja pruža opise servisa, uputstva za formiranje zahteva itd.,
koji pomažu API_GATEWAY-u da održava bržu, bolju i jaču komunikaciju sa servisima.
![mirkoServisSaRegistrz](https://user-images.githubusercontent.com/49309777/111916679-616c5700-8a7c-11eb-8990-5722ed09fa0a.jpg)

Prednost ovakve arhitekture je da se mikroservisi mogu nezavisno primeniti i omogućavaju veću autonomiju tima,
skalabilni su i ukoliko dođe do greške može efikasno da se pronađe a samim tim i popravi.
Sem toga, našem timu dosta doprinosi olakšana podela zadatak kao i što kodiranje mikroservisa ima manje koda koji bi trebao da bude razuman celom timu

Zbog manje veličine projekta, mane održavanja i performanse servisa u mikroservisnom rešenju zanemarujemo.

DesignerPower očekujemo da će funkcionisati putem servisa zaduženih za modele koje naš projekat podržava, 
servisa za transformaciju(konverziju) i servisa za autentifikaciju.



