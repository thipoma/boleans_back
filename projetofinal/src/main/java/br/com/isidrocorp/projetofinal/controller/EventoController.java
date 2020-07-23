package br.com.isidrocorp.projetofinal.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.isidrocorp.projetofinal.dao.EventoDAO;
import br.com.isidrocorp.projetofinal.dto.PeriodoConsulta;
import br.com.isidrocorp.projetofinal.dto.VolumeAlarmes;
import br.com.isidrocorp.projetofinal.model.Evento;

@RestController
@CrossOrigin("*")
public class EventoController {

	@Autowired
	EventoDAO dao;

	@GetMapping("/eventos")
	public ArrayList<Evento> recuperarTodos() {
		ArrayList<Evento> lista;
		lista = (ArrayList<Evento>) dao.findByOrderByData();

		return lista;
	}

	@GetMapping("/eventos/alarmes/resumo")
	public ArrayList<VolumeAlarmes> recuperarResumoPorAlarme() {
		return dao.getAllWithName();
	}
	
	@PostMapping("/eventos/periodo")
	public ArrayList<Evento> recuperarPorPeriodo(@RequestBody PeriodoConsulta periodo){
		try {
			Date inicio = new SimpleDateFormat("yyyy-MM-dd").parse(periodo.getInicio());
			Date fim    = new SimpleDateFormat("yyyy-MM-dd").parse(periodo.getFim());
			ArrayList<Evento> lista = dao.findByDataBetweenOrderByData(inicio, fim);
			return lista;
		}
		catch(Exception ex) {
			return null;
		}
	}

	@PostMapping("/eventos/alarmes/periodo")
	public ArrayList<VolumeAlarmes> recuperarDeJaneiro() {
		try {
			Date inicio = new SimpleDateFormat("yyyy-MM-dd").parse("01/01/2020");
			Date fim    = new SimpleDateFormat("yyyy-MM-dd").parse("31/01/2020");

			return dao.getAllWithNameByPeriod(inicio, fim);
		} catch (Exception ex) {
			return null;
		}
	}
	
	@GetMapping("/eventos/alarmes/manual")
	public ArrayList<VolumeAlarmes> recuperarManual(){
		/*   passo 1 - recuperar TODOS os eventos
		 *   passo 2 - para cada evento recuperado, colocar uma variável que conte quantos 
		 *             são os eventos para cada alarme (sabemos que são 5 alarmes diferentes)
		 *   passo 3 - montar um ArrayList com os 5 objetos do tipo VolumeAlarmes e retornar
		 */
		ArrayList<Evento> listaCompleta = (ArrayList<Evento>) dao.findAll();
		ArrayList<VolumeAlarmes> lista = new ArrayList<VolumeAlarmes>();
		// fixo estas variáveis pois tenho apenas 5 alarmes
		int cont1 = 0, cont2 = 0, cont3 = 0, cont4 = 0, cont5 = 0;
	
		for (Evento e: listaCompleta) {
			switch(e.getAlarme().getId()) {
			case 1:
				cont1++; break;
			case 2:
				cont2++; break;
			case 3: 
				cont3++; break;
			case 4: 
				cont4++; break;
			case 5:
				cont5++; break;
			}
		}
		lista.add(new VolumeAlarmes(1, cont1));
		lista.add(new VolumeAlarmes(2, cont2));
		lista.add(new VolumeAlarmes(3, cont3));
		lista.add(new VolumeAlarmes(4, cont4));
		lista.add(new VolumeAlarmes(5, cont5));
		return lista;	
	}
	
	
	@PostMapping("/alarmes/eventos")
	public ArrayList<VolumeAlarmes> recuperarPeriodo(@RequestBody PeriodoConsulta periodo) {
		try {
			Date inicio = new SimpleDateFormat("yyyy-MM-dd").parse(periodo.getInicio());
			Date fim    = new SimpleDateFormat("yyyy-MM-dd").parse(periodo.getFim());
			ArrayList<VolumeAlarmes> listaAlarmes = (ArrayList<VolumeAlarmes>)dao.getAllWithNameByPeriod(inicio, fim);
			return listaAlarmes;
		} catch (Exception ex) {
			return null;
		}
	}
	

}