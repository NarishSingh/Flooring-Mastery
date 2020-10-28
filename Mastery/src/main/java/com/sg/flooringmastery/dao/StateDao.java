package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.State;
import com.sg.flooringmastery.service.InvalidStateException;
import java.util.List;

public interface StateDao {

    /**
     * Return all valid states from treemap as a list
     *
     * @return {List} all valid states
     * @throws StateReadException if cannot read from state tax data roster
     */
    List<State> getValidStates() throws StateReadException;

    /**
     * Validate the user's state request for a new or edited order
     *
     * @param userState {String} user's inputted state abbreviation, formatted
     *                  to be uppercase to match with a valid key
     * @return {State} the proper state obj corresponding to user's request
     * @throws InvalidStateException if user inputs an invalid state selection
     * @throws StateReadException if cannot read from state tax data roster
     */
    State readStateByID(String userState) throws InvalidStateException, StateReadException;

}
