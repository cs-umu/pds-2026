package pds.umulingo.adapters.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pds.umulingo.domain.ports.input.RankingService;
import pds.umulingo.domain.ports.input.dto.RankingInfoDTO;

@RestController
@RequestMapping("/ranking")
public class RankingEndpoint {

    private final RankingService rankingService;

    public RankingEndpoint(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping
    public ResponseEntity<RankingInfoDTO> rankingGlobal() {
    	RankingInfoDTO ranking = rankingService.rankingGlobal();
        return ResponseEntity.ok(ranking);
    }
}
