package br.com.jdorigao.apidfe.controller;

import br.com.jdorigao.apidfe.service.DistribuicaoService;
import br.com.jdorigao.apidfe.service.NotaEntradaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notaEntrada")
@Slf4j
public class NotaEntradaController {

    private final NotaEntradaService notaEntradaService;
    private final DistribuicaoService distribuicaoService;

    public NotaEntradaController(NotaEntradaService notaEntradaService, DistribuicaoService distribuicaoService) {
        this.notaEntradaService = notaEntradaService;
        this.distribuicaoService = distribuicaoService;
    }

    @GetMapping(value = "consulta")
    public ResponseEntity<?> consulta() {
        try {
            distribuicaoService.consultaNotas();
            return ResponseEntity.ok(listarTodos());
        } catch (Exception e) {
            log.error("Erro ao listar NotaEntradas", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return ResponseEntity.ok(notaEntradaService.listaTudo());
        } catch (Exception e) {
            log.error("Erro ao listar NotaEntradas", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> listarPorId(@PathVariable("id") Long idNotaEntrada) {
        try {
            return ResponseEntity.ok(notaEntradaService.listarPorId(idNotaEntrada));
        } catch (Exception e) {
            log.error("Erro ao listar NotaEntrada", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "xml/{id}")
    public ResponseEntity<?> getXml(@PathVariable("id") Long idNotaEntrada) {
        try {
            return ResponseEntity.ok(notaEntradaService.getXml(idNotaEntrada));
        } catch (Exception e) {
            log.error("Erro ao listar NotaEntrada", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
