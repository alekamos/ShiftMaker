package it.costanza.shiftgenerator.command;

import model.ShiftCalendar;

import java.util.Calendar;

public interface ICalendarMaker {

    ShiftCalendar generate();
}
