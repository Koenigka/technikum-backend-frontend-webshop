package at.technikum.webshop_backend.controller;

import at.technikum.webshop_backend.dto.PositionDTO;
import at.technikum.webshop_backend.model.Position;
import at.technikum.webshop_backend.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }


    //USER IS hardcoded!!!!
    @PostMapping
    public Position createPosition(@RequestBody @Valid PositionDTO positionDTO){
        return positionService.save(fromDTO(positionDTO), 1L, positionDTO.getProductId());
    }

    private static Position fromDTO(PositionDTO positionDTO) {
        return new Position(positionDTO.getId(),
                positionDTO.getQuantity());
    }

}
