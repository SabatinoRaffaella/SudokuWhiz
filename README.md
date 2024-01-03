# SudokuWhiz
Progetto di intelligenza artificiale che tramite gli algoritmi di ricerca mira a risolvere i Sudoku.
#Scopo del progetto:
In questo progetto si intende costruire un agente intelligente in grado di risolvere il gioco in modo
corretto e nel minor tempo possibile.
La soluzione del gioco, ovvero una griglia Sudoku completamente riempita e che rispetta le regole
del gioco, verrà determinata applicando i seguenti algoritmi di ricerca: backtracking, ricerca A*,
simulated annealing, algoritmi genetici generazionali.
#Organizzazione dei package:
Il progetto è organizzato in tre package:
@oop21: 
Contiene la classe principale SudokuWhizWhiz con relativo form che si occupa di generare la componente grafica(GUI)
con la finestra che viene mostrata all'utente quando viene avviato il programma, che si occupa di prendere
l'input inserito dal utente dal apposito file chooser che compare una volta che l'utente clicca il pulsante "Choose A File Here": 
img(1): 
![screenprova](https://github.com/SabatinoRaffaella/SudokuWhiz/assets/131981162/7403563c-6ec0-4006-b0d6-e17e769deaee)
l'input richiesto è una griglia Sudoku 9x9 scritta in un file .txt dove ogni riga del file è una riga della griglia 9 × 9 ed i numeri presenti in una riga sono separati
l’uno dall’altro mediante uno spazio. Le caselle vuote sono indicate con il valore 0. Si propone un esempio in foto di una griglia Sudoku:
![screenboh](https://github.com/SabatinoRaffaella/SudokuWhiz/assets/131981162/4225695f-f402-4868-b606-e60ed447cec8)
Di seguito è riportato un esempio della finestra aggiornata una volta completata la selezione del input: img(2)
![finestra aggiornata](https://github.com/SabatinoRaffaella/SudokuWhiz/assets/131981162/56ace2b6-d893-42e3-8357-0fb4eb4cb149)
Si seleziona l'algoritmo che si vuole utilizzare tramite i pulsanti a selezione singola che si trovano all'interno della
finestra e che elencano le varie opzioni disponibili.
Una volta terminate la selezione del file e dell'algoritmo che si vuole utilizzare basta selezionare il pulsante Run!
Ecco un esempio in foto della finestra a fine esecuzione: img(3)
![finestra aggiornata2](https://github.com/SabatinoRaffaella/SudokuWhiz/assets/131981162/479608a7-f521-46eb-be91-70512265d5a5)

RunButtonPressed:
Listener che si occupa di gestire l'evento legato alla pressione del pulsante Run!
Occupandosi di richiedere l'esecuzione alla classe risolutore SudokuSolver che si occupa di usare i vari algoritmi di sopra 
elencati sulla matrice passata in input precedentemente.
MatrxiModel: 
Classe che si occupa della visualizzazione a video della matrice salvandola come modello per una JTable.
SudokuSelected:
Classe che gestisce la logica dietro la selezione del file iniziale, il recupero della matrice del Sudoku all'interno del file e
l'aggiornamento del modello della tabella visualizzato a video.
SudokuSolver:
Classe che contiene l'implementazione completa dell'algoritmo di backtracking e parte dell'implementazione dell'algoritmo A*
le altre implementazioni relative agli algoritmi menzionati precedentemente saranno aggiunte in seguito.
@graphics:
LayoutHelper:
Classe che si occupa della disposizione, organizzazione e disposizione del timer, delle statistiche di esecuzione e della tabella
attraverso l'utilizzo di GridBagLayout.
SudokuRenderer:
Classe che si occupa di renderizzare la tabella in modo tale che appaia come nell'immagine img(2) prima dell'esecuzione di un algoritmo
e img(3) dopo.

@utils:
BoardState: 
Classe che contiene la logica che permette la valutazione e la selezione di una soluzione candidata per l'algoritmo A*.
SolutionStatistics:
Classe che contiene le statistiche relative alla soluzione scelta dell'algoritmo quali i nodi esplorati, generati e soluzione 
quindi tutti i "passaggi" che l'algoritmo ha scelto per arrivare alla soluzione che soddisfacesse i vincoli, rapidamente e in modo corretto.
Oltre a questo contiene le due euristiche che sono state per l'algoritmo A*.
ManageMatrix:
Classe che contiene al proprio interno diverse funzioni di utility per il recupero dal file di input e la gestione della matrice. 



