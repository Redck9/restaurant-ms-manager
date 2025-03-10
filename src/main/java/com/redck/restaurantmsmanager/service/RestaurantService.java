package com.redck.restaurantmsmanager.service;

import com.redck.restaurantmsmanager.domain.*;
import com.redck.restaurantmsmanager.logging.enumeration.LogTag;
import com.redck.restaurantmsmanager.repository.ChairRepository;
import com.redck.restaurantmsmanager.repository.RestaurantRepository;
import com.redck.restaurantmsmanager.repository.TableRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.redck.restaurantmsmanager.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService
{
    private static final Logger LOGGER = new Logger(RestaurantService.class);
    private static final int DEFAULT_TABLES_CAPACITY = 1;

    private static final String DEFAULT_LOCATION = "Not Provided";
    private final RestaurantRepository restaurantRepository;



    @Autowired
    public RestaurantService(final RestaurantRepository restaurantRepository)
    {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Create Restaurant
     * @param restaurant restaurant
     * @return restaurant created
     */
    public Restaurant createRestaurant(final Restaurant restaurant)
    {
        final Optional<Restaurant> restaurantOptional = restaurantRepository.findByUid(restaurant.getUid());

        if(restaurantOptional.isEmpty())
        {
            LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.PERSISTED),
                        "Create Restaurant: " + restaurant.toString());

            restaurant.uid(UUID.randomUUID().toString());

            if(restaurant.getSchedule() == null || restaurant.getSchedule().isEmpty())
            {
                restaurant.schedule(createDefaultSchedule());
            }

            if(restaurant.getMenuItem() == null || restaurant.getMenuItem().isEmpty())
            {
                restaurant.menuItem(createDefaultMenuItems());
            }


            final Restaurant restaurantBuildEnriched = buildRestaurantInfo(restaurant);
            return restaurantRepository.save(restaurantBuildEnriched);
        }
        throw new NullPointerException("Restaurant already exists!!");
    }

    private Restaurant buildRestaurantInfo(final Restaurant restaurant)
    {
        if (restaurant.getTablesCapacity() <= 0) {
            restaurant.tablesCapacity(DEFAULT_TABLES_CAPACITY);
        }

        if(restaurant.getLocation().isEmpty())
        {
            restaurant.location(DEFAULT_LOCATION);
        }

        return restaurant;

    }

    private List<Schedule> createDefaultSchedule()
    {
        return new ArrayList<>();
    }

    private List<MenuItem> createDefaultMenuItems()
    {
        return new ArrayList<>();
    }

    /**
     * Get Restaurant
     * @param restaurantId restaurant id.
     * @return restaurant retrieved
     */
    public Restaurant getRestaurant(final String restaurantId)
    {
        final Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant does not exists!!");

        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.RETRIEVED),
                "Get Restaurant by id: " + restaurantId + ": " + restaurantOptional.get().toString());

        return restaurantOptional.get();
    }




    /**
     * Edit Restaurant.
     * @param restaurantId restaurant id
     * @param restaurantNew restaurant new
     * @return restaurant edited
     */
    public Restaurant editRestaurant(final String restaurantId, final Restaurant restaurantNew)
    {
        final Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId,
                "Restaurant to be edited not exists!!!");

        LOGGER.info(MDC.get("correlationId"),  Arrays.asList(LogTag.RESTAURANTS, LogTag.EDITED),
                "Edit Restaurant by id " + restaurantId);

        Restaurant currentRestaurant = restaurantOptional.get();

        if(restaurantNew.getName() != null)
        {
            currentRestaurant.setName(restaurantNew.getName());
        }

        if(restaurantNew.getType() != null)
        {
            currentRestaurant.setType(restaurantNew.getType());
        }

        if(restaurantNew.getLocation() != null)
        {
            currentRestaurant.setLocation(restaurantNew.getLocation());
        }

        if(restaurantNew.getTablesCapacity() != 0)
        {
            currentRestaurant.setTablesCapacity(restaurantNew.getTablesCapacity());
        }

        if(restaurantNew.getDescription() != null)
        {
            currentRestaurant.setDescription(restaurantNew.getDescription());
        }

        if(restaurantNew.getUserUid() != null)
        {
            currentRestaurant.setUserUid(restaurantNew.getUserUid());
        }

        if(restaurantNew.getSchedule() != null && !restaurantNew.getMenuItem().isEmpty())
        {
            updateSchedule(currentRestaurant.getSchedule(), restaurantNew.getSchedule());
        }

        // Handle menuItems update: Merge existing menuItems with the new ones.
        if (restaurantNew.getMenuItem() != null && !restaurantNew.getMenuItem().isEmpty())
        {
            updateMenuItem(currentRestaurant.getMenuItem(), restaurantNew.getMenuItem());
        }


        if(restaurantNew.getUrl() != null)
        {
            currentRestaurant.setUrl(restaurantNew.getUrl());
        }

        return restaurantRepository.save(currentRestaurant);
    }

    /**
     * Edit existing MenuItem.
     * @param currentMenuItems current list of MenuItems
     * @param newMenuItems updated MenuItem data
     */
    public void updateMenuItem(List<MenuItem> currentMenuItems, List<MenuItem> newMenuItems) {

        if (newMenuItems != null && !newMenuItems.isEmpty())
        {
            // Convert currentMenuItems to a Map using Stream API
            Map<String, MenuItem> menuItemsMap = currentMenuItems.stream()
                    .collect(Collectors.toMap(MenuItem::getName, item -> item));

            // Iterate over newMenuItems to update the existing items
            for (MenuItem newItem : newMenuItems) {

                // Find the corresponding menuItem in the map using its name as the key
                MenuItem currentItem = menuItemsMap.get(newItem.getName());

                if (currentItem != null) {
                    // Merge attributes, only updating non-null values from the incoming request

                    if (newItem.getDescription() != null) {
                        currentItem.setDescription(newItem.getDescription());
                    }

                    if (newItem.getPrice() != 0) {  // Assuming price should be non-zero to be valid
                        currentItem.setPrice(newItem.getPrice());
                    }

                    if (newItem.getCategory() != null) {
                        currentItem.setCategory(newItem.getCategory());
                    }

                    if (newItem.getIngredients() != null && !newItem.getIngredients().isEmpty()) {
                        currentItem.setIngredients(newItem.getIngredients());
                    }

                    if (newItem.isVegan() != null) {
                        currentItem.setVegan(newItem.isVegan());
                    }

                    if (newItem.isGlutenFree() != null) {
                        currentItem.setGlutenFree(newItem.isGlutenFree());
                    }

                    if (newItem.getCalories() != null) {
                        currentItem.setCalories(newItem.getCalories());
                    }

                    if (newItem.isAvailable() != null) {
                        currentItem.setAvailability(newItem.isAvailable());
                    }

                    if (newItem.getPreparationTime() != null) {
                        currentItem.setPreparationTime(newItem.getPreparationTime());
                    }

                    if (newItem.getAvailableDays() != null && !newItem.getAvailableDays().isEmpty()) {
                        currentItem.setAvailableDays(newItem.getAvailableDays());
                    }
                }
            }
        }

    }


    /**
     * Add MenuItem to Restaurant.
     * @param restaurantId restaurant id
     * @param newMenuItem new MenuItem to add
     * @return updated restaurant
     */
    public Restaurant addMenuItem(final String restaurantId, final MenuItem newMenuItem) {
        Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant not found");
        if(restaurantOptional.isEmpty())
        {
            throw new RuntimeException("Restaurant not found");
        }

        Restaurant restaurant = restaurantOptional.get();

        List<MenuItem> currentMenuItems = restaurant.getMenuItem();
        if (currentMenuItems.stream().anyMatch(item -> item.getName().equals(newMenuItem.getName())))
        {
            throw new RuntimeException("MenuItem already exists");
        }

        currentMenuItems.add(newMenuItem);
        restaurant.setMenuItem(currentMenuItems);

        return restaurantRepository.save(restaurant);
    }

    /**
     * Delete MenuItem from Restaurant.
     * @param restaurantId restaurant id
     * @param menuItemName name of the MenuItem to delete
     * @return updated restaurant
     */
    public Restaurant deleteMenuItem(final String restaurantId, final String menuItemName) {
        Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant not found");
        if(restaurantOptional.isEmpty())
        {
            throw new RuntimeException("Restaurant not found");
        }

        Restaurant restaurant = restaurantOptional.get();

        List<MenuItem> currentMenuItems = restaurant.getMenuItem();
        boolean removed = currentMenuItems.removeIf(item -> item.getName().equals(menuItemName));
        if (!removed)
        {
            throw new RuntimeException("MenuItem with name " + menuItemName + " not found");
        }
        restaurant.setMenuItem(currentMenuItems);

        return restaurantRepository.save(restaurant);
    }

    /**
     * Get Menu from Restaurant.
     * @param restaurantId restaurant id
     * @return MenuItems List from a restaurant
     */
    public List<MenuItem> getMenu(final String restaurantId)
    {
        Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant not found");
        if(restaurantOptional.isEmpty())
        {
            throw new RuntimeException("Restaurant not found");
        }

        return restaurantOptional.get().getMenuItem();
    }

    /**
     * Get MenuItem from Restaurant.
     * @param restaurantId restaurant id
     * @param menuItemName restaurant menu item name
     * @return Schedule Day from a restaurant
     */
    public MenuItem getMenuItemByName(final String restaurantId, final String menuItemName)
    {
        Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant not found");
        if(restaurantOptional.isEmpty())
        {
            throw new RuntimeException("Restaurant not found");
        }

        return restaurantOptional.get()
                .getMenuItem()
                .stream()
                .filter(menuItem -> menuItem.getName().equalsIgnoreCase(menuItemName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Menu with item named " + menuItemName + " not found."));
    }

    /**
     * Add Schedule to Restaurant.
     * @param restaurantId restaurant id
     * @param newSchedule new Schedule to add
     * @return updated restaurant
     */
    public Restaurant addSchedule(final String restaurantId, final Schedule newSchedule)
    {
        Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant not found");
        if(restaurantOptional.isEmpty())
        {
            throw new RuntimeException("Restaurant not found");
        }

        Restaurant restaurant = restaurantOptional.get();

        List<Schedule> currentSchedule = restaurant.getSchedule();
        if (currentSchedule.stream().anyMatch(item -> item.getDay().equals(newSchedule.getDay())))
        {
            throw new RuntimeException("MenuItem already exists");
        }

        currentSchedule.add(newSchedule);
        restaurant.setSchedule(currentSchedule);

        return restaurantRepository.save(restaurant);
    }

    /**
     * Delete Schedule from Restaurant.
     * @param restaurantId restaurant id
     * @param scheduleDay day of the schedule to delete
     * @return updated restaurant
     */
    public Restaurant deleteSchedule(final String restaurantId, final String scheduleDay)
    {
        Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant not found");
        if(restaurantOptional.isEmpty())
        {
            throw new RuntimeException("Restaurant not found");
        }

        Restaurant restaurant = restaurantOptional.get();

        List<Schedule> currentSchedule = restaurant.getSchedule();
        boolean removed = currentSchedule.removeIf(item -> item.getDay().equals(scheduleDay));
        if (!removed)
        {
            throw new RuntimeException("Schedule with day " + scheduleDay + " not found");
        }
        restaurant.setSchedule(currentSchedule);

        return restaurantRepository.save(restaurant);
    }

    /**
     * Edit existing Schedule.
     * @param currentSchedule current list of schedule
     * @param newSchedule updated Schedule data
     */
    public void updateSchedule(List<Schedule> currentSchedule, List<Schedule> newSchedule)
    {

        if (newSchedule != null && !newSchedule.isEmpty())
        {
            // Convert currentMenuItems to a Map using Stream API
            Map<String, Schedule> scheduleMap = currentSchedule.stream()
                    .collect(Collectors.toMap(Schedule::getDay, item -> item));

            // Iterate over newMenuItems to update the existing items
            for (Schedule newScheduleDay : newSchedule)
            {

                // Find the corresponding menuItem in the map using its name as the key
                Schedule currentScheduleDay = scheduleMap.get(newScheduleDay.getDay());

                if (currentScheduleDay != null)
                {
                    // Merge attributes, only updating non-null values from the incoming request

                    if (newScheduleDay.getOpenTime() != null)
                    {
                        currentScheduleDay.setOpenTime(newScheduleDay.getOpenTime());
                    }

                    if (newScheduleDay.getCloseTime() != null)
                    {
                        currentScheduleDay.setCloseTime(newScheduleDay.getCloseTime());
                    }

                }
            }
        }

    }

    /**
     * Get Schedule from Restaurant.
     * @param restaurantId restaurant id
     * @return Schedule List from a restaurant
     */
    public List<Schedule> getSchedule(final String restaurantId)
    {
        Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant not found");
        if(restaurantOptional.isEmpty())
        {
            throw new RuntimeException("Restaurant not found");
        }

        return restaurantOptional.get().getSchedule();
    }

    /**
     * Get ScheduleDay from Restaurant.
     * @param restaurantId restaurant id
     * @return Schedule Day from a restaurant
     */
    public Schedule getScheduleByDay(final String restaurantId, final String day)
    {
        Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant not found");
        if(restaurantOptional.isEmpty())
        {
            throw new RuntimeException("Restaurant not found");
        }

        return restaurantOptional.get()
                .getSchedule()
                .stream()
                .filter(schedule -> schedule.getDay().equalsIgnoreCase(day))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Schedule for day " + day + " not found."));
    }


    /**
     * Delete Restaurant
     * @param restaurantId restaurant id
     * @return restaurant deleted
     */
    public Restaurant deleteRestaurant(final String restaurantId)
    {
        final Optional<Restaurant> restaurantOptional = getRestaurantById(restaurantId, "Restaurant to be deleted not exists!!");

        restaurantRepository.delete(restaurantOptional.get());

        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.DELETED),
                "Delete Restaurant by id: " + restaurantId);

        return restaurantOptional.get();
    }



    /**
     * Get All Restaurants.
     * @return List of Restaurants.
     */
    public List<Restaurant> getAllRestaurants()
    {
        final List<Restaurant> restaurantList = restaurantRepository.findAll();
        if(restaurantList.isEmpty())
        {
            throw new NullPointerException("No Restaurants persisted!!");
        }
        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.RETRIEVED),
                    "Get All Restaurants from database");
        return restaurantList;

    }

    /**
     * Find Restaurant on Repository
     * @param restaurantId restaurant Id.
     * @param exceptionMessage exception Message
     * @return Optional of Restaurant
     */
    private Optional<Restaurant> getRestaurantById(final String restaurantId, final String exceptionMessage)
    {
        final Optional<Restaurant> restaurantOptional = restaurantRepository.findByUid(restaurantId);
        if(restaurantOptional.isEmpty())
        {
            throw new NullPointerException(exceptionMessage);
        }
        return restaurantOptional;
    }



}
