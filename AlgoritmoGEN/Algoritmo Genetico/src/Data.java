import java.util.ArrayList;
import java.util.Random;

public class Data {

    public static void main(String[] args) {
        int numeroPoblaciones = 6;
        int tamanoPoblacion = 9;
        boolean encontrado = false;
        int generacion = 0;

        while (!encontrado) {
            // Array de todas las Poblaciones
            ArrayList<ArrayList<Integer>> todasLasPoblaciones = new ArrayList<>();

            for (int i = 0; i < numeroPoblaciones; i++) {
                ArrayList<Integer> poblacion = generarPoblacion(tamanoPoblacion);
                todasLasPoblaciones.add(poblacion);
                int fitness = CalcularFitness(todasLasPoblaciones.get(i));
                System.out.println("Población " + (i + 1) + ": " + todasLasPoblaciones.get(i) + " - Fitness: " + fitness);// Imprimir todas las poblaciones generadas
                if (fitness == 9) {
                    System.out.println("Cadena con fitness 9 encontrada en la generación " + generacion + ".");
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                ArrayList<Integer> mejorPoblacionfit = mejorFitness(todasLasPoblaciones);
                System.out.println("\nPadre: " + mejorPoblacionfit);

                int totalFitness = TotalFitness(todasLasPoblaciones);
                ArrayList<Double> porcentajes = PorcentajeTotal(todasLasPoblaciones);
                ArrayList<Integer> seleccionRuleta = Ruleta.seleccionarIndividuoRuleta(todasLasPoblaciones, porcentajes, mejorPoblacionfit);
                System.out.println("Madre: " + seleccionRuleta);

                ArrayList<ArrayList<Integer>> hijos = Crossover(mejorPoblacionfit, seleccionRuleta, todasLasPoblaciones);

                // Limpiar y llenar el ArrayList todasLasPoblaciones con los valores de los hijos
                todasLasPoblaciones.clear();
                todasLasPoblaciones.addAll(hijos);

                for (ArrayList<Integer> hijo : todasLasPoblaciones) {
                    //System.out.println("Hijo resultante: " + hijo);
                }

                realizarMutacion(todasLasPoblaciones.get(0), todasLasPoblaciones.get(1), todasLasPoblaciones);

                for (ArrayList<Integer> hijo : todasLasPoblaciones) {
                    //System.out.println("Hijo después de la mutación: " + hijo);
                }
            }

            generacion++;
        }



    }

    public static ArrayList<Integer> generarPoblacion(int tamanoPoblacion) {
        ArrayList<Integer> poblacion = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < tamanoPoblacion; i++) {
            int valorAleatorio = random.nextInt(2);
            poblacion.add(valorAleatorio);
        }

        return poblacion;
    }

    public static int CalcularFitness(ArrayList<Integer> poblacion) {
        int contadorUnos = 0;

        for (int numero : poblacion) {
            if (numero == 1) {
                contadorUnos++;
            }
        }
        return contadorUnos;
    }

    public static int TotalFitness(ArrayList<ArrayList<Integer>> todasLasPoblaciones) {
        int totalFitness = 0;

        for (ArrayList<Integer> poblacion : todasLasPoblaciones) {
            int fitness = CalcularFitness(poblacion);
            totalFitness += fitness;
        }
        return totalFitness;
    }

    public static ArrayList<Double> PorcentajeTotal(ArrayList<ArrayList<Integer>> todasLasPoblaciones) {
        int totalFitness = TotalFitness(todasLasPoblaciones);
        ArrayList<Double> porcentajes = new ArrayList<>();

        for (ArrayList<Integer> poblacion : todasLasPoblaciones) {
            int fitness = CalcularFitness(poblacion);
            double porcentaje = (double) fitness / totalFitness * 100;
            porcentajes.add(porcentaje);
        }

        return porcentajes;
    }


    public static ArrayList<Integer> mejorFitness(ArrayList<ArrayList<Integer>> todasLasPoblaciones) {
        int mejorFitness = -1; // Inicializar con un valor que sea menor que cualquier posible fitness
        ArrayList<Integer> mejorPoblacionfit = null;

        for (ArrayList<Integer> poblacion : todasLasPoblaciones) {
            int fitness = CalcularFitness(poblacion);
            // Actualizar si encontramos una población con un fitness mejor
            if (fitness > mejorFitness) {
                mejorFitness = fitness;
                mejorPoblacionfit = poblacion;
            }
        }

        return mejorPoblacionfit;
    }

    public static ArrayList<ArrayList<Integer>> Crossover(ArrayList<Integer> mejorPoblacionfit, ArrayList<Integer> seleccionRuleta, ArrayList<ArrayList<Integer>> todasLasPoblaciones) {
        int SeHace = 50, Mut = 50;
        Random random = new Random();
        int Valor = random.nextInt(101);

        if (Valor > SeHace) {
            // Si se hace el crossover
            System.out.println("Si se Hace CrossOver.");
            ArrayList<ArrayList<Integer>> hijos = realizarCrossover(mejorPoblacionfit, seleccionRuleta, todasLasPoblaciones);

            int Valor2 = random.nextInt(101);
            if (Valor2 > Mut) {
                System.out.println("Se Hace Mutacion.");
                realizarMutacion(hijos.get(0), hijos.get(1), todasLasPoblaciones);
            } else {
                System.out.println("No se Hace Mutacion.");
            }

            return hijos;
        } else {
            // No se Hace el CrossOver
            System.out.println("No se hizo CrossOver.");
            ArrayList<Double> porcentajes = PorcentajeTotal(todasLasPoblaciones);
            seleccionRuleta = Ruleta.seleccionarIndividuoRuleta(todasLasPoblaciones, porcentajes, mejorPoblacionfit);
            System.out.println("Nueva madre seleccionada: \n" + seleccionRuleta);

            ArrayList<ArrayList<Integer>> hijos = realizarCrossover(mejorPoblacionfit, seleccionRuleta, todasLasPoblaciones);

            realizarMutacion(hijos.get(0), hijos.get(1), todasLasPoblaciones);
            return hijos;
        }
    }

    private static ArrayList<ArrayList<Integer>> realizarCrossover(ArrayList<Integer> mejorPoblacionfit, ArrayList<Integer> seleccionRuleta, ArrayList<ArrayList<Integer>> todasLasPoblaciones) {
        int cuantosValores = new Random().nextInt(mejorPoblacionfit.size());

        ArrayList<Integer> hijo = new ArrayList<>();
        ArrayList<Integer> hijo2 = new ArrayList<>();

        // Intercambiar los valores entre mejorPoblacionfit y seleccionRuleta para el primer hijo
        for (int i = 0; i < cuantosValores; i++) {
            int temp = mejorPoblacionfit.get(i);
            mejorPoblacionfit.set(i, seleccionRuleta.get(i));
            seleccionRuleta.set(i, temp);
        }

        // Crear el primer hijo combinando las partes intercambiadas
        hijo.addAll(mejorPoblacionfit.subList(0, cuantosValores));
        hijo.addAll(seleccionRuleta.subList(cuantosValores, seleccionRuleta.size()));

        // Intercambiar los valores nuevamente para restaurar las listas originales
        for (int i = 0; i < cuantosValores; i++) {
            int temp = mejorPoblacionfit.get(i);
            mejorPoblacionfit.set(i, seleccionRuleta.get(i));
            seleccionRuleta.set(i, temp);
        }

        // Intercambiar los valores entre mejorPoblacionfit y seleccionRuleta para el segundo hijo
        for (int i = cuantosValores; i < seleccionRuleta.size(); i++) {
            int temp = mejorPoblacionfit.get(i);
            mejorPoblacionfit.set(i, seleccionRuleta.get(i));
            seleccionRuleta.set(i, temp);
        }

        // Crear el segundo hijo combinando las partes intercambiadas
        hijo2.addAll(mejorPoblacionfit.subList(0, cuantosValores));
        hijo2.addAll(seleccionRuleta.subList(cuantosValores, seleccionRuleta.size()));

        ArrayList<ArrayList<Integer>> hijos = new ArrayList<>();
        hijos.add(hijo);
        hijos.add(hijo2);

        // Imprimir los hijos resultantes del crossover
        //System.out.println("Primer hijo resultante del CrossOver: " + hijo);
        //System.out.println("Segundo hijo resultante del CrossOver: " + hijo2);

        return hijos;
    }

    public static void realizarMutacion(ArrayList<Integer> hijo, ArrayList<Integer> hijo2, ArrayList<ArrayList<Integer>> todasLasPoblaciones) {
        Random random = new Random();
        // Selecciona cual cromosoma se va a cambiar en el primer hijo
        int CromosomaHijo1 = random.nextInt(hijo.size());
        //System.out.println("Cromosoma a cambiar en el primer hijo: " + CromosomaHijo1 + "(+1)");
        int valorCromosoma1 = hijo.get(CromosomaHijo1);
        if (valorCromosoma1 == 0) {
            hijo.set(CromosomaHijo1, 1);
        } else {
            hijo.set(CromosomaHijo1, 0);
        }
        //System.out.println("Primer hijo después de la mutación: " + hijo);
        // Selecciona cual cromosoma se va a cambiar en el segundo hijo
        int CromosomaHijo2 = random.nextInt(hijo2.size());
        //System.out.println("Cromosoma a cambiar en el segundo hijo: " + CromosomaHijo2 + "(+1)");
        int valorCromosoma2 = hijo2.get(CromosomaHijo2);
        if (valorCromosoma2 == 0) {
            hijo2.set(CromosomaHijo2, 1);
        } else {
            hijo2.set(CromosomaHijo2, 0);
        }
        //System.out.println("Segundo hijo después de la mutación: " + hijo2);
    }

}
