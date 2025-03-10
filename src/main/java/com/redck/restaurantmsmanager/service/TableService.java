package com.redck.restaurantmsmanager.service;

import com.redck.restaurantmsmanager.domain.Chair;
import com.redck.restaurantmsmanager.domain.Restaurant;
import com.redck.restaurantmsmanager.domain.Table;
import com.redck.restaurantmsmanager.logging.Logger;
import com.redck.restaurantmsmanager.logging.enumeration.LogTag;
import com.redck.restaurantmsmanager.repository.RestaurantRepository;
import com.redck.restaurantmsmanager.repository.TableRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TableService
{
    private static final Logger LOGGER = new Logger(TableService.class);

    private final TableRepository tableRepository;

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public TableService(final TableRepository tableRepository, final RestaurantRepository restaurantRepository)
    {
        this.tableRepository = tableRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Create Table
     * @param table table
     * @return table created
     */
    public Table createTable(final Table table)
    {
        // Check if the tableUid exists
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByUid(table.getRestaurantUid());
        //System.out.println(tableRepository.countByRestaurantUid(table.getRestaurantUid()));
        //System.out.println(restaurantOptional.get().getTablesCapacity());
        if (restaurantOptional.isEmpty())
        {
            throw new NullPointerException("Restaurant with UID " + table.getRestaurantUid() + " not found");
        }
        else if(tableRepository.countByRestaurantUid(table.getRestaurantUid()) < restaurantOptional.get().getTablesCapacity())
        {
            final Optional<Table> tableOptional = tableRepository.findByTableUid(table.getTableUid());

            if(tableOptional.isEmpty())
            {
                LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.TABLES, LogTag.PERSISTED),
                        "Create Table: " + table.toString());

                table.tableUid(UUID.randomUUID().toString());

                LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.RESTAURANTS, LogTag.EDITED),
                        "Edited Restaurant: " + restaurantOptional.get().toString());

                return tableRepository.save(table);
            }
            else
            {
                throw new NullPointerException("Table already exists!!");
            }

        }
        else
        {
            throw new IllegalStateException("Table creation failed. Restaurant with UID " + table.getRestaurantUid() +
                    " has reached its maximum table capacity: " + restaurantOptional.get().getTablesCapacity());
        }

    }

    /**
     * Get Table
     * @param tableUid table id.
     * @return table retrieved
     */
    public Table getTable(final String tableUid)
    {
        final Optional<Table> tableOptional = getTableById(tableUid, "Table does not exists!!");

        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.TABLES, LogTag.RETRIEVED),
                "Get Table by id: " + tableUid);

        return tableOptional.get();
    }

    /**
     * Edit Table.
     * @param tableUid table id
     * @param tableNew table new
     * @return table edited
     */
    public Table editTable(final String tableUid, final Table tableNew)
    {
        final Optional<Table> tableOptional = getTableById(tableUid,
                "Table to be edited not exists!!!");

        LOGGER.info(MDC.get("correlationId"),  Arrays.asList(LogTag.TABLES, LogTag.EDITED),
                "Edit Table by id " + tableUid);


        if(tableNew.getChairsCapacity() == 0)
        {
            tableNew.chairsCapacity(tableOptional.get().getChairsCapacity());
        }

        if(tableNew.getxPosition() == 0)
        {
            tableNew.xPosition(tableOptional.get().getxPosition());
        }

        if(tableNew.getyPosition() == 0)
        {
            tableNew.yPosition(tableOptional.get().getyPosition());
        }

        if(tableNew.getAngle() == 0)
        {
            tableNew.angle(tableOptional.get().getAngle());
        }

        if(tableNew.getWidth() == 0)
        {
            tableNew.width(tableOptional.get().getWidth());
        }

        if(tableNew.getHeight() == 0)
        {
            tableNew.height(tableOptional.get().getHeight());
        }

        tableOptional.get()
                .chairsCapacity(tableNew.getChairsCapacity())
                .xPosition(tableNew.getxPosition())
                .yPosition(tableNew.getyPosition())
                .angle(tableNew.getAngle())
                .width(tableNew.getWidth())
                .height(tableNew.getHeight());


        return tableRepository.save(tableOptional.get());
    }

    /**
     * Delete Table
     * @param tableUid table id
     * @return table deleted
     */
    public Table deleteTable(final String tableUid)
    {
        final Optional<Table> tableOptional = getTableById(tableUid, "Table to be deleted not exists!!");

        tableRepository.delete(tableOptional.get());

        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.TABLES, LogTag.DELETED),
                "Delete Table by id: " + tableUid);

        return tableOptional.get();
    }

    /**
     * Get All Tables from a Restaurant.
     * @return List of Tables.
     */
    public List<Table> getAllTablesFromRestaurant(String restaurantUid)
    {
        List<Table> tableList = tableRepository.findByRestaurantUid(restaurantUid);

        if (tableList.isEmpty()) {
            throw new NullPointerException("No Tables persisted!!");
        } else {
            LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.TABLES, LogTag.RETRIEVED),
                    "Get All Tables for restaurantUid: " + restaurantUid);
        }

        return tableList;

    }

    /**
     * Get All Tables.
     * @return List of Tables.
     */
    public List<Table> getAllTables()
    {
        final List<Table> tableList = tableRepository.findAll();
        if(tableList.isEmpty())
        {
            throw new NullPointerException("No Tables persisted!!");
        }
        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.TABLES, LogTag.RETRIEVED),
                "Get All Tables from database");
        return tableList;

    }

    /**
     * Find Table on Repository
     * @param tableUid table Uid.
     * @param exceptionMessage exception Message
     * @return Optional of Table
     */
    private Optional<Table> getTableById(final String tableUid, final String exceptionMessage)
    {
        final Optional<Table> tableOptional = tableRepository.findByTableUid(tableUid);
        if(tableOptional.isEmpty())
        {
            throw new NullPointerException(exceptionMessage);
        }
        return tableOptional;
    }
}
