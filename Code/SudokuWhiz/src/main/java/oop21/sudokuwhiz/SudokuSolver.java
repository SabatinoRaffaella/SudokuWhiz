package oop21.sudokuwhiz;

import ga.Algorithm;
import ga.FitnessCalc;
import ga.Population;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import utils.BoardState;
import utils.ManageMatrix;
import utils.SolutionStatistics;
import utils.BoardRandomizer;
import utils.SudokuPuzzle;

public class SudokuSolver {
	long countNodes = 0;
	Set<String> exploredNodes = new HashSet<>();
	ManageMatrix m = new ManageMatrix();
	private static final int SIZE = 9;
	private static final int SUBGRID_SIZE = 3;

	/// ALGORITMO DI BACKTRACKING
	/**
	 * Risolve la griglia Sudoku passata come parametro utilizzando il Backtracking.
	 * 
	 * @param sudo_m: matrice del sudoku da risolvere.
	 * @return sudo_m (soluzione del sudoku) o null nel caso in cui il sudoku non
	 *         sia risolvibile.
	 *
	 */
	public int[][] solve_sudoku_backtrack(int sudo_m[][], SolutionStatistics st) {
		if (solveSudoku_basic(sudo_m) == true) {
			st.recordSolutionStatistics(countNodes, exploredNodes.size(), countNodes);
			System.out.println("Numero nodi esplorati: " + countNodes);
			System.out.println("Numero nodi utili per la soluzione: " + exploredNodes.size());
			return sudo_m;
		} else
			return null;
	}

	private boolean solveSudoku_basic(int sudo_m[][]) {
		int row = 0;
		int col = 0;
		boolean checkBlankSpaces = false;
		/*
		 * controllo se il Sudoku è risolto e, in caso negativo,
		 * prendo la posizione della prossima cella "vuota"
		 */
		for (row = 0; row < sudo_m.length; row++) {
			for (col = 0; col < sudo_m[row].length; col++) {
				if (sudo_m[row][col] == 0) {
					checkBlankSpaces = true;
					break;
				}
			}
			if (checkBlankSpaces == true) {
				break;
			}
		}
		// se non ci sono più celle "vuote" significa che il Sudoku è stato risolto.
		if (checkBlankSpaces == false) {
			return true;
		}
		// cerco di riempire le celle "vuote" con il numero corretto
		for (int num = 1; num <= 9; num++) {
			/*
			 * isSafe controlla che num non sia già presente
			 * nella riga, colonna, o sotto-griglia 3x3
			 * (sotto le funzioni che si occupano di fare questi controlli)
			 */
			countNodes++;

			if (isSafe(sudo_m, row, col, num)) {
				sudo_m[row][col] = num;
				String nodeKey = row + "-" + col + "-" + num;
				exploredNodes.add(nodeKey);
				if (solveSudoku_basic(sudo_m)) {
					// table.getModel().setValueAt(num, row, col);
					return true;
				}
				/*
				 * se num è stato piazzato in una posizione scorretta,
				 * marco nuovamente la cella come "vuota", poi faccio il backtrack su
				 * un num differente su cui provare gli altri numeri che non sono stati provati
				 */
				sudo_m[row][col] = 0;
			}
		}
		return false;
	}

	private boolean isSafe(int sudo_m[][], int row, int col, int num) {
		return (!usedInRow(sudo_m, row, num) &&
				!usedInCol(sudo_m, col, num) &&
				!usedInBox(sudo_m, row - (row % 3), col - (col % 3), num));
	}

	/**
	 * 
	 * @param sudo_m: matrice in cui verificare la presenza del numero inserito
	 * @param row:    riga in cui controllare
	 * @param num:    numero di cui verificare la presenza
	 * @return true se il numero è presente, false altrimenti
	 */
	private boolean usedInRow(int sudo_m[][], int row, int num) {
		for (int col = 0; col < sudo_m.length; col++) {
			if (sudo_m[row][col] == num) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica la presenza del parametro num nella colonna col
	 * 
	 * @param sudo_m: matrice in cui verificare la presenza del numero inserito
	 * @param col:    colonna in cui controllare
	 * @param num:    numero di cui verificare la presenza
	 * @return true se il numero è presente, false altrimenti
	 */
	private boolean usedInCol(int sudo_m[][], int col, int num) {
		for (int row = 0; row < sudo_m.length; row++) {
			if (sudo_m[row][col] == num) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica la presenza del parametro num nella regione delimitata da
	 * boxStartRow
	 * e boxStartCol se lo trova restituisce true, false altrimenti
	 * 
	 * @param sudo_m:      matrice in cui verificare la presenza del numero inserito
	 * @param boxStartRow: riga in cui inizia la e-nesima regione in cui controllare
	 * @param boxStartCol: colonna in cui inizia la e-nesima regione in cui
	 *                     controllare
	 * @param num:         numero di cui verificare la presenza
	 * @return true se il numero è presente, false altrimenti
	 */
	private boolean usedInBox(int sudo_m[][], int boxStartRow, int boxStartCol, int num) {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (sudo_m[row + boxStartRow][col + boxStartCol] == num) {
					return true;
				}
			}
		}
		return false;
	}

	/// ALGORITMO DI RICERCA A*

	public int[][] solveSudoku_AsteriskA(int sudo_m[][], SolutionStatistics st) {
		int exploredNodes = 0; // contatore per tenere traccia dei nodi visitati durante la ricerca
		int totalGeneratedNodes = 0; // numero dei nodi generati
		Set<String> visitedStates = new HashSet<>();

		PriorityQueue<BoardState> queue = new PriorityQueue<>(Comparator.comparingDouble(BoardState::getTotalCost));
		queue.add(new BoardState(sudo_m, 0, BoardState.heuristic1(sudo_m)));
		visitedStates.add(getGridHash(sudo_m));
		boolean isRoot = true;

		while (!queue.isEmpty()) {
			BoardState currentNode;
			if (isRoot) {
				currentNode = queue.poll();
				isRoot = false;
			} else {
				currentNode = queue.poll();
			}

			if (currentNode.isGoal()) {
				BoardState.copyValues(currentNode.getGrid(), sudo_m);
				st.recordSolutionStatistics(exploredNodes, exploredNodes, totalGeneratedNodes);
				System.out.println("Numero nodi esplorati: " + exploredNodes);
				System.out.println("Numero totale dei nodi generati: " + totalGeneratedNodes);
				return currentNode.getGrid();
			}
			exploredNodes++;
			List<BoardState> successors = currentNode.generateSuccessors();
			totalGeneratedNodes++; // conteggio per ogni stato generato
			totalGeneratedNodes += successors.size();
			for (BoardState successor : successors) {
				String successorHash = getGridHash(successor.getGrid());
				if (!visitedStates.contains(successorHash)) {
					visitedStates.add(successorHash);
					boolean replace = false;
					for (BoardState nodeInQueue : queue) {
						if (Arrays.deepEquals(successor.getGrid(), nodeInQueue.getGrid()) &&
								successor.getTotalCost() < nodeInQueue.getTotalCost()) {
							replace = true;
							break;
						}
					}

					if (replace) {
						queue.remove(successor);
						queue.add(successor);
					} else {
						queue.add(successor);
					}
				}
			}
		}
		return sudo_m;
	}

	/*
	 * Funzione utilizzata per creare un identificativo per ogni stato visitato
	 * durante la ricerca A*
	 **/
	public String getGridHash(int[][] grid) {
		StringBuilder hashBuilder = new StringBuilder();
		for (int[] row : grid) {
			for (int cell : row) {
				hashBuilder.append(cell);
			}
		}
		return hashBuilder.toString();
	}

	// ALGORITMO DI RICERCA SIMULATED ANNEALING TRADIZIONALE

	public int[][] solveSudoku_SimulatedAnnealing_Tradizionale(int sudo_m[][], SolutionStatistics stat) {
		List<Integer> original_entries = m.getOriginalEntriesList(sudo_m);
		BoardRandomizer br = new BoardRandomizer();
		br.generateRandomSudoku(sudo_m);
		m.printMatrix(sudo_m);

		SudokuPuzzle currentSudokuPuzzle = new SudokuPuzzle(sudo_m, original_entries);
		SudokuPuzzle bestSudokuPuzzle = new SudokuPuzzle(sudo_m, original_entries);
		int currentScore = currentSudokuPuzzle.scoreBoard();
		int bestScore = currentScore;
		double temperature = 10000;
		double coolingFactor = 0.99995;
		int count = 0;
		for (double t = temperature; t > 1; t *= coolingFactor) {
			try {

				if (count % 1000 == 0) {
					System.out.printf("Iteration %d,\tT = %.5f,\tbest_score = %d,\tcurrent_score = %d%n",
							count, t, bestScore, currentScore);
				}
				int[][] candidateData = currentSudokuPuzzle.makeCandidateData(original_entries);
				SudokuPuzzle spCandidate = new SudokuPuzzle(candidateData, original_entries);
				int candidateScore = spCandidate.scoreBoard();

				if (currentSudokuPuzzle.acceptanceProbability(spCandidate, temperature) > Math.random()) {
					currentSudokuPuzzle = spCandidate;
					currentScore = candidateScore;
				}

				if (currentScore < bestScore) {

					bestSudokuPuzzle = new SudokuPuzzle(currentSudokuPuzzle.getData(), original_entries);
					bestScore = bestSudokuPuzzle.scoreBoard();
				}
				if (candidateScore == 0) {
					currentSudokuPuzzle = spCandidate;
					break;
				}
			} catch (Exception e) {
				System.out.println("Hit an inexplicable numerical error. It's a random algorithm-- try again.");
			}

			if (bestScore == 0) {
				System.out.println("\nSOLVED THE PUZZLE.");

			}
			count++;
		}
		stat.recordSolutionStatistics(count, count, count);
		return bestSudokuPuzzle.getData();
	}

	// ALGORITMO DI RICERCA SIMULATED ANNEALING - VARIANTE
	public int[][] solveSudoku_SimulatedAnnealing(int sudo_m[][], SolutionStatistics stat) {
		List<Integer> original_entries = m.getOriginalEntriesList(sudo_m);
		BoardRandomizer br = new BoardRandomizer();
		br.generateRandomSudoku(sudo_m);
		m.printMatrix(sudo_m);
		SudokuPuzzle sudokuPuzzle = new SudokuPuzzle(sudo_m);
		// sudokuPuzzle.randomizeOnZeroes();
		SudokuPuzzle bestSudokuPuzzle = new SudokuPuzzle(sudo_m, original_entries);
		int currentScore = sudokuPuzzle.scoreBoard();
		int bestScore = currentScore;
		double temperature = 100;
		double coolingFactor = 0.995;
		int max_iterations = 250000;
		int totalCount = 0;
		while (temperature > 1) {
			for (int count = 0; count < max_iterations; count++) {
				totalCount++;
				try {
					if (count % 1000 == 0) {
						System.out.printf("Iteration %d,\tT = %.5f,\tbest_score = %d,\tcurrent_score = %d%n",
								count, temperature, bestScore, currentScore);
					}
					int[][] candidateData = sudokuPuzzle.makeCandidateDataDup(original_entries);
					SudokuPuzzle spCandidate = new SudokuPuzzle(candidateData, original_entries);
					int candidateScore = spCandidate.scoreBoard();
					double deltaS = currentScore - candidateScore;
					if (candidateScore < currentScore || Math.exp(deltaS / temperature) - Math.random() > 0) {
						sudokuPuzzle = spCandidate;
						currentScore = candidateScore;
						if (currentScore < bestScore) {
							bestSudokuPuzzle = new SudokuPuzzle(sudokuPuzzle.getData(), original_entries);
							bestScore = bestSudokuPuzzle.scoreBoard();
						}
					}
					if (candidateScore == 0) {
						sudokuPuzzle = spCandidate;
						break;
					}
				} catch (Exception e) {
					System.out.println("Hit an inexplicable numerical error. It's a random algorithm-- try again.");
				}

				if (bestScore == 0) {
					System.out.println("\nSOLVED THE PUZZLE.");

				}
				totalCount += count;
			}
			temperature *= coolingFactor;
		}
		stat.recordSolutionStatistics(totalCount, totalCount, totalCount);
		return bestSudokuPuzzle.getData();
	}
        //ALGORITMO DI RICERCA GENETICO
    public int[][] solveSudoku_GeneticAlgorithm(int sudo_m[][]) {
        List<Integer> hints = m.getOriginalEntriesList(sudo_m);
         /*GA parametri*/
        int POPULATION_SIZE = 200;    
        Population startingPopulation;
        int MAX_GENERATIONS = 5000;       
        startingPopulation = new Population(POPULATION_SIZE,sudo_m);
        int generationCount = 0;
        FitnessCalc fit = new FitnessCalc();
            while (generationCount<MAX_GENERATIONS && startingPopulation.getFittest().getFitness() < fit.calculateTotalFitness(startingPopulation)) {
                generationCount++;
                System.out.println("Generation: " + generationCount + " Fittest: " + startingPopulation.getFittest().getFitness());
                startingPopulation = Algorithm.evolvePopulation(startingPopulation,sudo_m,hints);
                //ManageMatrix m= new ManageMatrix();
                //m.printMatrix(startingPopulation.getFittest().getSudo_m());
            }
            System.out.println("Solution found!");
            System.out.println("Generation: " + generationCount);
            System.out.println("Genes:");
            System.out.println(startingPopulation.getFittest());
            ManageMatrix m = new ManageMatrix();
            m.printMatrix(startingPopulation.getFittest().getSudo_m());           
            return startingPopulation.getFittest().getSudo_m();   
    }    
}
