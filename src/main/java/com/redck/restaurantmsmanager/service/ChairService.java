package com.redck.restaurantmsmanager.service;

import com.redck.restaurantmsmanager.domain.Chair;
import com.redck.restaurantmsmanager.domain.Table;
import com.redck.restaurantmsmanager.logging.Logger;
import com.redck.restaurantmsmanager.logging.enumeration.LogTag;
import com.redck.restaurantmsmanager.repository.ChairRepository;
import com.redck.restaurantmsmanager.repository.TableRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChairService
{
    private static final Logger LOGGER = new Logger(ChairService.class);

    private final ChairRepository chairRepository;

    private final TableRepository tableRepository;


    @Autowired
    public ChairService(final ChairRepository chairRepository, TableRepository tableRepository)
    {
        this.chairRepository = chairRepository;
        this.tableRepository = tableRepository;
    }

    /**
     * Create Chair
     * @param chair chair
     * @return chair created
     */
    public Chair createChair(final Chair chair)
    {
        // Check if the tableUid exists
        Optional<Table> tableOptional = tableRepository.findByTableUid(chair.getTableUid());

        if (tableOptional.isEmpty()) {
            throw new NullPointerException("Table with UID " + chair.getTableUid() + " not found");
        }
        else if(chairRepository.countByTableUid(chair.getTableUid()) < tableOptional.get().getChairsCapacity())
        {
            chair.chairUid(UUID.randomUUID().toString());

            final Optional<Chair> chairOptional = chairRepository.findByChairUid(chair.getChairUid());

            if(chairOptional.isEmpty())
            {
                LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.CHAIRS, LogTag.PERSISTED),
                        "Create Chair: " + chair.toString());

                return chairRepository.save(chair);
            }
            else
            {
                throw new NullPointerException("Chair already exists!!");
            }

        }
        else
        {
            throw new IllegalStateException("Chair creation failed. Table with UID " + chair.getTableUid() +
                    " has reached its maximum chair capacity: " + tableOptional.get().getChairsCapacity());
        }

    }

    /**
     * Get Chair
     * @param chairId chair id.
     * @return chair retrieved
     */
    public Chair getChair(final String chairId)
    {
        final Optional<Chair> chairOptional = getChairById(chairId, "Chair does not exists!!");

        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.CHAIRS, LogTag.RETRIEVED),
                "Get Chair by id: " + chairId);

        return chairOptional.get();
    }

    /**
     * Edit Chair.
     * @param chairId chair id
     * @param chairNew chair new
     * @return chair edited
     */
    public Chair editChair(final String chairId, final Chair chairNew)
    {
        final Optional<Chair> chairOptional = getChairById(chairId,
                "Chair to be edited not exists!!!");

        LOGGER.info(MDC.get("correlationId"),  Arrays.asList(LogTag.CHAIRS, LogTag.EDITED),
                "Edit Chair by id " + chairId);

        chairOptional.get()
                .tableUid(chairNew.getTableUid())
                .occupied(chairNew.isOccupied());

        return chairRepository.save(chairOptional.get());
    }

    /**
     * Delete Chair
     * @param chairId chair id
     * @return chair deleted
     */
    public Chair deleteChair(final String chairId)
    {
        final Optional<Chair> chairOptional = getChairById(chairId, "Chair to be deleted not exists!!");

        chairRepository.delete(chairOptional.get());

        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.CHAIRS, LogTag.DELETED),
                "Delete Chair by id: " + chairId);

        return chairOptional.get();
    }

    /**
     * Get All Chairs from a Table.
     * @return List of Chairs.
     */
    public List<Chair> getAllChairFromTable(String tableUid)
    {
        List<Chair> chairList = chairRepository.findChairByTableUid(tableUid);

        if (chairList.isEmpty()) {
            throw new NullPointerException("No Chairs persisted!!");
        } else {
            LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.CHAIRS, LogTag.RETRIEVED),
                    "Get All Chairs for tableUid: " + tableUid);
        }

        return chairList;

    }

    /**
     * Get All Chairs.
     * @return List of Chairs.
     */
    public List<Chair> getAllChair()
    {
        final List<Chair> chairList = chairRepository.findAll();
        if(chairList.isEmpty())
        {
            throw new NullPointerException("No Chairs persisted!!");
        }
        LOGGER.info(MDC.get("correlationId"), Arrays.asList(LogTag.CHAIRS, LogTag.RETRIEVED),
                "Get All Chairs from database");
        return chairList;

    }

    /**
     * Find Chair on Repository
     * @param chairId chair Uid.
     * @param exceptionMessage exception Message
     * @return Optional of Chair
     */
    private Optional<Chair> getChairById(final String chairId, final String exceptionMessage)
    {
        final Optional<Chair> chairOptional = chairRepository.findByChairUid(chairId);
        if(chairOptional.isEmpty())
        {
            throw new NullPointerException(exceptionMessage);
        }
        return chairOptional;
    }
}
