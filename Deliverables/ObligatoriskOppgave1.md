# ObligatoriskOppgave1 #

## Organisering av team ##

Kundekontakt: Martin Kydland
Leder: Johanna Jøsang
Kompetansen er nokså jevnt fordelt over gruppen.Martin er people-person og flinkest til å kommunisere. Han blir da kundekontakt, slik at han har ansvar for korrespondanse med Siv. Johanna blir leder og har da hovedansvaret for at prosjektet når sine mål.


Målet for prosjektet: Digital versjon av roborally, med alle regler og mekanikker som er med i brettspillet slik som
* Noen options kort
* Sette sammen brett
* Mulighet for opp til 8 spillere
* Funksjonelle brett mekanismer av alle typer
* osv
Videre vil vi ha mulighet for AI, og evt videreutvikle spillet med andre brett-mekanismer, options kort og genererte brett.

## MVP ##

* Vise spillbrett
* Vise en spiller på brettet
  * Spiller vises bare posisjonen den skal være på, når den skal være der
* Vise flere spillere
  * Det er mulig å se forskjell på spillere
  * Spillere skal ikke vises hvis de ikke er med i spillet
* Kunne programmere robot
  * Man kan velge fra tildelt kort
* Ha “gjenstander” på brettet
* Ha en roborally kortstokk
* Dele ut kort
  * Et kort kan ikke deles ut til flere spillere samtidig
  * Spillere for utdelt færre kort om de har tilsvarende skade
* Låse kort
* Win / lose condition
  * Spillet avsluttes hvis noen har besøkt alle flaggene i riktig rekkefølge
  * Spillet avsluttes hvis alle spillerne har mistet alle livene sine
* Funksjonelle lasere
  * Vise lasere på slutten av hver fase
  * Stoppe lasere ved vegger
  * Gi skade til roboter som blir truffet av laser
* La roboter skyte laser
  * Laser skal skytes fra “hodet” til roboten i retning roboten står
  * Laser skytes fra roboten på slutten av hver fase
* Plassere flagg
  * Roboter kan flytte andre roboter hvis de står i veien
  * Men ikke hvis vegger vil forhindre det
* Låse kort ut i fra skade tatt
* Oppdatering av backup/respawnplass
* Besøke flagg
  * Må besøkes i riktig rekkefølge
* Powerdown
  * Mulighet for å annonsere powerdown
  * Være i powerdown
  * Komme tilbake igjen fra powerdown
