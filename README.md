# SudokuWhiz
Progetto preparato per l'esame di **Fondamenti di Intelligenza Artificiale** che, tramite gli algoritmi di ricerca, mira a risolvere il gioco Sudoku.

### *Scopo del progetto*
In questo progetto si intende costruire un agente intelligente in grado di risolvere il gioco del Sudoku in modo
corretto e nel minor tempo possibile.
La soluzione del gioco, ovvero una griglia Sudoku completamente riempita e che rispetta le regole
del gioco, verrà determinata applicando i seguenti algoritmi di ricerca: **backtracking**, **ricerca A***,
**simulated annealing**, **algoritmi genetici generazionali**.

### Stato del progetto
In Lavorazione

### *Come iniziare*
#### Prerequisiti
- Java (versione consigliata: [8])
- Maven (versione consigliata: [3.8.6])
#### Avvio
1. Clona il repository sul tuo computer:
   ```bash
   git clone https://github.com/SabatinoRaffaella/SudokuWhiz.git

2. Compila il progetto utilizzando Maven:
   ```bash
   mvn clean install

3. Esegui il programma
   ```bash
   java -jar target/SudokuWhizGrafica-1.0-SNAPSHOT.jar
   
  L'esecuzione del programma genererà un'interfaccia grafica (GUI).
  
  ![screenprova](https://github.com/SabatinoRaffaella/SudokuWhiz/assets/131981162/7403563c-6ec0-4006-b0d6-e17e769deaee)
  
#### Funzionamento
4. In questa interfaccia clicca il pulsante *Choose A File Here* per inserire la griglia Sudoku che vorresti far risolvere al nostro agente.
   Nella cartella *Griglie_test* del nostro progetto puoi scegliere, in base al livello di difficoltà, la griglia Sudoku da proporre oppure puoi fornire un file in formato .txt che contiene una griglia Sudoku
   con il seguente formato:

   ![screenboh](https://github.com/SabatinoRaffaella/SudokuWhiz/assets/131981162/4225695f-f402-4868-b606-e60ed447cec8)

   dove ogni riga del file è una riga della griglia 9 × 9 ed i numeri presenti in una riga sono separati
   l’uno dall’altro mediante uno spazio. Le caselle vuote sono indicate con il valore 0. 

5. Seleziona l'*algoritmo di ricerca* che vorresti utilizzare: backtracking, ricerca A*, simulated annealing.
   Di seguito è riportato un esempio della finestra aggiornata.
   
   ![finestra aggiornata](https://github.com/SabatinoRaffaella/SudokuWhiz/assets/131981162/56ace2b6-d893-42e3-8357-0fb4eb4cb149)

6. Una volta selezionati sia la griglia Sudoku che l'algoritmo di ricerca tra quelli disponibili, è possibile selezionare il pulsante *Run!*
Ecco un esempio in foto della finestra a fine esecuzione:

![finestra aggiornata2](https://github.com/SabatinoRaffaella/SudokuWhiz/assets/131981162/479608a7-f521-46eb-be91-70512265d5a5)

In alto a destra sono riportati: il tempo impiegato per trovare la soluzione, il numero di stati (o nodi dell'albero di ricerca nel caso di backtracking e di ricerca A*) utili per determinare la soluzione,
il numero di stati esplorati, il numero di stati generati.
N.B.: i valori sopra indicati fanno riferimento all'esecuzione del programma in un terminale avente processore pari a 2.27 GHz e memoria pari a 4GB.
   
### *Organizzazione dei package*

Il progetto è organizzato in tre packages.

- @oop21
Questo package contiene la classe principale SudokuWhizWhiz con relativo form che si occupa di generare la componente grafica (GUI)
con la finestra che viene mostrata all'utente quando viene avviato il programma, che si occupa di prendere
l'input inserito dall'utente. 
Altre classi contenuti nel package sono:

  - RunButtonPressed: Listener che si occupa di gestire l'evento legato alla pressione del pulsante *Run!*, utile per richiedere alla classe risolutore SudokuSolver la risoluzione, mediante i vari algoritmi di sopra 
elencati, della griglia Sudoku passata in input, interpretata dall'agente come una matrice 9x9.
  - MatrxiModel: Classe che si occupa della visualizzazione a video della matrice, salvandola come modello per una JTable.
  - SudokuSelected: Classe che gestisce la logica dietro la selezione del file iniziale, il recupero della matrice del Sudoku all'interno del file e
dell'aggiornamento del modello della tabella visualizzato a video.
  - SudokuSolver: Classe che contiene l'implementazione completa dell'algoritmo di backtracking e parte dell'implementazione dell'algoritmo di ricerca A*
le altre implementazioni relative agli algoritmi menzionati precedentemente saranno aggiunte in seguito.

- @graphics:
  - LayoutHelper: Classe che si occupa della disposizione, organizzazione e disposizione del timer, delle statistiche di esecuzione e della tabella
attraverso l'utilizzo di GridBagLayout.
  - SudokuRenderer: Classe che si occupa di far visualizzare graficamente la griglia Sudoku scelta prima dell'esecuzione dell'algoritmo e dopo come griglia Sudoku completa e corretta.

- @utils:
  - BoardState: Classe che contiene la logica che permette la valutazione e la selezione di una soluzione candidata per l'algoritmo A*.
  - SolutionStatistics: Classe che contiene le statistiche relative alla soluzione scelta dell'algoritmo quali gli stati (o i nodi) esplorati, gli stati totali generati e gli stati utili effettivamente per la soluzione; ciò consente di valutare le prestazioni dell'algoritmo scelto per arrivare alla soluzione che soddisfacesse i vincoli, rapidamente e in modo corretto.
Oltre a questo contiene le due euristiche che sono state implementate per l'algoritmo A*.
  - ManageMatrix: Classe che contiene al proprio interno diverse funzioni di utility per il recupero del file di input e la gestione della matrice. 



