package it.costanza.shiftgenerator.service;

import it.costanza.shiftgenerator.model.Shift;
import it.costanza.shiftgenerator.model.Slot;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelService implements IUtil{

    /**
     * Convert a slot to empty shift, that is a shift without a worker
     * @param slot
     * @return
     */
    public Shift slotToShiftWithoutWorker(Slot slot){
        return new Shift(slot);

    }

    /**
     * Convert a list of slot to a list of empty shift, that is a shift without a worker
     * @param slot
     * @return
     */
    public List<Shift> slotToShiftWithoutWorker(List<Slot> slot){


        ArrayList<Shift> shiftList = new ArrayList<>();
        slot.forEach(elem->shiftList.add(new Shift(elem)));
        return shiftList;

    }



}
