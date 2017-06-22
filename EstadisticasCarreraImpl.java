package fp.ciclismo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EstadisticasCarreraImpl implements EstadisticasCarrera {

	private String nombreCarrera;
	private List<Ganador> ganadores;

	/**
	 * @param nombre
	 *            Nombre de la carrera
	 */
	public EstadisticasCarreraImpl(String nombre) {
		this.nombreCarrera = nombre;
		this.ganadores = new ArrayList<Ganador>();
	}

	public EstadisticasCarreraImpl(String nombre, Collection<Ganador> ganadores) {
		this(nombre);
		this.ganadores = new ArrayList<Ganador>(ganadores);
	}

	@Override
	public String getNombreCarrera() {
		return this.nombreCarrera;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fp.ciclismo.EstadisticasTour#getGanadores()
	 */
	public List<Ganador> getGanadores() {
		return new ArrayList<Ganador>(ganadores);
	}

	public boolean equals(Object obj) {
		boolean res = false;
		if (obj != null && obj instanceof EstadisticasCarrera) {
			EstadisticasCarrera est = (EstadisticasCarrera) obj;
			res = getNombreCarrera().equals(est.getNombreCarrera()) && this.ganadores.equals(est.getGanadores());
		}
		return res;
	}

	public int hashCode() {
		return getNombreCarrera().hashCode() + 31 * this.ganadores.hashCode();
	}

	public String toString() {
		return getNombreCarrera() + " - " + this.ganadores;
	}

	// Una lista con los nombres de los ganadores que han recorrido
	// menos kilometros que los dados como parametro
	public List<String> getGanadoresConRecorridoInferiorA(Integer km) {
		return this.getGanadores().stream().filter(x -> x.getKmRecorridos() < km).map(Ganador::getNombre).distinct()
				.collect(Collectors.toList());
	}

	// El numero de ganadores distintos del tour. Si un ciclista ha
	// ganado el tour en varias ediciones, solo se debe contar una vez.
	public Long getNumeroGanadores() {

		return this.getGanadores().stream().mapToLong(Ganador::getNumEtapasGanadas).distinct().count();
	}

	// return true si todos los ganadores han ganado alguna etapa en la edicion
	// en la que ganaron el tour

	public Boolean hanGanadoTodosAlgunaEtapa() {
		return this.getGanadores().stream().allMatch(x -> x.getNumEtapasGanadas() > 1);
	}

	// Un conjunto con los nombres de los equipos que han ganado la carrera.
	public Set<String> getEquiposGanadores() {

		return this.getGanadores().stream().map(Ganador::getEquipo).collect(Collectors.toSet());
	}
	// El primer ganador que concuerde con el nombre dado como
	// parametro. Si no se encuentra ninguno, se devuelve null

	public Ganador buscaGanador(String nombre) {
		return this.getGanadores().stream().filter(x -> x.getNombre().equals(nombre)).findFirst().orElse(null);
	}
	
	//Igual que el metodo anterior, pero envez de buscar el primero por nombre, se busca por anyo.
	
	public Ganador buscaGanador(Integer anyo) {

		return this.getGanadores().stream().filter(x -> x.getAnyo().equals(anyo)).findFirst().orElse(null);
	}

	// El numero de kilometros de la edicion en la que se ha hecho el
	// recorrido mas corto. Si no se puede calcular, devuelve cero..
	public Integer getKmMenorRecorrido() {

		return this.getGanadores().stream().map(Ganador::getNumEtapasGanadas).min(Comparator.naturalOrder()).orElse(0);
	}

	// El nombre del ganador que ha alcanzado una mayor velocidad media
	// * en la edicion en la que ha sido ganador. Si no se puede calcular
	// * devuelve null.
	public String getGanadorMasRapido() {

		return this.getGanadores().stream().map(Ganador::getNombre).max(Comparator.naturalOrder()).orElse(null);
	}

	// Este no viene en el boletin
	// Igual que el anterior, pero se busca el mas rapido entre los ganadores de
	// la nacionalidad dada
	public String getGanadorMasRapido(String nacionalidad) {
		return null;
	}

	// La suma de los kilometros recorridos en todas las ediciones de la
	// carrera.
	public Integer calculaDistanciaTotal() {

		return this.getGanadores().stream().mapToInt(Ganador::getKmRecorridos).sum();
	}

	// Numero medio de etapas que han ganado los ciclistas que corren en
	// * ese equipo. Si no se puede calcular, devuelve cero.
	public Double getMediaEtapasGanadas(String equipo) {

		return this.getGanadores().stream().filter(x -> x.getEquipo().equals(equipo))
				.mapToDouble(Ganador::getNumEtapasGanadas).average().orElse(0);
	}
	// Map en el que las claves son las nacionalidades y los valores la
	// * lista de ganadores de esa nacionalidad

	public Map<String, List<Ganador>> getGanadoresPorNacionalidad() {
		return this.getGanadores().stream()
				.collect(Collectors.groupingBy(Ganador::getNacionalidad, Collectors.toList()));
	}

	// Map en el que las claves son las nacionalidades, y los valores el
	// numero de veces que un corredor de esa nacionalidad ha ganado la carrera
	public Map<String, Long> cuentaGanadoresPorNacionalidad() {
		return this.getGanadores().stream()
				.collect(Collectors.groupingBy(Ganador::getNacionalidad, HashMap::new, Collectors.counting()));
	}

}
