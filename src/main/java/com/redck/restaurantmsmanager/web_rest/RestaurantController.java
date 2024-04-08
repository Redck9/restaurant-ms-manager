package com.redck.restaurantmsmanager.web_rest;

import com.redck.restaurantmsmanager.domain.Restaurant;
import com.redck.restaurantmsmanager.service.RestaurantService;
import com.redck.restaurantmsmanager.service.mapper.RestaurantMapper;
import com.redck.restaurantmsmanager.service.model.RestaurantDTO;
import com.redck.restaurantmsmanager.service.model.TableDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;


@RestController
public class RestaurantController implements ApiController
{

    private static final String RESTAURANT_RESTAURANT_ID = "/restaurant/{restaurantId}";
    private static final String RESTAURANT = "/restaurant";
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;


    /**
     * Constructor for Restaurant Controller
     *
     * @param restaurantService Restaurant Service
     * @param restaurantMapper Restaurant Mapper
     */
    public RestaurantController(final RestaurantService restaurantService, RestaurantMapper restaurantMapper)
    {
        this.restaurantService = restaurantService;
        this.restaurantMapper = restaurantMapper;
    }

    /**
     * Controller to get a restaurantDTO by uid
     * @param restaurantId restaurantDTO uid to get
     * @return RestaurantDTO with the provided uid
     */
    @Operation(summary = "Get a restaurant by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the restaurant",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Restaurant not exists",
                    content = @Content)})
    @GetMapping(value = RESTAURANT_RESTAURANT_ID,
        produces = {"application/json"},
        consumes = {"application/json"})
    ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable final String restaurantId)
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(
                                    restaurantService.getRestaurant(restaurantId)));
    }

    /**
     * Controller to create a restaurantDTO
     * @param restaurantDTO restaurantDTO to create
     * @return RestaurantDTO created
     */
    @PostMapping(value = RESTAURANT,
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody final RestaurantDTO restaurantDTO)
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(
                restaurantService.createRestaurant(restaurantMapper.mapRestaurantDTOToRestaurant(restaurantDTO))));
    }

    /**
     * Controller to edit a restaurantDTO by uid
     * @param restaurantId restaurantDTO uid to be edited
     * @param restaurantToEdit restaurantDTO update
     * @return RestaurantDTO edited
     */
    @PutMapping(value = RESTAURANT_RESTAURANT_ID,
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<RestaurantDTO> editRestaurant(@PathVariable final String restaurantId, @RequestBody final Restaurant restaurantToEdit)
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(
                restaurantService.editRestaurant(restaurantId, restaurantToEdit)));
    }


    /**
     * Controller to delete restaurantDTO by uid
     * @param restaurantId restaurantDTO uid to be deleted
     * @return RestaurantDTO deleted
     */
    @DeleteMapping(value = RESTAURANT_RESTAURANT_ID,
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<RestaurantDTO> deleteRestaurant(@PathVariable final String restaurantId)
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(
                restaurantService.deleteRestaurant(restaurantId)));
    }

    /**
     * Controller to get a list with all restaurantsDTO
     * @return restaurantsDTO list
     */
    @GetMapping(value = RESTAURANT,
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<List<RestaurantDTO>> getAllRestaurants()
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantListToRestaurantDTOList(
                restaurantService.getAllRestaurants()));
    }


}
