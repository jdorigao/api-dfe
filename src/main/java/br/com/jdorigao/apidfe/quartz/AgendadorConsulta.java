package br.com.jdorigao.apidfe.quartz;

import br.com.jdorigao.apidfe.service.DistribuicaoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DisallowConcurrentExecution
public class AgendadorConsulta {

    private final DistribuicaoService distribuicaoService;

    public AgendadorConsulta(DistribuicaoService distribuicaoService) {
        this.distribuicaoService = distribuicaoService;
    }

    @Scheduled(initialDelay = (1000*60*10), fixedDelay = (1000*60*60))
    public void efetuaConsulta() {
        try {
            log.info("Iniciando consulta notas.");
            distribuicaoService.consultaNotas();
            log.info("Finalizado consulta notas.");
        } catch (Exception e) {
            log.error("Erro ao consultar notas", e);
        }
    }
}
