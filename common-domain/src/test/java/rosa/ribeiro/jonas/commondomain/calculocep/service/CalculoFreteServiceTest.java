package rosa.ribeiro.jonas.commondomain.calculocep.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rosa.ribeiro.jonas.commondomain.calculocep.dto.ViaCepResponseDTO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculoFreteServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CalculoFreteService service;

    @Test
    @DisplayName("Deve cobrar R$ 10,00 para São Paulo (Local)")
    void deveCalcularFreteSP() {
        String cep = "01001-000";
        ViaCepResponseDTO mockResponse = new ViaCepResponseDTO(cep, "Praça da Sé", "Sé", "São Paulo", "SP");

        when(restTemplate.getForObject(anyString(), eq(ViaCepResponseDTO.class)))
                .thenReturn(mockResponse);

        BigDecimal valor = service.calcularFrete(cep);

        assertEquals(new BigDecimal("10.00"), valor);
    }

    @Test
    @DisplayName("Deve cobrar R$ 20,00 para Sudeste (ex: RJ)")
    void deveCalcularFreteSudeste() {
        String cep = "20000-000";
        ViaCepResponseDTO mockResponse = new ViaCepResponseDTO(cep, "Rua A", "Bairro B", "Rio de Janeiro", "RJ");

        when(restTemplate.getForObject(anyString(), eq(ViaCepResponseDTO.class)))
                .thenReturn(mockResponse);

        BigDecimal valor = service.calcularFrete(cep);

        assertEquals(new BigDecimal("20.00"), valor);
    }

    @Test
    @DisplayName("Deve cobrar R$ 25,00 para Sul (ex: RS)")
    void deveCalcularFreteSul() {
        String cep = "90000-000";
        ViaCepResponseDTO mockResponse = new ViaCepResponseDTO(cep, "Rua C", "Bairro D", "Porto Alegre", "RS");

        when(restTemplate.getForObject(anyString(), eq(ViaCepResponseDTO.class)))
                .thenReturn(mockResponse);

        BigDecimal valor = service.calcularFrete(cep);

        assertEquals(new BigDecimal("25.00"), valor);
    }

    @Test
    @DisplayName("Deve cobrar R$ 40,00 para outras regiões (ex: BA)")
    void deveCalcularFreteNordeste() {
        String cep = "40000-000";
        ViaCepResponseDTO mockResponse = new ViaCepResponseDTO(cep, "Rua E", "Bairro F", "Salvador", "BA");

        when(restTemplate.getForObject(anyString(), eq(ViaCepResponseDTO.class)))
                .thenReturn(mockResponse);

        BigDecimal valor = service.calcularFrete(cep);

        assertEquals(new BigDecimal("40.00"), valor);
    }

    @Test
    @DisplayName("Deve cobrar R$ 30,00 (Fallback) se o CEP não existir ou API retornar nulo")
    void deveRetornarFallbackSeCepInvalido() {
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponseDTO.class)))
                .thenReturn(null);

        BigDecimal valor = service.calcularFrete("99999-999");

        assertEquals(new BigDecimal("30.00"), valor);
    }

    @Test
    @DisplayName("Deve cobrar R$ 25,00 (Erro de Rede) se a API falhar")
    void deveRetornarFallbackSeApiFalhar() {
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponseDTO.class)))
                .thenThrow(new RestClientException("Serviço indisponível"));

        BigDecimal valor = service.calcularFrete("01001-000");

        assertEquals(new BigDecimal("25.00"), valor);
    }
}