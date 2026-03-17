package pds.umulingo.domain.ports.input;

import pds.umulingo.domain.ports.input.dto.RankingInfoDTO;

public interface RankingService {

	RankingInfoDTO rankingGlobal();
	
	RankingInfoDTO rankingDeUsuario(String usuarioId);
}
