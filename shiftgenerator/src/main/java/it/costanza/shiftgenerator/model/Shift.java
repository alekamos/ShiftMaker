package it.costanza.shiftgenerator.model;

import java.util.UUID;

/**
 * Union slot and owner ex slot: night_shift_1 owner id2
 */
public class Shift implements Comparable<Shift>{

    private UUID id;
    private Slot slot;
    private Worker worker;



    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Shift(Slot slot) {
        this.id = UUID.randomUUID();
        this.slot = slot;
    }

    @Override
    public int compareTo(Shift ohterShift) {
        return this.slot.compareTo(ohterShift.slot);
    }
}
