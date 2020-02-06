# ObligatoriskOppgave1 #

## Organisering av team ##

Kundekontakt: Martin Kydland

Leder: Johanna Jøsang

Kompetansen er nokså jevnt fordelt over gruppen.Martin er people-person og flinkest til å kommunisere. Han blir da kundekontakt, slik at han har ansvar for korrespondanse med Siv. Johanna blir leder og har da hovedansvaret for at prosjektet når sine mål. Dette er fordi Johanna har mest erfaring med bl.a. git og java.

Språket brukt i koding; variabelnavn, kommentarer, vil være på engelsk.


## Målet for prosjektet ##

Digital versjon av roborally, med alle regler og mekanikker som er med i brettspillet som f.eks:

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

## Prioritert liste over krav til første iterasjon ##

* Vise 2D grid laget med libGDX og Tiled
* Vise et objekt på brettet
* Kunne flytte et objekt på brettet

## Prosjekt prosess og fremgangsmåte ##

### Møter ###

Hvert møte skal begynne med at hvert teammedlem forteller om hvilket arbeid de har gjort på prosjektet siden forrige møte. Dersom de har ferdigskrevet og testet en implementasjon på sin branch, skal den pushes til main dersom den er godkjent av gruppen. Dersom det er uenigheter om godkjenningen, er det leder som har den avgjørende stemmen.

Utenom lab kl14 på Torsdager, så blir det møte kl16 hver mandag etter INF112 forelesning. Dersom det ikke er mandagsforelesning, flyttes møte til kl14. Møtetiden passer bra for alle på mandager, i tillegg til at tidspunktet gir noen dagers mellomrom fra torsdagsmøte til egenjobbing.

### Prosjektmetodikk ### 

I denne iterasjonen så skal vi prøve å ta i bruk XP, samtidig som vi er åpen til endring ettersom ingen på gruppen har brukt denne prosjektmetodikken før. Vi vil prøve å vektlegge god kommunikasjon, som vi skal oppnå ved å bevisst bruke møtene der alle er tilstede som hovedkommunikasjonsmiddel.

### Arbeidsfordeling ###

Generell fordeling av arbeid har vi som mål å få ferdig ila første møte i en iterasjon etter at krav for den iterasjonen er satt. På dette første møte vil TODO’s legges til i project board, slik at teammedlemmer kan velge ut TODO’s i lista når de går tom for ting å gjøre.

### Kommunikasjon ###

Utenom møter vil kommunikasjon foregå på en egen Discord server for gruppen. Her er det mulighet for både kommunikasjon til hele gruppen, i tillegg til private chatter mellom medlemmer. Om nødvendig er voice chat også en mulighet på discord. Project board på GitHub skal brukes for å holde oversikt over framgangen i prosjektet.

## Oppsummering ## 

Vi har brukt en god del tid til å sette oss inn i libGDX og ser at vi fremdeles har mye å lære før vi kan vise spillet på optimal måte. Dermed er måten brett og spiller er satt opp på nå bare en veldig grov skisse som vi håper å lære mer fra, i det endelige målet om å lage et velfungerende og oversiktlige spill.

I første iterasjon var det ikke mye arbeid å gjøre når det kom til koding, derfor ble det en ubalansert fordeling i gruppen. Dette er noe vi kommer til å prøve å forbedre til neste iterasjon. 

Møtene virker som om de fungerer bra så langt. Vi får se om flere møter trengs etterhvert. 

Flere i gruppen har hatt store problemer med git. Ettersom dette er et verktøy som skal brukes, så må dette fikses snarest mulig. 

Ellers er vi fornøyd med prosjektet så langt.
