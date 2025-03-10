package com.redck.restaurantmsmanager.web_rest;

import com.redck.restaurantmsmanager.domain.MenuItem;
import com.redck.restaurantmsmanager.domain.Restaurant;
import com.redck.restaurantmsmanager.service.RestaurantService;
import com.redck.restaurantmsmanager.service.mapper.MenuItemMapper;
import com.redck.restaurantmsmanager.service.mapper.RestaurantMapper;
import com.redck.restaurantmsmanager.service.mapper.ScheduleMapper;
import com.redck.restaurantmsmanager.service.model.MenuItemDTO;
import com.redck.restaurantmsmanager.service.model.RestaurantDTO;
import com.redck.restaurantmsmanager.service.model.ScheduleDTO;
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

    private static final String RESTAURANT_RESTAURANT_ID = "/restaurant/{restaurantUid}";
    private static final String RESTAURANT = "/restaurant";
    private static final String RESTAURANT_MENU = "/restaurant/{restaurantUid}/menu";
    private static final String RESTAURANT_MENU_ITEM = "/restaurant/{restaurantUid}/menu/{menuItemName}";
    private static final String RESTAURANT_SCHEDULE = "/restaurant/{restaurantUid}/schedule";
    private static final String RESTAURANT_SCHEDULE_DAY = "/restaurant/{restaurantUid}/schedule/{day}";
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;
    private final ScheduleMapper scheduleMapper;


    /**
     * Constructor for Restaurant Controller
     *
     * @param restaurantService Restaurant Service
     * @param restaurantMapper Restaurant Mapper
     */
    public RestaurantController(final RestaurantService restaurantService, RestaurantMapper restaurantMapper, MenuItemMapper menuItemMapper, ScheduleMapper scheduleMapper)
    {
        this.restaurantService = restaurantService;
        this.restaurantMapper = restaurantMapper;
        this.menuItemMapper = menuItemMapper;
        this.scheduleMapper = scheduleMapper;
    }

    /**
     * Controller to get a restaurantDTO by uid
     * @param restaurantUid restaurantDTO uid to get
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
        produces = {"application/json"})
    ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable final String restaurantUid)
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(
                                    restaurantService.getRestaurant(restaurantUid)));
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
     * @param restaurantUid restaurantDTO uid to be edited
     * @param restaurantToEdit restaurantDTO update
     * @return RestaurantDTO edited
     */
    @PutMapping(value = RESTAURANT_RESTAURANT_ID,
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<RestaurantDTO> editRestaurant(@PathVariable final String restaurantUid, @RequestBody final RestaurantDTO restaurantToEdit)
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(
                restaurantService.editRestaurant(restaurantUid, restaurantMapper.mapRestaurantDTOToRestaurant(restaurantToEdit))));
    }


    /**
     * Controller to delete restaurantDTO by uid
     * @param restaurantUid restaurantDTO uid to be deleted
     * @return RestaurantDTO deleted
     */
    @DeleteMapping(value = RESTAURANT_RESTAURANT_ID,
            produces = {"application/json"})
    ResponseEntity<RestaurantDTO> deleteRestaurant(@PathVariable final String restaurantUid)
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(
                restaurantService.deleteRestaurant(restaurantUid)));
    }

    /**
     * Controller to get a list with all restaurantsDTO
     * @return restaurantsDTO list
     */
    @GetMapping(value = RESTAURANT,
            produces = {"application/json"})
    ResponseEntity<List<RestaurantDTO>> getAllRestaurants()
    {

        return ResponseEntity.ok(restaurantMapper.mapRestaurantListToRestaurantDTOList(
                restaurantService.getAllRestaurants()));
    }

    @PostMapping(value = RESTAURANT_MENU,
              produces = {"application/json"},
              consumes = {"application/json"})
    ResponseEntity<RestaurantDTO> addNewItemToRestaurantMenu(@PathVariable final String restaurantUid, @RequestBody final MenuItemDTO menuItemDTO)
    {
        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(restaurantService.addMenuItem(restaurantUid, menuItemMapper.mapMenuItemDTOToMenuItem(menuItemDTO))));
    }

    @DeleteMapping(value = RESTAURANT_MENU_ITEM,
                produces = {"application/json"})
    ResponseEntity<RestaurantDTO> removeItemFromRestaurantMenu(@PathVariable final String restaurantUid, @PathVariable final String menuItemName)
    {
        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(restaurantService.deleteMenuItem(restaurantUid, menuItemName)));
    }

    @GetMapping(value = RESTAURANT_MENU,
            produces = {"application/json"})
    ResponseEntity<List<MenuItemDTO>> getMenu(@PathVariable final String restaurantUid)
    {
        return ResponseEntity.ok(menuItemMapper.mapMenuItemListToMenuItemDTOList(
                restaurantService.getMenu(restaurantUid)));
    }

    @GetMapping(value = RESTAURANT_MENU_ITEM,
            produces = {"application/json"})
    ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable final String restaurantUid, @PathVariable final String menuItemName)
    {
        return ResponseEntity.ok(menuItemMapper.mapMenuItemToMenuItemDTO(
                restaurantService.getMenuItemByName(restaurantUid, menuItemName)));
    }

    @PostMapping(value = RESTAURANT_SCHEDULE,
                produces = {"application/json"},
                consumes = {"application/json"})
    ResponseEntity<RestaurantDTO> addNewDayToRestaurantSchedule(@PathVariable final String restaurantUid, @RequestBody final ScheduleDTO scheduleDTO)
    {
        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(restaurantService.addSchedule(restaurantUid, scheduleMapper.mapScheduleDTOToSchedule(scheduleDTO))));
    }

    @DeleteMapping(value = RESTAURANT_SCHEDULE_DAY,
                    produces = {"application/json"})
    ResponseEntity<RestaurantDTO> removeDayFromRestaurantSchedule(@PathVariable final String restaurantUid, @PathVariable final String day)
    {
        return ResponseEntity.ok(restaurantMapper.mapRestaurantToRestaurantDTO(restaurantService.deleteSchedule(restaurantUid, day)));
    }

    @GetMapping(value = RESTAURANT_SCHEDULE,
            produces = {"application/json"})
    ResponseEntity<List<ScheduleDTO>> getSchedule(@PathVariable final String restaurantUid)
    {
        return ResponseEntity.ok(scheduleMapper.mapScheduleListToScheduleDTOList(
                restaurantService.getSchedule(restaurantUid)));
    }

    @GetMapping(value = RESTAURANT_SCHEDULE_DAY,
            produces = {"application/json"})
    ResponseEntity<ScheduleDTO> getScheduleByDay(@PathVariable final String restaurantUid, @PathVariable final String day)
    {
        return ResponseEntity.ok(scheduleMapper.mapScheduleToScheduleDTO(
                restaurantService.getScheduleByDay(restaurantUid, day)));
    }

}
