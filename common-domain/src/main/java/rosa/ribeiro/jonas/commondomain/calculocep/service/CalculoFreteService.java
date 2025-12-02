package rosa.ribeiro.jonas.commondomain.calculocep.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rosa.ribeiro.jonas.commondomain.calculocep.dto.ViaCepResponseDTO;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class CalculoFreteService {

    private static final String URL_VIACEP = "https://viacep.com.br/ws/{cep}/json/";
    private final RestTemplate restTemplate;

    private static final Set<String> SUDESTE = Set.of("SP", "RJ", "MG", "ES");
    private static final Set<String> SUL = Set.of("PR", "SC", "RS");

    public CalculoFreteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal calcularFrete(String cepDestino) {
        try {
            String url = URL_VIACEP.replace("{cep}", cepDestino.replace("-", "").trim());

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Bookstore-API/1.0");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ViaCepResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ViaCepResponseDTO.class
            );

            ViaCepResponseDTO endereco = response.getBody();

            if (endereco == null || endereco.uf() == null) {
                return new BigDecimal("30.00");
            }

            String uf = endereco.uf().toUpperCase();

            if ("SP".equals(uf)) {
                return new BigDecimal("10.00");

            } else if (SUDESTE.contains(uf)) {
                return new BigDecimal("20.00");

            } else if (SUL.contains(uf)) {
                return new BigDecimal("25.00");

            } else {
                return new BigDecimal("40.00");
            }

        } catch (Exception e) {
            System.err.println("Erro ao consultar ViaCEP (" + e.getClass().getSimpleName() + "): " + e.getMessage());

            return new BigDecimal("25.00");
        }
    }
}