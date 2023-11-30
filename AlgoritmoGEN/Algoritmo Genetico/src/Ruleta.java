import java.util.ArrayList;
import java.util.Random;

public class Ruleta {
    public static ArrayList<Integer> seleccionarIndividuoRuleta(ArrayList<ArrayList<Integer>> poblacion, ArrayList<Double> porcentajes, ArrayList<Integer> mejorPoblacionfit) {
        Random rand = new Random();
        double valorRuleta = rand.nextDouble() * 100; // Genera un valor entre 0 y 100
        double sumaPorcentajes = 0;

        for (int i = 0; i < poblacion.size(); i++) {
            // Excluir mejorPoblacionfit de las opciones
            if (poblacion.get(i).equals(mejorPoblacionfit)) {
                continue;
            }

            sumaPorcentajes += porcentajes.get(i);

            // Si el valor de la ruleta cae dentro del rango del porcentaje acumulado, selecciona ese individuo
            if (valorRuleta <= sumaPorcentajes) {
                return poblacion.get(i);
            }
        }
        ArrayList<ArrayList<Integer>> opciones = new ArrayList<>(poblacion);
        opciones.remove(mejorPoblacionfit);
        return opciones.get(rand.nextInt(opciones.size()));
    }
}
