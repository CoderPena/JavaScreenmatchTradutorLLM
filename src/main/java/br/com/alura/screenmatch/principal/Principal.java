package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=68d3e477";

    private List<DadosSerie> dadosSerie = new ArrayList<>();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar seŕies buscadas
                4 - Buscar série por nome
                5 - Buscar sério por ator
                6 - TOP 5 séries
                
                0 - Sair                                 
                """;
        int opcao = -1;

        while (opcao != 0) {
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarSerieTop5();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
//        dadosSerie.add(dados);
        repositorio.save(serie);

    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
//        System.out.println(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
//        DadosSerie dadosSerie = getDadosSerie();
        listarSeriesBuscadas();
        System.out.println("Digite o nome da séria: ");
        String nomeSerie = leitura.nextLine();

//        Optional<Serie> serieBuscada = series.stream()
//                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
//                .findFirst();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            var serieEncontrada = serieBuscada.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);

                List<Episodio> episodios = temporadas.stream()
                        .flatMap(d -> d.episodios().stream()
                                .map(e -> new Episodio(d.numero(), e)))
                        .collect(Collectors.toList()
                        );
                serieEncontrada.setEpisodios(episodios);
            }
            temporadas.forEach(System.out::println);
            repositorio.save(serieEncontrada);
        }else{
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
//        List<Serie> series = new ArrayList<>();
//        series = dadosSerie.stream()
//                .map(ds -> new Serie(ds))
//                .collect(Collectors.toList());
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);


//        "A group of ambitious misfits try to escape the harsh realities of high school by joining a glee club headed by a passionate Spanish teacher."
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da série: ");
        var nomeSerie = leitura.nextLine();
        Optional <Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBuscada.isPresent()){
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Digite a menor avaliação da série: ");
        var avaliacao = leitura.nextDouble();

//        List <Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCase(nomeAtor);
        List <Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou:");
        seriesEncontradas.stream()
                        .forEach(s -> System.out.println(s.getTitulo() + " - " + s.getAvaliacao()));

    }

    private void buscarSerieTop5() {
        List <Serie> seriesEncontradas = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesEncontradas.stream()
                .forEach(s -> System.out.println(s.getTitulo() + " - " + s.getAvaliacao()));
    }


}